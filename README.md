# Career Path

Career Path is a lightweight native Android career-exploration starter app for South African learners. It uses Java and Android framework APIs, targets Android 5.0 (API 21) and later, and has no third-party runtime dependencies.

## What is included

- Local, client-side sign-in validation and an encrypted on-device session. This preview does not create an online account or authenticate a real identity.
- An offline SQLite catalog of 20 career starters, searchable by title, sector, description, school subjects and pathway.
- Career overviews, example school subjects, possible study routes and clearly marked indicative salary ranges.
- A followable sample expert feed with external profile links, a learning-history list and a subscription preview with no payment flow.
- Dark/light appearance, localized core navigation/login/settings strings for English, isiZulu, isiXhosa, Afrikaans, Sepedi and Setswana, plus broader-language placeholders. Career catalog descriptions remain in English.
- An opt-in AI career helper for Gemini, DeepSeek and OpenAI. The user confirms before each sanitized, HTTPS-only request; OAuth is not implemented.

## Build and run

Open this directory in Android Studio and sync the Gradle project, then run the `app` configuration on an emulator or device. With Java 17, Android SDK Platform 35 and Gradle 8.9 or later available, build from the repository root:

```text
gradle :app:assembleDebug
```

The app's minimum SDK is API 21. The Android Gradle Plugin and Android SDK are build-time requirements; no application libraries are downloaded as runtime dependencies. See [SETUP.md](SETUP.md) for SDK requirements, provider limitations and security notes.

## Offline and privacy notes

Career entries, saved learning history, appearance/language preferences and followed experts are stored locally. Profile email and any optional AI API key are encrypted using Android Keystore-backed encryption (AES-GCM on API 23+; a Keystore RSA-wrapped AES key on API 21–22). Passwords are validated but never retained. The app requests `INTERNET` only for the AI helper; it sends a prompt only after an explicit confirmation. Browser profile links are opened through Android's external `ACTION_VIEW` intent.

Career, salary and sector notes are illustrative educational content, not live labor-market statistics, vacancy data, financial advice or a guarantee of employment. Sample expert posts and identities are fictional and are not endorsements.
