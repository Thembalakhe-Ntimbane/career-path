# Career Path

Career Path is a lightweight, mobile-first web app for exploring South African career options. It uses plain HTML, CSS and JavaScript with no application framework or external runtime dependencies. The installable PWA shell and 20-career guide are available offline after the first visit.

## Run locally

Serve this directory over `localhost` (service workers and Web Crypto require a secure context):

```powershell
python -m http.server 8000
```

Open `http://localhost:8000`. Or deploy the static files to a host with HTTPS. No build step or package install is required. See [SETUP.md](SETUP.md) for security and AI notes.

## Features

- Private demo sign-in with immediate email/password validation; the password is cleared and never stored. The email lives only in the current browser tab. This is not a real account system.
- A searchable, offline career catalogue with 20 illustrative jobs, sectors, school subject examples and possible study routes.
- Responsive 2×2 career-sector cards, detail dialogs, a sample expert feed, locally saved follows, and LinkedIn/GitHub home-page links.
- Bite-sized crash courses generated locally and saved on this browser, with active/complete history.
- Dark and light themes, English/isiZulu/isiXhosa/Afrikaans/Sepedi/Setswana core navigation and sign-in labels.
- Optional, explicitly confirmed AI questions for Gemini, DeepSeek or OpenAI using a user-provided API key. API keys are not OAuth.

All careers and pathways are starting points, not admissions requirements or live labour-market statistics. The illustrative expert posts are sample content, not verified endorsements.
