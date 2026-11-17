# Setup and provider notes

## Local development

1. Install Android Studio and the Android SDK Platform 35 (or use an equivalent Android SDK installation).
2. Use a Java 17 JDK and Gradle 8.9 or later for the Android Gradle Plugin 8.7.3 build.
3. Open this repository in Android Studio, allow Gradle sync, and run the `app` module on an API 21+ emulator/device.
4. Or run `gradle :app:assembleDebug` from the repository root with Gradle installed and configured to use that JDK.

No signing key, provider credential, account or OAuth redirect URI is included in the repository. The AI helper sends a prompt only after the learner saves a provider key and confirms the send dialog. Requests use the platform's HTTPS certificate validation, bounded connect/read timeouts, no redirects and a capped response; errors do not include request headers. Review consent, privacy, retention and provider terms before using real credentials. Do not log API-key headers or distribute a shared provider key.

## AI provider authentication limits

The app's `AiClient` supports explicitly initiated provider requests, not OAuth. It sanitizes text, limits prompts to 4,000 characters, uses provider HTTPS endpoints and places credentials in headers where supported. It never silently calls a provider. The settings screen stores optional keys only after the user enters them and confirms saving.

- **Gemini:** the Gemini API commonly uses a Google AI API key; Google OAuth is not a universal substitute for a Gemini API key. OAuth availability depends on the specific Google API, project and scopes. Do not embed a service-account credential in a distributed app.
- **DeepSeek:** the API accepts a provider API key. This project does not assume or implement a public end-user OAuth flow.
- **OpenAI:** API usage is authenticated with an OpenAI API key. A ChatGPT login/subscription is not an API credential, and this project does not implement a public OAuth flow for API usage.

Provider products and authentication policies can change; confirm current provider documentation before enabling network calls. A native app cannot keep a user-supplied API key secret from a device owner or a compromised device. Keystore encryption protects stored data at rest, but does not make direct client-side provider billing safe for a production service. For a public release, prefer a backend that owns provider credentials and enforces user consent, quotas, abuse prevention and privacy rules. If a provider offers an approved native OAuth flow for the exact API and use case, integrate only its documented authorization-code/PKCE flow; never collect or store the user's provider password.

## Storage and language

The career catalog is seeded into a small SQLite database on first launch and remains available offline. The language selector includes localized core navigation, login and appearance labels for English, isiZulu, isiXhosa, Afrikaans, Sepedi and Setswana; career descriptions remain in English, and other languages are placeholders. Profile email and optional provider keys use Android Keystore-backed encryption. Clearing app data removes the local profile, history, preferences and stored keys.
