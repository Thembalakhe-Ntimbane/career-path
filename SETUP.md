# Setup, offline use and AI provider notes

## Static web app

There are no build dependencies. Run a static development server such as `python -m http.server 8000` and visit `http://localhost:8000`, or deploy the files to an HTTPS static host. Installable PWA features, service workers and Web Crypto require HTTPS or the browser's trusted `localhost` context. After the shell is cached, career browsing and saved courses work offline. The service worker caches only first-party shell assets; AI API calls are never cached. Bump the `CACHE` version in `sw.js` whenever publishing changed shell assets so returning offline installs receive the new files.

## Privacy and saved data

The local demo sign-in is not server authentication. It validates input in the browser, clears the password, and retains the email only in `sessionStorage` for the open tab. Career data is bundled with the app; courses, follows, theme, language and an optional encrypted AI credential are stored locally in browser storage. Clearing site data removes them.

An AI key is encrypted with Web Crypto AES-256-GCM. The encryption key is derived using PBKDF2-SHA-256 from a learner-chosen passphrase (minimum 12 characters) and a random salt. Neither the passphrase nor the derived encryption key is stored; the learner must unlock the key again after reload. The unlocked API key exists in page memory only. Browser encryption protects stored data against casual inspection, **not** against malicious same-origin JavaScript, browser extensions, compromised devices or a user who forgets the passphrase. Use a unique passphrase and do not store a shared or production billing key in a student browser.

## Optional AI and provider authentication

No provider request is made until the learner has saved/unlocked their own key, enters a question and confirms the send prompt. The text is limited to 1,200 characters. Requests use HTTPS, default browser certificate verification, fixed provider hosts, no redirects, bounded timeouts and do not include browser credentials. A provider receives each submitted question and handles it under its own policies. Avoid sensitive personal, school or health data.

This app supports **API keys, not OAuth**. No provider OAuth sign-in or password collection is implemented. Gemini API keys, DeepSeek API keys and OpenAI API keys have provider-specific eligibility, billing and terms; a Google account or ChatGPT subscription alone is not an API key. Provider endpoints/models and browser cross-origin (CORS) rules can change. Direct browser API calls may be refused by a provider's CORS policy; the UI reports that without retrying elsewhere. For production student use, prefer a trusted server-side broker that applies consent, authentication, quotas and privacy protections. Never embed a shared provider secret in static app files.

## Localization

Core sign-in, navigation and offline labels include English, isiZulu, isiXhosa, Afrikaans, Sepedi and Setswana. Career summaries and learning pathways are currently in English; additional language translations can be added to the `I18N` dictionaries in `app.js`.
