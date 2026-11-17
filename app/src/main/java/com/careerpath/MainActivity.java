package com.careerpath;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MainActivity extends Activity {
    private static final String STATE_PREFS = "career_path_preferences";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_FOLLOWED = "followed_experts";
    private final Map<String, String[]> translations = new HashMap<>();
    private CareerDatabase database;
    private SecureStore secureStore;
    private SharedPreferences preferences;
    private LinearLayout content;
    private LinearLayout bottomNavigation;
    private FrameLayout contentContainer;
    private boolean darkMode;
    private String language = "English";
    private String currentPage = "Home";
    private String currentQuery = "";
    private String currentSector = "";
    private final ExecutorService aiExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void attachBaseContext(Context base) {
        String savedLanguage = base.getSharedPreferences(STATE_PREFS, MODE_PRIVATE)
                .getString("language", "English");
        Locale locale;
        if ("isiZulu".equals(savedLanguage)) {
            locale = new Locale("zu");
        } else if ("isiXhosa".equals(savedLanguage)) {
            locale = new Locale("xh");
        } else if ("Afrikaans".equals(savedLanguage)) {
            locale = new Locale("af");
        } else if ("Sepedi".equals(savedLanguage)) {
            locale = new Locale("nso");
        } else if ("Setswana".equals(savedLanguage)) {
            locale = new Locale("tn");
        } else {
            locale = Locale.ENGLISH;
        }
        Configuration configuration = new Configuration(base.getResources().getConfiguration());
        configuration.setLocale(locale);
        super.attachBaseContext(base.createConfigurationContext(configuration));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new CareerDatabase(this);
        secureStore = new SecureStore(this);
        preferences = getSharedPreferences(STATE_PREFS, MODE_PRIVATE);
        darkMode = preferences.getBoolean(KEY_DARK_MODE, false);
        language = preferences.getString("language", "English");
        initializeTranslations();
        setContentView(R.layout.activity_main);
        contentContainer = findViewById(R.id.content_container);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        TextView avatar = findViewById(R.id.header_avatar);
        String email = secureStore.get("session_email");
        if (email == null) {
            showLogin();
        } else {
            avatar.setText(email.substring(0, 1).toUpperCase(Locale.ROOT));
            showPage("Home");
        }
    }

    @Override
    protected void onDestroy() {
        aiExecutor.shutdownNow();
        database.close();
        super.onDestroy();
    }

    private void initializeTranslations() {
        translations.put("English", new String[]{"Home", "Explore", "Connect", "Settings", "Search careers, skills, subjects…"});
        translations.put("isiZulu", new String[]{"Ekhaya", "Hlola", "Xhumana", "Izilungiselelo", "Sesha imisebenzi, amakhono, izifundo…"});
        translations.put("isiXhosa", new String[]{"Ekhaya", "Khangela", "Nxibelelana", "Iisetingi", "Khangela imisebenzi, izakhono, izifundo…"});
        translations.put("Afrikaans", new String[]{"Tuis", "Verken", "Verbind", "Instellings", "Soek loopbane, vaardighede, vakke…"});
        translations.put("Sepedi", new String[]{"Gae", "Nyakišiša", "Kgokagano", "Dipeakanyo", "Nyaka mešomo, bokgoni, dithuto…"});
        translations.put("Setswana", new String[]{"Gae", "Batla", "Golagana", "Dithulaganyo", "Batla ditiro, bokgoni, dithuto…"});
    }

    private String tr(int index) {
        String[] selected = translations.get(language);
        return selected == null ? translations.get("English")[index] : selected[index];
    }

    private void showLogin() {
        bottomNavigation.setVisibility(View.GONE);
        contentContainer.removeAllViews();
        contentContainer.setBackgroundColor(color(R.color.background));
        ScrollView scroll = new ScrollView(this);
        LinearLayout page = column();
        page.setGravity(Gravity.CENTER_HORIZONTAL);
        page.setPadding(dp(28), dp(36), dp(28), dp(32));
        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.ic_rocket);
        logo.setContentDescription(getString(R.string.app_name));
        page.addView(logo, new LinearLayout.LayoutParams(dp(88), dp(88)));
        TextView title = text(getString(R.string.login_title), 28, true, color(R.color.ink));
        title.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = wrap();
        titleParams.topMargin = dp(18);
        page.addView(title, titleParams);
        TextView subtitle = text(getString(R.string.login_subtitle), 16, false, color(R.color.muted));
        subtitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams subtitleParams = wrap();
        subtitleParams.topMargin = dp(8);
        page.addView(subtitle, subtitleParams);

        EditText email = input(getString(R.string.email_hint), false);
        EditText password = input(getString(R.string.password_hint), true);
        LinearLayout.LayoutParams fieldParams = match();
        fieldParams.topMargin = dp(24);
        page.addView(email, fieldParams);
        LinearLayout.LayoutParams passwordParams = match();
        passwordParams.topMargin = dp(12);
        page.addView(password, passwordParams);
        Button login = button(getString(R.string.login_action), R.color.electric_blue);
        LinearLayout.LayoutParams loginParams = match();
        loginParams.topMargin = dp(20);
        page.addView(login, loginParams);
        TextView note = text(getString(R.string.local_account_note), 13, false, color(R.color.muted));
        note.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams noteParams = match();
        noteParams.topMargin = dp(16);
        page.addView(note, noteParams);
        login.setOnClickListener(v -> {
            String address = email.getText().toString().trim();
            String secret = password.getText().toString();
            if (!Patterns.EMAIL_ADDRESS.matcher(address).matches()) {
                email.setError("Enter a valid email address.");
                return;
            }
            if (secret.length() < 8) {
                password.setError("Use at least 8 characters.");
                return;
            }
            password.setText("");
            try {
                secureStore.put("session_email", address);
                secureStore.put("session_started", Long.toString(System.currentTimeMillis()));
                TextView avatar = findViewById(R.id.header_avatar);
                avatar.setText(address.substring(0, 1).toUpperCase(Locale.ROOT));
                showPage("Home");
            } catch (IllegalStateException exception) {
                showMessage("Could not save your encrypted local session. " + exception.getMessage());
            }
        });
        scroll.setFillViewport(true);
        scroll.addView(page);
        contentContainer.addView(scroll);
    }

    private void showPage(String pageName) {
        currentPage = pageName;
        if (contentContainer == null) {
            return;
        }
        bottomNavigation.setVisibility(View.VISIBLE);
        contentContainer.removeAllViews();
        contentContainer.setBackgroundColor(color(R.color.background));
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        content = column();
        content.setPadding(dp(18), dp(18), dp(18), dp(22));
        scroll.addView(content);
        contentContainer.addView(scroll);
        renderNavigation();
        TextView header = findViewById(R.id.header_title);
        if (header != null) {
            header.setText(getString(R.string.app_name));
        }
        if ("Home".equals(pageName)) {
            renderHome();
        } else if ("Explore".equals(pageName)) {
            renderExplore();
        } else if ("Connect".equals(pageName)) {
            renderCommunity();
        } else if ("Courses".equals(pageName)) {
            renderCourses();
        } else {
            renderSettings();
        }
    }

    private void renderNavigation() {
        bottomNavigation.removeAllViews();
        String[] pages = {"Home", "Explore", "Connect", "Settings"};
        int[] translationIndexes = {0, 1, 2, 3};
        for (int i = 0; i < pages.length; i++) {
            final String pageName = pages[i];
            TextView tab = text(tr(translationIndexes[i]), 12, pageName.equals(currentPage),
                    pageName.equals(currentPage) ? color(R.color.electric_blue) : color(R.color.muted));
            tab.setGravity(Gravity.CENTER);
            tab.setPadding(dp(6), 0, dp(6), 0);
            tab.setClickable(true);
            bottomNavigation.addView(tab, new LinearLayout.LayoutParams(0, -1, 1));
            tab.setOnClickListener(v -> showPage(pageName));
        }
        bottomNavigation.setBackgroundColor(color(R.color.surface));
    }

    private void renderHome() {
        String name = "there";
        String email = secureStore.get("session_email");
        if (email != null && email.contains("@")) {
            name = email.substring(0, email.indexOf('@'));
        }
        addTitle("Hello, " + name + " 👋", 25);
        addBody("Big dreams start with one small step. Explore careers that match your curiosity.");
        LinearLayout hero = card();
        hero.setBackground(backgroundResource(R.color.electric_blue, 18));
        TextView heroTitle = text("Your future is full of possibilities", 19, true, Color.WHITE);
        TextView heroBody = text("Discover real pathways, subjects and skills for South African careers.", 14, false, Color.WHITE);
        hero.addView(heroTitle);
        addGap(hero, 7);
        hero.addView(heroBody);
        addCardView(hero);

        addTitle("Explore 4 sectors", 20);
        String[][] sectors = {
                {"Technology", "Build the digital future", "💻"},
                {"Health", "Care for people and communities", "🩺"},
                {"Engineering & Energy", "Design and power a better world", "⚡"},
                {"Business & Finance", "Turn ideas into impact", "📈"}
        };
        for (int i = 0; i < sectors.length; i += 2) {
            LinearLayout row = row();
            for (int j = i; j < Math.min(i + 2, sectors.length); j++) {
                final String sector = sectors[j][0];
                LinearLayout tile = card();
                tile.setClickable(true);
                tile.setFocusable(true);
                TextView emoji = text(sectors[j][2], 22, true, color(R.color.electric_blue));
                TextView tileTitle = text(sectors[j][0], 15, true, color(R.color.ink));
                TextView tileBody = text(sectors[j][1], 12, false, color(R.color.muted));
                tile.addView(emoji);
                addGap(tile, 4);
                tile.addView(tileTitle);
                addGap(tile, 3);
                tile.addView(tileBody);
                LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(0, -2, 1);
                if (j % 2 == 0) {
                    tileParams.rightMargin = dp(8);
                } else {
                    tileParams.leftMargin = dp(8);
                }
                row.addView(tile, tileParams);
                tile.setOnClickListener(v -> {
                    currentSector = sector;
                    showPage("Explore");
                });
            }
            LinearLayout.LayoutParams rowParams = match();
            rowParams.topMargin = dp(10);
            content.addView(row, rowParams);
        }

        addTitle("Trending careers in South Africa", 20);
        addBody("A starter guide to growing fields for 2026. Job-market demand and pay vary by location and experience.");
        addGap(content, 8);
        List<Job> careers = database.searchCareers("", "");
        for (Job job : careers) {
            addJobCard(job);
        }
        Button courses = button("My learning history", R.color.energetic_coral);
        LinearLayout.LayoutParams courseParams = match();
        courseParams.topMargin = dp(6);
        content.addView(courses, courseParams);
        courses.setOnClickListener(v -> showPage("Courses"));
    }

    private void renderExplore() {
        addTitle(tr(1) + " careers", 25);
        addBody("Search jobs, subjects or pathways. Everything here works offline.");
        EditText search = input(tr(4), false);
        search.setSingleLine(true);
        search.setText(currentQuery);
        LinearLayout.LayoutParams searchParams = match();
        searchParams.topMargin = dp(14);
        content.addView(search, searchParams);
        Spinner sectors = new Spinner(this);
        List<String> options = new ArrayList<>();
        options.add("All sectors");
        options.addAll(database.getSectors());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectors.setAdapter(adapter);
        if (options.contains(currentSector)) {
            sectors.setSelection(options.indexOf(currentSector));
        } else if (currentSector.isEmpty()) {
            sectors.setSelection(0);
        }
        LinearLayout.LayoutParams sectorParams = match();
        sectorParams.topMargin = dp(8);
        content.addView(sectors, sectorParams);
        LinearLayout results = column();
        LinearLayout.LayoutParams resultsParams = match();
        resultsParams.topMargin = dp(18);
        content.addView(results, resultsParams);
        Runnable update = () -> {
            results.removeAllViews();
            String filter = sectors.getSelectedItem() == null ? "" : sectors.getSelectedItem().toString();
            if ("All sectors".equals(filter)) {
                filter = "";
            }
            List<Job> matches = database.searchCareers(currentQuery, filter);
            if (matches.isEmpty()) {
                results.addView(text("No careers found. Try another search.", 15, false, color(R.color.muted)));
            } else {
                for (Job job : matches) {
                    addJobCardTo(results, job);
                }
            }
        };
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString();
                update.run();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        sectors.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                currentSector = position == 0 ? "" : options.get(position);
                update.run();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
        update.run();
    }

    private void renderCommunity() {
        addTitle("Learn together, go further", 25);
        addBody("Follow people who share useful advice. External profiles open only when you tap a link.");
        LinearLayout membership = card();
        membership.setBackground(backgroundResource(R.color.sunburst_yellow, 16));
        membership.addView(text("Career Path Plus", 19, true, color(R.color.ink)));
        addGap(membership, 6);
        membership.addView(text("Expert sessions and extra learning tools are coming soon. No payment is collected in this preview.", 14, false, color(R.color.ink)));
        addCardView(membership);

        addTitle("Community feed", 20);
        addExpertCard("Thandi Mokoena", "Software engineer · Johannesburg",
                "I started with small coding projects. Try making something useful for your school or community.",
                "https://www.linkedin.com/");
        addExpertCard("Sipho Dlamini", "Renewable energy technician · Cape Town",
                "Curious about solar? Learn the basics of electricity and ask to shadow a qualified technician.",
                "https://www.linkedin.com/");
        addExpertCard("Ayesha Khan", "Data analyst · Durban",
                "A spreadsheet is a great first data tool. Ask a question, explore the numbers and share what you find.",
                "https://github.com/");
        addBody("Expert names and posts in this sample feed are illustrative, not verified endorsements.");
    }

    private void addExpertCard(String name, String role, String message, String profileUrl) {
        LinearLayout card = card();
        LinearLayout heading = row();
        LinearLayout identity = column();
        identity.addView(text(name, 17, true, color(R.color.ink)));
        identity.addView(text(role, 13, false, color(R.color.muted)));
        heading.addView(identity, new LinearLayout.LayoutParams(0, -2, 1));
        Button follow = smallButton("Follow", R.color.electric_blue);
        String followed = preferences.getString(KEY_FOLLOWED, "");
        boolean isFollowing = followed.contains("|" + name + "|");
        follow.setText(isFollowing ? "Following" : "Follow");
        heading.addView(follow);
        card.addView(heading);
        addGap(card, 12);
        card.addView(text(message, 15, false, color(R.color.ink)));
        TextView profile = text("View profile ↗", 14, true, color(R.color.electric_blue));
        LinearLayout.LayoutParams profileParams = wrap();
        profileParams.topMargin = dp(12);
        card.addView(profile, profileParams);
        profile.setOnClickListener(v -> openExternal(profileUrl));
        follow.setOnClickListener(v -> {
            String current = preferences.getString(KEY_FOLLOWED, "");
            boolean following = current.contains("|" + name + "|");
            current = following ? current.replace("|" + name + "|", "|")
                    : current + "|" + name + "|";
            preferences.edit().putString(KEY_FOLLOWED, current).apply();
            follow.setText(following ? "Follow" : "Following");
            follow.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        });
        addCardView(card);
    }

    private void renderCourses() {
        addTitle("Learning history", 25);
        addBody("Career pathways you explored are saved on this device.");
        List<String> history = database.getCourseHistory();
        if (history.isEmpty()) {
            addBody("Your list is empty. Open a career and tap “Save to my learning history” to keep it here.");
            return;
        }
        for (String item : history) {
            LinearLayout card = card();
            card.addView(text("📘  " + item, 16, true, color(R.color.ink)));
            addCardView(card);
        }
    }

    private void renderSettings() {
        addTitle(tr(3), 25);
        addBody("Your choices are stored locally on this device.");

        LinearLayout appearance = card();
        appearance.addView(text("Appearance", 18, true, color(R.color.ink)));
        SwitchRow theme = new SwitchRow(this, darkMode ? getString(R.string.mode_dark) : getString(R.string.mode_light), darkMode);
        appearance.addView(theme);
        theme.setOnCheckedChangeListener((button, checked) -> {
            darkMode = checked;
            preferences.edit().putBoolean(KEY_DARK_MODE, checked).apply();
            showPage("Settings");
        });
        addCardView(appearance);

        LinearLayout languageCard = card();
        languageCard.addView(text("Language", 18, true, color(R.color.ink)));
        List<String> languages = Arrays.asList("English", "isiZulu", "isiXhosa", "Afrikaans", "Sepedi", "Setswana", "More languages soon");
        Spinner languageSpinner = new Spinner(this);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languages);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setSelection(languages.indexOf(language));
        languageCard.addView(languageSpinner);
        languageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selection = languages.get(position);
                if (!selection.equals("More languages soon")) {
                    if (!selection.equals(language)) {
                        language = selection;
                        preferences.edit().putString("language", language).apply();
                        recreate();
                    }
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
        addCardView(languageCard);

        addAiSettings();

        Button learning = button("My learning history", R.color.electric_blue);
        LinearLayout.LayoutParams learningParams = match();
        learningParams.topMargin = dp(4);
        content.addView(learning, learningParams);
        learning.setOnClickListener(v -> showPage("Courses"));
        Button signOut = button("Sign out", R.color.energetic_coral);
        LinearLayout.LayoutParams signOutParams = match();
        signOutParams.topMargin = dp(10);
        content.addView(signOut, signOutParams);
        signOut.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("Sign out")
                .setMessage("Remove your encrypted local session from this device?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Sign out", (dialog, which) -> {
                    secureStore.remove("session_email");
                    secureStore.remove("session_started");
                    showLogin();
                }).show());
        addBody("This local preview does not create online accounts or collect subscription payments. AI prompts are sent only after you confirm each request.");
    }

    private void addAiSettings() {
        LinearLayout aiCard = card();
        aiCard.addView(text("AI career helper", 18, true, color(R.color.ink)));
        addGap(aiCard, 6);
        aiCard.addView(text("Choose a provider. API keys are encrypted at rest. Each prompt is sent only after you confirm.", 14, false, color(R.color.muted)));
        Spinner providers = new Spinner(this);
        List<String> providerNames = Arrays.asList("Gemini", "DeepSeek", "OpenAI");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, providerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        providers.setAdapter(adapter);
        String selectedProvider = preferences.getString("ai_provider", "Gemini");
        if (providerNames.contains(selectedProvider)) {
            providers.setSelection(providerNames.indexOf(selectedProvider));
        }
        LinearLayout.LayoutParams providerParams = match();
        providerParams.topMargin = dp(8);
        aiCard.addView(providers, providerParams);

        EditText keyInput = input("Provider API key (optional)", true);
        LinearLayout.LayoutParams keyParams = match();
        keyParams.topMargin = dp(8);
        aiCard.addView(keyInput, keyParams);
        String stored = secureStore.get("ai_key_" + selectedProvider.toUpperCase(Locale.ROOT));
        if (stored != null) {
            keyInput.setHint("API key saved securely · enter a new key to replace");
        }
        Button saveKey = smallButton("Save key securely", R.color.electric_blue);
        LinearLayout.LayoutParams saveParams = match();
        saveParams.topMargin = dp(8);
        aiCard.addView(saveKey, saveParams);

        EditText prompt = input("Ask about a career pathway…", false);
        prompt.setSingleLine(false);
        prompt.setMinLines(3);
        prompt.setGravity(Gravity.TOP | Gravity.START);
        prompt.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
                | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams promptParams = match();
        promptParams.topMargin = dp(10);
        aiCard.addView(prompt, promptParams);
        Button prepare = smallButton("Send to provider", R.color.energetic_coral);
        LinearLayout.LayoutParams prepareParams = match();
        prepareParams.topMargin = dp(8);
        aiCard.addView(prepare, prepareParams);
        TextView response = text("Your provider's response will appear here.", 14, false, color(R.color.muted));
        LinearLayout.LayoutParams responseParams = match();
        responseParams.topMargin = dp(10);
        aiCard.addView(response, responseParams);
        saveKey.setOnClickListener(v -> {
            String key = keyInput.getText().toString().trim();
            String provider = providers.getSelectedItem().toString();
            if (key.isEmpty()) {
                showMessage("Enter a provider key to save it.");
                return;
            }
            try {
                secureStore.put("ai_key_" + provider.toUpperCase(Locale.ROOT), key);
                preferences.edit().putString("ai_provider", provider).apply();
                keyInput.setText("");
                keyInput.setHint("API key saved securely · enter a new key to replace");
                showMessage("Saved encrypted on this device. No provider was contacted.");
            } catch (IllegalStateException exception) {
                showMessage("Could not encrypt this key. " + exception.getMessage());
            }
        });
        prepare.setOnClickListener(v -> {
            String provider = providers.getSelectedItem().toString();
            final AiClient.Request request;
            try {
                String key = secureStore.get("ai_key_" + provider.toUpperCase(Locale.ROOT));
                request = new AiClient().prepareRequest(
                        AiClient.Provider.fromLabel(provider), key, prompt.getText().toString());
            } catch (IllegalArgumentException | IllegalStateException exception) {
                showMessage(exception.getMessage());
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Send your question?")
                    .setMessage("Your prompt will be sent over HTTPS to " + provider
                            + " using your API key. The provider may process or retain it under its own terms, and API charges may apply. Continue?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Send", (dialog, which) -> {
                        prepare.setEnabled(false);
                        response.setText("Connecting securely…");
                        aiExecutor.execute(() -> {
                            try {
                                String answer = new AiClient().send(request);
                                mainHandler.post(() -> {
                                    if (!isFinishing() && !isDestroyed()) {
                                        response.setText(answer);
                                        prepare.setEnabled(true);
                                    }
                                });
                            } catch (Exception exception) {
                                String message = exception.getMessage();
                                if (message == null || message.trim().isEmpty()) {
                                    message = "Could not complete the request.";
                                }
                                final String error = message;
                                mainHandler.post(() -> {
                                    if (!isFinishing() && !isDestroyed()) {
                                        response.setText("Request failed: " + error);
                                        prepare.setEnabled(true);
                                    }
                                });
                            }
                        });
                    }).show();
        });
        addCardView(aiCard);
        addBody("OAuth is not initiated by this app. Provider account and OAuth support varies; see SETUP.md before adding an account flow.");
    }

    private void addJobCard(Job job) {
        addJobCardTo(content, job);
    }

    private void addJobCardTo(LinearLayout parent, Job job) {
        LinearLayout card = card();
        TextView sector = text(job.sector.toUpperCase(Locale.ROOT), 11, true, color(R.color.electric_blue));
        card.addView(sector);
        addGap(card, 5);
        card.addView(text(job.title, 18, true, color(R.color.ink)));
        addGap(card, 6);
        card.addView(text(job.summary, 14, false, color(R.color.muted)));
        addGap(card, 10);
        TextView pathway = text("Subjects & pathway  →", 14, true, color(R.color.electric_blue));
        card.addView(pathway);
        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(v -> showCareerDetail(job));
        card.setOnLongClickListener(v -> {
            showCareerDetail(job);
            return true;
        });
        parent.addView(card, match());
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) card.getLayoutParams();
        params.bottomMargin = dp(11);
        card.setLayoutParams(params);
    }

    private void showCareerDetail(Job job) {
        LinearLayout detail = column();
        detail.setPadding(dp(20), dp(8), dp(20), dp(4));
        detail.addView(text(job.sector.toUpperCase(Locale.ROOT), 12, true, color(R.color.electric_blue)));
        addGap(detail, 8);
        detail.addView(text(job.summary, 16, false, color(R.color.ink)));
        addGap(detail, 14);
        detail.addView(text("Helpful school subjects", 15, true, color(R.color.ink)));
        detail.addView(text(job.subjects, 14, false, color(R.color.muted)));
        addGap(detail, 12);
        detail.addView(text("A possible pathway", 15, true, color(R.color.ink)));
        detail.addView(text(job.pathway, 14, false, color(R.color.muted)));
        addGap(detail, 12);
        detail.addView(text("Indicative South African salary range: " + job.salary
                + "\nRanges are illustrative estimates, not a promise or current vacancy.", 13, false, color(R.color.muted)));
        new AlertDialog.Builder(this)
                .setTitle(job.title)
                .setView(detail)
                .setNegativeButton("Close", null)
                .setPositiveButton("Save to my learning history", (dialog, which) -> {
                    database.recordCourse(job.title + " pathway");
                    showMessage("Added to your learning history.");
                })
                .show();
    }

    private void addTitle(String value, int size) {
        TextView title = text(value, size, true, color(R.color.ink));
        LinearLayout.LayoutParams params = wrap();
        params.bottomMargin = dp(8);
        content.addView(title, params);
    }

    private void addBody(String value) {
        TextView body = text(value, 14, false, color(R.color.muted));
        LinearLayout.LayoutParams params = match();
        params.bottomMargin = dp(12);
        content.addView(body, params);
    }

    private void addCardView(LinearLayout card) {
        LinearLayout.LayoutParams params = match();
        params.topMargin = dp(10);
        params.bottomMargin = dp(4);
        content.addView(card, params);
    }

    private LinearLayout column() {
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    private LinearLayout row() {
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setGravity(Gravity.CENTER_VERTICAL);
        return view;
    }

    private LinearLayout card() {
        LinearLayout view = column();
        view.setPadding(dp(16), dp(15), dp(16), dp(15));
        view.setBackground(backgroundResource(R.color.surface, 16));
        return view;
    }

    private EditText input(String hint, boolean secret) {
        EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setTextSize(15);
        editText.setHint(hint);
        editText.setTextColor(color(R.color.ink));
        editText.setHintTextColor(color(R.color.muted));
        editText.setPadding(dp(14), dp(11), dp(14), dp(11));
        editText.setBackground(backgroundResource(R.color.surface, 12));
        if (secret) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                    | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (hint.toLowerCase(Locale.ROOT).contains("email")) {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                    | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        return editText;
    }

    private Button button(String label, int backgroundColor) {
        Button button = new Button(this);
        button.setText(label);
        button.setTextColor(Color.WHITE);
        button.setTextSize(15);
        button.setAllCaps(false);
        button.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        button.setBackground(background(backgroundColor, 14));
        return button;
    }

    private Button smallButton(String label, int backgroundColor) {
        Button button = button(label, backgroundColor);
        button.setTextSize(13);
        return button;
    }

    private TextView text(String value, int size, boolean bold, int textColor) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(textColor);
        if (bold) {
            view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return view;
    }

    private GradientDrawable backgroundResource(int colorResource, int radius) {
        return background(color(colorResource), radius);
    }

    private GradientDrawable background(int fillColor, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setCornerRadius(dp(radius));
        return drawable;
    }

    private int color(int resource) {
        if (darkMode && resource == R.color.background) {
            return Color.rgb(16, 23, 41);
        }
        if (darkMode && resource == R.color.surface) {
            return Color.rgb(30, 39, 61);
        }
        if (darkMode && resource == R.color.ink) {
            return Color.rgb(245, 247, 255);
        }
        if (darkMode && resource == R.color.muted) {
            return Color.rgb(180, 190, 211);
        }
        return getResources().getColor(resource);
    }

    private void openExternal(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException exception) {
            showMessage("No browser app is available to open this profile.");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addGap(LinearLayout parent, int height) {
        View gap = new View(this);
        parent.addView(gap, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private final class SwitchRow extends LinearLayout {
        private final android.widget.Switch toggle;

        SwitchRow(Activity activity, String label, boolean checked) {
            super(activity);
            setGravity(Gravity.CENTER_VERTICAL);
            setPadding(0, dp(8), 0, dp(4));
            TextView title = text(label, 15, false, color(R.color.ink));
            addView(title, new LinearLayout.LayoutParams(0, -2, 1));
            toggle = new android.widget.Switch(activity);
            toggle.setChecked(checked);
            addView(toggle);
        }

        void setOnCheckedChangeListener(android.widget.CompoundButton.OnCheckedChangeListener listener) {
            toggle.setOnCheckedChangeListener(listener);
        }
    }
}
