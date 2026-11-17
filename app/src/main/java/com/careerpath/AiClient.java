package com.careerpath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Builds provider-specific requests and sends them only when explicitly called.
 */
final class AiClient {
    enum Provider {
        GEMINI("Gemini"),
        DEEPSEEK("DeepSeek"),
        OPENAI("OpenAI");

        final String label;

        Provider(String label) {
            this.label = label;
        }

        static Provider fromLabel(String label) {
            for (Provider provider : values()) {
                if (provider.label.equals(label)) {
                    return provider;
                }
            }
            throw new IllegalArgumentException("Unsupported AI provider.");
        }
    }

    static final class Request {
        final URI endpoint;
        final Map<String, String> headers;
        final String body;

        Request(URI endpoint, Map<String, String> headers, String body) {
            this.endpoint = endpoint;
            this.headers = Collections.unmodifiableMap(new LinkedHashMap<>(headers));
            this.body = body;
        }
    }

    Request prepareRequest(Provider provider, String apiKey, String userText) {
        if (provider == null || apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Choose a provider and add its API key first.");
        }
        String prompt = sanitize(userText);
        if (prompt.isEmpty()) {
            throw new IllegalArgumentException("Enter a question first.");
        }
        String endpoint;
        JSONObject payload = new JSONObject();
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");
        try {
            switch (provider) {
                case GEMINI:
                    endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
                    headers.put("x-goog-api-key", apiKey);
                    JSONObject part = new JSONObject().put("text", prompt);
                    JSONArray parts = new JSONArray().put(part);
                    payload.put("contents", new JSONArray().put(new JSONObject().put("parts", parts)));
                    break;
                case DEEPSEEK:
                    endpoint = "https://api.deepseek.com/chat/completions";
                    headers.put("Authorization", "Bearer " + apiKey);
                    payload.put("model", "deepseek-chat");
                    payload.put("messages", new JSONArray().put(new JSONObject()
                            .put("role", "user").put("content", prompt)));
                    break;
                case OPENAI:
                    endpoint = "https://api.openai.com/v1/chat/completions";
                    headers.put("Authorization", "Bearer " + apiKey);
                    payload.put("model", "gpt-4o-mini");
                    payload.put("messages", new JSONArray().put(new JSONObject()
                            .put("role", "user").put("content", prompt)));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported AI provider.");
            }
        } catch (JSONException exception) {
            throw new IllegalStateException("Could not prepare the AI request.", exception);
        }
        URI uri = URI.create(endpoint);
        if (!"https".equalsIgnoreCase(uri.getScheme()) || uri.getHost() == null) {
            throw new IllegalStateException("AI providers must use HTTPS.");
        }
        return new Request(uri, headers, payload.toString());
    }

    String send(Request request) throws IOException, JSONException {
        if (request == null || !"https".equalsIgnoreCase(request.endpoint.getScheme())
                || !isProviderHost(request.endpoint.getHost())) {
            throw new IllegalArgumentException("AI requests must use an approved provider HTTPS endpoint.");
        }
        URL url = request.endpoint.toURL();
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        for (Map.Entry<String, String> header : request.headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }

        byte[] body = request.body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(body.length);
        try {
            try (OutputStream output = connection.getOutputStream()) {
                output.write(body);
            }
            int status = connection.getResponseCode();
            if (status < 200 || status >= 300) {
                throw new IOException(status >= 300 && status < 400
                        ? "The AI provider redirected the request; redirects are not followed."
                        : "The AI provider returned HTTP " + status + ".");
            }
            try (InputStream input = connection.getInputStream()) {
                String response = readBounded(input, 1024 * 1024);
                return extractResponse(response);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static boolean isProviderHost(String host) {
        return "generativelanguage.googleapis.com".equals(host)
                || "api.deepseek.com".equals(host)
                || "api.openai.com".equals(host);
    }

    private static String readBounded(InputStream input, int maxChars) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        StringBuilder text = new StringBuilder();
        char[] buffer = new char[4096];
        int count;
        while ((count = reader.read(buffer)) != -1) {
            if (text.length() + count > maxChars) {
                throw new IOException("The AI provider response exceeded the size limit.");
            }
            text.append(buffer, 0, count);
        }
        return text.toString();
    }

    private static String extractResponse(String response) throws JSONException, IOException {
        JSONObject root = new JSONObject(response);
        JSONArray choices = root.optJSONArray("choices");
        String answer = null;
        if (choices != null && choices.length() > 0) {
            JSONObject message = choices.getJSONObject(0).optJSONObject("message");
            if (message != null) {
                answer = message.optString("content", null);
            }
        } else {
            JSONArray candidates = root.optJSONArray("candidates");
            if (candidates != null && candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
                JSONArray parts = content == null ? null : content.optJSONArray("parts");
                if (parts != null && parts.length() > 0) {
                    answer = parts.getJSONObject(0).optString("text", null);
                }
            }
        }
        if (answer == null || answer.trim().isEmpty()) {
            throw new IOException("The AI provider returned no answer.");
        }
        return answer;
    }

    static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder clean = new StringBuilder();
        for (int i = 0; i < input.length() && clean.length() < 4000; i++) {
            char character = input.charAt(i);
            if (character == '\n' || character == '\t' || character >= 0x20) {
                clean.append(character);
            }
        }
        return clean.toString().trim();
    }
}
