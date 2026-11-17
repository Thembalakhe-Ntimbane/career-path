"use strict";

const CAREERS = [
  { title: "Software Engineer", category: "Technology", icon: "⌘", summary: "Designs and builds apps, websites and digital services.", subjects: "Mathematics, Physical Sciences, Information Technology", pathway: "Study computer science, software development or a related qualification. Build projects and look for internships." },
  { title: "Data Scientist", category: "Technology", icon: "▦", summary: "Uses data, statistics and computing to answer important questions.", subjects: "Mathematics, Physical Sciences, Information Technology", pathway: "Study statistics, data science, mathematics or computer science; practise with real datasets." },
  { title: "Cybersecurity Analyst", category: "Technology", icon: "◈", summary: "Helps protect organisations and people from digital threats.", subjects: "Mathematics, Information Technology, Physical Sciences", pathway: "Study IT or computer science; develop networking and security skills through practical labs." },
  { title: "AI and Machine Learning Specialist", category: "Technology", icon: "✦", summary: "Builds systems that learn from examples and data.", subjects: "Mathematics, Physical Sciences, Information Technology", pathway: "Study computer science, mathematics or engineering; learn to build and use AI responsibly." },
  { title: "Cloud Engineer", category: "Technology", icon: "☁", summary: "Builds and maintains the online computing services organisations rely on.", subjects: "Mathematics, Information Technology, Physical Sciences", pathway: "Study IT or computer science; practise networking, systems and cloud platforms." },
  { title: "Renewable Energy Technician", category: "Trades", icon: "☀", summary: "Installs and maintains solar, wind and battery systems.", subjects: "Mathematics, Physical Sciences, Electrical Technology", pathway: "Explore an electrical TVET qualification or apprenticeship and recognised solar PV training." },
  { title: "Electrical Engineer", category: "Trades", icon: "ϟ", summary: "Plans and improves electrical systems for industry and energy.", subjects: "Mathematics, Physical Sciences, Engineering Graphics and Design", pathway: "Explore accredited engineering degree or diploma routes and supervised workplace experience." },
  { title: "Civil Engineer", category: "Trades", icon: "△", summary: "Designs roads, water systems, bridges and other public infrastructure.", subjects: "Mathematics, Physical Sciences, Engineering Graphics and Design", pathway: "Explore an accredited civil engineering degree or diploma and supervised practical experience." },
  { title: "Artisan Electrician", category: "Trades", icon: "⚡", summary: "Wires, tests and repairs electrical installations safely.", subjects: "Mathematics, Physical Sciences, Electrical Technology", pathway: "Consider TVET training or an apprenticeship, followed by a trade test and required registration." },
  { title: "Agricultural Technologist", category: "Agriculture", icon: "♧", summary: "Uses technology to grow food efficiently and sustainably.", subjects: "Mathematics, Life Sciences, Agricultural Sciences", pathway: "Explore agriculture or engineering studies; learn irrigation, sensors and farm data." },
  { title: "Agricultural Scientist", category: "Agriculture", icon: "❀", summary: "Improves crops, soil and farming through scientific research.", subjects: "Mathematics, Life Sciences, Agricultural Sciences", pathway: "Study agricultural science or a related discipline; specialise through research or field work." },
  { title: "Environmental Scientist", category: "Agriculture", icon: "🌱", summary: "Studies ecosystems and helps reduce environmental harm.", subjects: "Mathematics, Life Sciences, Geography, Physical Sciences", pathway: "Explore environmental science study, field research and specialist experience." },
  { title: "Fintech Specialist", category: "Finance", icon: "R", summary: "Creates digital tools that make payments and financial services easier.", subjects: "Mathematics, Accounting, Information Technology", pathway: "Study finance, business or software; learn about digital payments and data protection." },
  { title: "Financial Analyst", category: "Finance", icon: "↗", summary: "Helps people and organisations make informed money decisions.", subjects: "Mathematics, Accounting, Economics", pathway: "Explore finance, economics or commerce studies and relevant industry learning." },
  { title: "Accountant", category: "Finance", icon: "Σ", summary: "Tracks financial information and supports sound business decisions.", subjects: "Mathematics, Accounting, Economics", pathway: "Study accounting; investigate professional qualification pathways with relevant bodies." },
  { title: "Nurse", category: "Health", icon: "+", summary: "Provides essential care and supports patients, families and communities.", subjects: "Mathematics, Life Sciences, Physical Sciences", pathway: "Explore an accredited nursing programme and professional registration requirements." },
  { title: "Medical Doctor", category: "Health", icon: "✚", summary: "Diagnoses illness, treats patients and promotes community health.", subjects: "Mathematics, Physical Sciences, Life Sciences", pathway: "Explore medical school requirements, supervised internship and professional registration." },
  { title: "Pharmacist", category: "Health", icon: "⚕", summary: "Helps ensure medicines are safe and used correctly.", subjects: "Mathematics, Physical Sciences, Life Sciences", pathway: "Explore accredited pharmacy degrees, practical training and professional registration." },
  { title: "UX/UI Designer", category: "Arts", icon: "◉", summary: "Makes digital products easier and more enjoyable for people to use.", subjects: "Visual Arts, Design, Information Technology", pathway: "Study design or build a portfolio; practise accessible design and user research." },
  { title: "Digital Content Creator", category: "Arts", icon: "▶", summary: "Creates useful, engaging stories and media for online audiences.", subjects: "English, Visual Arts, Dramatic Arts", pathway: "Build a portfolio; learn editing, storytelling and digital safety." }
];

const EXPERTS = [
  { name: "Lerato Mokoena", role: "Renewable energy engineer", post: "I started with small science projects. Curiosity, practical problem-solving and asking good questions can take you far.", icon: "☀", links: [["LinkedIn", "https://www.linkedin.com/"], ["GitHub", "https://github.com/"]] },
  { name: "Thabo Ndlovu", role: "Software developer and mentor", post: "Try making something useful for your school or community. Small projects are a brilliant way to begin learning to code.", icon: "⌘", links: [["GitHub", "https://github.com/"], ["LinkedIn", "https://www.linkedin.com/"]] },
  { name: "Ayesha Khan", role: "Data scientist", post: "A spreadsheet is a great first data tool. Ask a question, explore the numbers and share what you find.", icon: "▦", links: [["LinkedIn", "https://www.linkedin.com/"]] },
  { name: "Nomsa Dlamini", role: "Agricultural technologist", post: "Technology and food growing make a powerful team. Learn from local growers and keep experimenting.", icon: "♧", links: [["LinkedIn", "https://www.linkedin.com/"]] }
];

const I18N = {
  en: { home: "Home", explore: "Explore", experts: "Connect", courses: "Courses", settings: "Settings", welcome: "Big dreams start with one small step.", loginNote: "A private, on-this-device demo profile. No online account needed.", email: "Email address", password: "Password (8+ characters)", continue: "Start exploring", passwordNote: "Your password is only checked here and is never saved.", discover: "DISCOVER YOUR FUTURE", offlineReady: "Your career guide works offline", littleSteps: "LITTLE STEPS, BIG FUTURES" },
  zu: { home: "Ekhaya", explore: "Hlola", experts: "Xhumana", courses: "Izifundo", settings: "Izilungiselelo", welcome: "Amaphupho amakhulu aqala ngesinyathelo esincane.", loginNote: "Iphrofayela eyimfihlo ehlala kulesi sidivayisi.", email: "Ikheli le-imeyili", password: "Iphasiwedi (izinhlamvu eziyi-8+)", continue: "Qala ukuhlola", passwordNote: "Iphasiwedi ayigcinwa.", discover: "THOLA IKUSASA LAKHO", offlineReady: "Umhlahlandlela uyasebenza ngaphandle kwe-inthanethi", littleSteps: "IZINYATHELO EZINCANE, IKUSASA ELIKHULU" },
  xh: { home: "Ekhaya", explore: "Khangela", experts: "Nxibelelana", courses: "Izifundo", settings: "Iisetingi", welcome: "Amaphupha amakhulu aqala ngenyathelo elincinci.", loginNote: "Iprofayile yakho ihlala kwesi sixhobo.", email: "Idilesi ye-imeyile", password: "Igama lokugqitha (oonobumba abasi-8+)", continue: "Qalisa ukukhangela", passwordNote: "Igama lokugqitha aligcinwa.", discover: "FUMANA IKAMVA LAKHO", offlineReady: "Isikhokelo sisebenza ngaphandle kwe-intanethi", littleSteps: "AMANYATHELO AMANCINCI, IKAMVA ELIKHULU" },
  af: { home: "Tuis", explore: "Verken", experts: "Verbind", courses: "Kursusse", settings: "Instellings", welcome: "Groot drome begin met een klein tree.", loginNote: "Jou privaat profiel bly op hierdie toestel.", email: "E-posadres", password: "Wagwoord (8+ karakters)", continue: "Begin verken", passwordNote: "Jou wagwoord word nooit gestoor nie.", discover: "ONTDEK JOU TOEKOMS", offlineReady: "Jou loopbaangids werk vanlyn", littleSteps: "KLEIN TREE, GROOT TOEKOMSTE" },
  nso: { home: "Gae", explore: "Nyakišiša", experts: "Kgokagano", courses: "Dikhoso", settings: "Dipeakanyo", welcome: "Ditoro tše dikgolo di thoma ka kgato e nnyane.", loginNote: "Profaele ya gago e dula sedirišweng se.", email: "Aterese ya imeile", password: "Phasewete (ditlhaka tše 8+)", continue: "Thoma go nyakišiša", passwordNote: "Phasewete ya gago ga e bolokwe.", discover: "HWELETŠA BOKAMOSO BJA GAGO", offlineReady: "Tlhahlo ya gago e šoma ntle le inthanete", littleSteps: "DIKGATO TŠE DINNYANE, BOKAMOSO BJO BOGOLO" },
  tn: { home: "Gae", explore: "Batla", experts: "Golagana", courses: "Dithuto", settings: "Dithulaganyo", welcome: "Ditoro tse dikgolo di simolola ka kgato e nnye.", loginNote: "Porofaele ya gago e nna mo sedirisiweng seno.", email: "Aterese ya imeile", password: "Phasewete (ditlhaka di le 8+)", continue: "Simolola go batla", passwordNote: "Phasewete ya gago ga e bolokwe.", discover: "LEMOGA BOKAMOSO JWA GAGO", offlineReady: "Kaelo ya tiro e dira o se mo inthaneteng", littleSteps: "DIKGATO TSE DINNYE, BOKAMOSO JO BOGOLO" }
};

const NAV = [
  ["home", "⌂"], ["explore", "⌕"], ["experts", "✦"], ["courses", "▤"], ["settings", "⚙"]
];
const SECTORS = [
  ["Technology", "Build the digital future", "✦"],
  ["Agriculture", "Grow food and care for the land", "♧"],
  ["Health", "Care for people and communities", "+"],
  ["Finance", "Turn ideas into lasting impact", "↗"]
];
const STORAGE = { courses: "career-path-courses-v1", follows: "career-path-follows-v1", theme: "career-path-theme-v1", language: "career-path-language-v1", credential: "career-path-ai-cipher-v1" };
const state = {
  page: "home", email: "", query: "", category: "All categories",
  follows: Array.isArray(readJSON(STORAGE.follows, [])) ? readJSON(STORAGE.follows, []) : [],
  courses: Array.isArray(readJSON(STORAGE.courses, [])) ? readJSON(STORAGE.courses, []) : [],
  language: localStorage.getItem(STORAGE.language) || "en",
  credential: null, toastTimer: 0, searchTimer: 0
};
let unlockedAI = null;
let careerDialogItem = null;

const $ = (selector, root = document) => root.querySelector(selector);
function readJSON(key, fallback) {
  try { const value = JSON.parse(localStorage.getItem(key) || "null"); return value == null ? fallback : value; }
  catch { return fallback; }
}
function saveJSON(key, value) {
  try { localStorage.setItem(key, JSON.stringify(value)); return true; }
  catch { toast("This browser could not save local app data. Check available storage."); return false; }
}
function t(key) { return (I18N[state.language] || I18N.en)[key] || I18N.en[key] || key; }
function node(tag, className, value) {
  const item = document.createElement(tag);
  if (className) item.className = className;
  if (value !== undefined) item.textContent = value;
  return item;
}
function button(label, className, action) {
  const item = node("button", `button ${className || ""}`, label);
  item.type = "button";
  if (action) item.addEventListener("click", action);
  return item;
}
function sectionTitle(title, actionLabel, action) {
  const row = node("div", "section-heading");
  row.append(node("h2", "", title));
  if (actionLabel) {
    if (action) {
      const link = node("button", "", actionLabel);
      link.type = "button";
      link.addEventListener("click", action);
      row.append(link);
    } else {
      row.append(node("span", "sample-label", actionLabel));
    }
  }
  return row;
}
function toast(message) {
  const box = $("#toast");
  box.textContent = message;
  box.classList.add("show");
  window.clearTimeout(state.toastTimer);
  state.toastTimer = window.setTimeout(() => box.classList.remove("show"), 2800);
}
function greetingName() { return state.email.split("@")[0].replace(/[._-]/g, " ") || "there"; }
function setTheme() {
  document.body.classList.toggle("dark", localStorage.getItem(STORAGE.theme) === "dark");
}
function applyLanguage() {
  document.documentElement.lang = state.language;
  document.querySelectorAll("[data-i18n]").forEach((item) => {
    const key = item.dataset.i18n;
    if (t(key)) item.textContent = t(key);
  });
}
function init() {
  applyLanguage();
  setTheme();
  $("#login-form").addEventListener("submit", (event) => {
    event.preventDefault();
    const email = $("#login-email").value.trim();
    const password = $("#login-password").value;
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      $("#login-error").textContent = "Enter a valid email address.";
      $("#login-email").focus();
      return;
    }
    if (password.length < 8) {
      $("#login-error").textContent = "Use at least 8 characters.";
      $("#login-password").focus();
      return;
    }
    $("#login-password").value = "";
    $("#login-error").textContent = "";
    sessionStorage.setItem("career-path-demo-session", email);
    startApp(email);
  });
  $("#mobile-brand").addEventListener("click", () => navigate("home"));
  $("#profile-button").addEventListener("click", () => navigate("settings"));
  $("#top-profile").addEventListener("click", () => navigate("settings"));
  const email = sessionStorage.getItem("career-path-demo-session");
  if (email) startApp(email);
  else showLogin();
  if ("serviceWorker" in navigator && location.protocol !== "file:") {
    window.addEventListener("load", () => navigator.serviceWorker.register("./sw.js").catch(() => {}), { once: true });
  }
}
function showLogin() {
  $("#app-shell").hidden = true;
  $("#login-screen").hidden = false;
  window.setTimeout(() => $("#login-email").focus(), 0);
}
function startApp(email) {
  state.email = email;
  $("#login-screen").hidden = true;
  $("#app-shell").hidden = false;
  $("#profile-button").textContent = `☺  ${email}`;
  $("#top-profile").textContent = email.slice(0, 1).toUpperCase();
  $("#greeting").textContent = `Kia ora, ${greetingName()}!`;
  renderNav();
  render();
}
function renderNav() {
  const makeNav = (container) => {
    container.replaceChildren();
    NAV.forEach(([key, icon]) => {
      const item = node("button", `nav-link ${state.page === key ? "active" : ""}`);
      item.type = "button";
      item.setAttribute("aria-current", state.page === key ? "page" : "false");
      item.append(node("span", "nav-icon", icon), node("span", "", t(key)));
      item.addEventListener("click", () => navigate(key));
      container.append(item);
    });
  };
  makeNav($("#side-nav"));
  makeNav($("#mobile-nav"));
}
function navigate(page) {
  state.page = page;
  renderNav();
  render();
  $("#view").focus({ preventScroll: true });
  window.scrollTo({ top: 0, behavior: "smooth" });
}
function heading(title, subtitle) {
  const box = node("div", "page-header");
  box.append(node("h1", "page-heading", title), node("p", "page-subtitle", subtitle));
  return box;
}
function render() {
  const view = $("#view");
  view.replaceChildren();
  if (state.page === "home") renderHome(view);
  else if (state.page === "explore") renderExplore(view);
  else if (state.page === "experts") renderExperts(view);
  else if (state.page === "courses") renderCourses(view);
  else renderSettings(view);
}
function renderHome(view) {
  const hero = node("section", "hero");
  hero.append(node("span", "eyebrow", "YOUR FUTURE IS FULL OF POSSIBILITIES"));
  hero.append(node("h2", "", "Find a path that feels like yours."));
  hero.append(node("p", "", "Discover real-world careers, school subjects and possible study routes — all ready on your device."));
  hero.append(button("Explore careers", "button-primary", () => navigate("explore")));
  view.append(hero);
  view.append(sectionTitle("Explore four directions", "See all careers", () => navigate("explore")));
  const grid = node("div", "sector-grid");
  SECTORS.forEach(([name, description, icon]) => {
    const card = node("button", "sector-card");
    card.type = "button";
    const copy = node("span");
    copy.append(node("strong", "", name), node("small", "", description));
    card.append(copy, node("span", "sector-symbol", icon));
    card.addEventListener("click", () => {
      state.category = name;
      navigate("explore");
    });
    grid.append(card);
  });
  view.append(grid, sectionTitle("Trending careers in South Africa", "Explore", () => navigate("explore")));
  const cards = node("div", "career-grid");
  CAREERS.slice(0, 4).forEach((career) => cards.append(makeCareerCard(career)));
  view.append(cards);
  view.append(sectionTitle("From the community", "Meet the experts", () => navigate("experts")));
  view.append(makeFeedCard(EXPERTS[0]));
}
function categories() { return ["All categories", ...new Set(CAREERS.map((item) => item.category))]; }
function renderExplore(view) {
  view.append(heading("Explore careers", "Search this offline guide by job, sector, school subject or pathway."));
  const controls = node("div", "search-row");
  const search = node("input", "field");
  search.type = "search";
  search.placeholder = "Try “science”, “design” or “solar”…";
  search.setAttribute("aria-label", "Search careers, subjects and pathways");
  search.value = state.query;
  const select = node("select", "select");
  select.setAttribute("aria-label", "Filter by career category");
  categories().forEach((category) => {
    const option = node("option", "", category);
    option.value = category;
    option.selected = category === state.category;
    select.append(option);
  });
  controls.append(search, select);
  const count = node("p", "result-count");
  const results = node("div", "career-grid");
  const update = () => {
    state.query = search.value;
    state.category = select.value;
    const query = state.query.trim().toLocaleLowerCase();
    const matches = CAREERS.filter((career) => (state.category === "All categories" || career.category === state.category) &&
      [career.title, career.category, career.summary, career.subjects, career.pathway].join(" ").toLocaleLowerCase().includes(query));
    count.textContent = `${matches.length} ${matches.length === 1 ? "career" : "careers"} in your offline guide`;
    results.replaceChildren();
    if (!matches.length) results.append(node("p", "empty-state", "No matches yet. Try another word or category."));
    matches.forEach((career) => results.append(makeCareerCard(career)));
  };
  search.addEventListener("input", update);
  select.addEventListener("change", update);
  view.append(controls, count, results);
  update();
}
function makeCareerCard(career) {
  const card = node("article", "career-card");
  card.tabIndex = 0;
  card.setAttribute("role", "button");
  card.setAttribute("aria-label", `${career.title}, ${career.category}. Open career details`);
  card.append(node("span", "tag", `${career.icon}  ${career.category}`), node("h3", "", career.title), node("p", "", career.summary));
  card.append(node("p", "subjects-preview", `School subjects: ${career.subjects}`));
  card.addEventListener("click", () => openCareer(career));
  card.addEventListener("keydown", (event) => {
    if (event.key === "Enter" || event.key === " ") { event.preventDefault(); openCareer(career); }
  });
  return card;
}
function openCareer(career) {
  careerDialogItem = career;
  const detail = $("#career-detail");
  detail.replaceChildren();
  detail.append(node("span", "tag", `${career.icon}  ${career.category}`), node("h2", "detail-title", career.title));
  const addSection = (title, text) => {
    const section = node("section", "detail-section");
    section.append(node("h3", "", title), node("p", "", text));
    detail.append(section);
  };
  addSection("A little about this career", career.summary);
  addSection("Helpful school subjects", career.subjects);
  addSection("Possible pathway", career.pathway);
  addSection("A reminder", "Requirements vary by institution and professional body. Check current admission and registration guidance before making study choices.");
  const actions = node("div", "detail-actions");
  actions.append(button("Create a crash course", "button-blue", () => createCourse(career)), button("Close", "button-soft", () => $("#career-dialog").close()));
  detail.append(actions);
  $("#career-dialog").showModal();
}
function renderExperts(view) {
  view.append(heading("Connect & get inspired", "Friendly career snapshots and ideas from a sample community feed."));
  const note = node("div", "callout", "Sample feed for learning and inspiration — these are illustrative profiles, not verified endorsements or live social accounts. Follow choices stay on this device.");
  const membership = node("div", "callout subscription-callout");
  membership.append(node("strong", "", "Career Path Plus"), node("span", "", " · Expert sessions and extra learning tools are planned. This preview does not collect payments."));
  view.append(note, membership, sectionTitle("A community of possibilities", `${state.follows.length} following`, null));
  const feed = node("div", "feed-list");
  EXPERTS.forEach((expert) => feed.append(makeFeedCard(expert)));
  view.append(feed);
}
function makeFeedCard(expert) {
  const card = node("article", "feed-card");
  const top = node("div", "feed-top");
  top.append(node("span", "expert-avatar", expert.icon));
  const meta = node("span", "feed-meta");
  meta.append(node("strong", "", expert.name), node("small", "", `${expert.role} · sample profile`));
  top.append(meta);
  const followed = state.follows.includes(expert.name);
  const follow = button(followed ? "Following ✓" : "Follow", followed ? "button-soft" : "button-blue", () => {
    state.follows = followed ? state.follows.filter((name) => name !== expert.name) : [...state.follows, expert.name];
    saveJSON(STORAGE.follows, state.follows);
    render();
    toast(followed ? `Unfollowed ${expert.name}.` : `You’re following ${expert.name}.`);
  });
  follow.setAttribute("aria-pressed", String(followed));
  top.append(follow);
  card.append(top, node("p", "feed-copy", `“${expert.post}”`));
  const actions = node("div", "feed-actions");
  expert.links.forEach(([label, url]) => {
    const link = node("a", "", label === "GitHub" ? "GitHub ↗" : "LinkedIn ↗");
    link.href = url;
    link.target = "_blank";
    link.rel = "noopener noreferrer";
    link.title = `Open ${label} homepage. Sample profile links are not individual accounts.`;
    actions.append(link);
  });
  actions.append(node("span", "sample-label", "Illustrative content"));
  card.append(actions);
  return card;
}
function makeCourse(career) {
  return {
    id: `${Date.now()}-${Math.random().toString(16).slice(2, 8)}`,
    title: career.title,
    category: career.category,
    createdAt: new Date().toISOString(),
    completed: false,
    steps: [
      `Explore: ${career.summary}`,
      `School subjects to investigate: ${career.subjects}. Ask a teacher which subjects are available to you.`,
      `Possible next step: ${career.pathway}`,
      "Reflect: write down one question for someone who works or studies in this field."
    ]
  };
}
function createCourse(career) {
  const course = makeCourse(career);
  state.courses.unshift(course);
  if (saveJSON(STORAGE.courses, state.courses)) {
    $("#career-dialog").close();
    toast("Your offline crash course is ready.");
    navigate("courses");
  }
}
function renderCourses(view) {
  view.append(heading("Your crash courses", "Short career overviews are created on your device and saved here."));
  const courses = node("div", "course-list");
  if (!state.courses.length) {
    courses.append(node("p", "empty-state", "Your course list is ready. Explore a career and choose “Create a crash course” to start."));
  }
  state.courses.forEach((course) => {
    const card = node("article", "course-card");
    const title = node("div");
    title.append(node("span", "tag", course.completed ? "Finished" : "Ready to explore"), node("h3", "", course.title), node("p", "", course.category));
    const date = node("span", "course-date", new Intl.DateTimeFormat(undefined, { dateStyle: "medium" }).format(new Date(course.createdAt)));
    const steps = node("ol", "course-steps");
    (course.steps || []).forEach((step) => steps.append(node("li", "", step)));
    const action = button(course.completed ? "Mark active" : "Complete", course.completed ? "button-soft" : "button-coral", () => {
      course.completed = !course.completed;
      saveJSON(STORAGE.courses, state.courses);
      render();
      toast(course.completed ? "Crash course complete. Nice work!" : "Crash course moved back to active.");
    });
    card.append(title, date, steps, action);
    courses.append(card);
  });
  view.append(courses);
}
function renderSettings(view) {
  view.append(heading("Settings", "Set up Career Path to work the way you like."));
  const appearance = node("section", "setting-card");
  appearance.append(node("h2", "", "Appearance"));
  const theme = node("label", "inline-control");
  const toggle = node("input", "switch");
  toggle.type = "checkbox";
  toggle.checked = document.body.classList.contains("dark");
  theme.append(toggle, node("span", "", "Dark mode"));
  toggle.addEventListener("change", () => {
    document.body.classList.toggle("dark", toggle.checked);
    localStorage.setItem(STORAGE.theme, toggle.checked ? "dark" : "light");
  });
  appearance.append(theme);
  view.append(appearance);

  const languageCard = node("section", "setting-card");
  languageCard.append(node("h2", "", "Language"));
  languageCard.append(node("p", "", "Choose the app’s main navigation and sign-in language. Career profiles are currently in English."));
  const language = node("select", "select");
  language.setAttribute("aria-label", "Choose language");
  [["en", "English"], ["zu", "isiZulu"], ["xh", "isiXhosa"], ["af", "Afrikaans"], ["nso", "Sepedi"], ["tn", "Setswana"]].forEach(([code, label]) => {
    const option = node("option", "", label);
    option.value = code;
    option.selected = code === state.language;
    language.append(option);
  });
  const futureLanguages = node("option", "", "More languages coming soon");
  futureLanguages.disabled = true;
  language.append(futureLanguages);
  language.addEventListener("change", () => {
    state.language = language.value;
    localStorage.setItem(STORAGE.language, language.value);
    applyLanguage();
    renderNav();
    render();
  });
  languageCard.append(language);
  view.append(languageCard);

  view.append(makeAISettings());
  const account = node("section", "setting-card");
  account.append(node("h2", "", "Local demo profile"), node("p", "", `${state.email} · Stored only for this browser tab.`));
  account.append(button("Sign out", "button-soft", () => {
    sessionStorage.removeItem("career-path-demo-session");
    state.email = "";
    unlockedAI = null;
    showLogin();
  }));
  view.append(account);
}
function makeAISettings() {
  const card = node("section", "setting-card");
  card.append(node("h2", "", "Optional Career AI"));
  card.append(node("p", "", "AI is off until you save your own provider key and deliberately send a question. Your encrypted key is protected with a passphrase you choose; the passphrase is never stored. API keys are not OAuth."));
  const stored = getCredentialRecord();
  const provider = node("select", "select");
  provider.setAttribute("aria-label", "AI provider");
  ["Gemini", "DeepSeek", "OpenAI"].forEach((item) => {
    const option = node("option", "", item);
    option.selected = stored && stored.provider === item;
    provider.append(option);
  });
  const key = node("input", "field");
  key.type = "password";
  key.autocomplete = "new-password";
  key.placeholder = "Your provider API key";
  key.setAttribute("aria-label", "Provider API key");
  const passphrase = node("input", "field");
  passphrase.type = "password";
  passphrase.autocomplete = "new-password";
  passphrase.minLength = 12;
  passphrase.placeholder = "Local encryption passphrase (12+ characters)";
  passphrase.setAttribute("aria-label", "Passphrase to encrypt or unlock the key");
  const status = node("p", "status", stored ? (unlockedAI ? `Key unlocked for ${unlockedAI.provider}. It will be cleared when you close the page.` : "An encrypted key is stored on this device. Enter your passphrase to unlock it.") : "No AI key saved.");
  status.setAttribute("aria-live", "polite");
  const keyLabel = node("label", "setting-field", "Provider");
  keyLabel.append(provider);
  const secretLabel = node("label", "setting-field", stored ? "Replace provider key (leave empty to unlock the saved key)" : "Provider API key");
  secretLabel.append(key);
  const passLabel = node("label", "setting-field", stored ? "Encryption / unlock passphrase" : "Create an encryption passphrase");
  passLabel.append(passphrase);
  const actions = node("div", "setting-actions");
  const save = button("Encrypt and save key", "button-blue");
  const unlock = button("Unlock saved key", "button-soft");
  const ask = button("Ask Career AI", "button-coral");
  const forget = button("Forget encrypted key", "button-soft");
  save.addEventListener("click", async () => {
    status.className = "status";
    if (key.value.length < 12) { status.textContent = "Enter a provider key (at least 12 characters)."; status.classList.add("error"); return; }
    if (passphrase.value.length < 12) { status.textContent = "Choose a passphrase of at least 12 characters."; status.classList.add("error"); return; }
    try {
      const providerName = provider.value;
      const encrypted = await encryptCredential({ provider: providerName, apiKey: key.value }, passphrase.value);
      if (!saveJSON(STORAGE.credential, encrypted)) return;
      unlockedAI = { provider: providerName, apiKey: key.value };
      key.value = "";
      passphrase.value = "";
      status.textContent = `Encrypted key saved and unlocked for ${providerName}. No provider was contacted.`;
      status.classList.add("success");
      render();
    } catch (error) { status.textContent = `Could not encrypt this key: ${error.message}`; status.classList.add("error"); }
  });
  unlock.addEventListener("click", async () => {
    status.className = "status";
    if (passphrase.value.length < 12) { status.textContent = "Enter your encryption passphrase."; status.classList.add("error"); return; }
    try {
      const record = getCredentialRecord();
      if (!record) throw new Error("No encrypted key is saved.");
      unlockedAI = await decryptCredential(record, passphrase.value);
      passphrase.value = "";
      status.textContent = `Key unlocked for ${unlockedAI.provider}. It stays in memory only for this page session.`;
      status.classList.add("success");
      render();
    } catch (error) { unlockedAI = null; status.textContent = `Could not unlock key: ${error.message}`; status.classList.add("error"); }
  });
  ask.addEventListener("click", () => showAIQuestion(status));
  forget.addEventListener("click", () => {
    localStorage.removeItem(STORAGE.credential);
    unlockedAI = null;
    status.textContent = "Saved provider key forgotten.";
    status.className = "status success";
    render();
  });
  if (!window.crypto || !window.crypto.subtle) {
    status.textContent = "Secure browser encryption is unavailable here. AI key saving is disabled.";
    status.classList.add("error");
    save.disabled = true;
    unlock.disabled = true;
  }
  if (!stored) unlock.hidden = true;
  if (!unlockedAI) ask.disabled = true;
  actions.append(save, unlock, ask);
  if (stored) actions.append(forget);
  card.append(keyLabel, secretLabel, passLabel, actions, status);
  return card;
}
function getCredentialRecord() {
  const record = readJSON(STORAGE.credential, null);
  return record && record.version === 1 && record.salt && record.iv && record.ciphertext && record.provider ? record : null;
}
async function deriveEncryptionKey(passphrase, salt) {
  const material = await crypto.subtle.importKey("raw", new TextEncoder().encode(passphrase), "PBKDF2", false, ["deriveKey"]);
  return crypto.subtle.deriveKey({ name: "PBKDF2", salt, iterations: 310000, hash: "SHA-256" },
    material, { name: "AES-GCM", length: 256 }, false, ["encrypt", "decrypt"]);
}
async function encryptCredential(credential, passphrase) {
  const salt = crypto.getRandomValues(new Uint8Array(16));
  const iv = crypto.getRandomValues(new Uint8Array(12));
  const key = await deriveEncryptionKey(passphrase, salt);
  const bytes = new TextEncoder().encode(JSON.stringify(credential));
  const ciphertext = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, key, bytes);
  return { version: 1, provider: credential.provider, salt: encodeBytes(salt), iv: encodeBytes(iv), ciphertext: encodeBytes(new Uint8Array(ciphertext)) };
}
async function decryptCredential(record, passphrase) {
  const key = await deriveEncryptionKey(passphrase, decodeBytes(record.salt));
  const bytes = await crypto.subtle.decrypt({ name: "AES-GCM", iv: decodeBytes(record.iv) }, key, decodeBytes(record.ciphertext));
  const credential = JSON.parse(new TextDecoder().decode(bytes));
  if (!["Gemini", "DeepSeek", "OpenAI"].includes(credential.provider) || typeof credential.apiKey !== "string") {
    throw new Error("The encrypted provider key has an invalid format.");
  }
  return credential;
}
function encodeBytes(bytes) { return btoa(Array.from(bytes, (byte) => String.fromCharCode(byte)).join("")); }
function decodeBytes(value) { return Uint8Array.from(atob(value), (char) => char.charCodeAt(0)); }
async function showAIQuestion(status) {
  if (!unlockedAI) { status.textContent = "Unlock your encrypted provider key first."; status.className = "status error"; return; }
  const question = window.prompt("Your question is sent to your selected AI provider. Do not include personal or sensitive information. Enter 3–1,200 characters:");
  if (question === null) return;
  const prompt = sanitizePrompt(question);
  if (prompt.length < 3 || prompt.length > 1200) { status.textContent = "Use 3 to 1,200 readable characters."; status.className = "status error"; return; }
  if (!window.confirm(`Send this question to ${unlockedAI.provider}? The provider will receive the text and may process it under its own terms.`)) return;
  status.className = "status";
  status.textContent = "Waiting for the provider…";
  try {
    const answer = await sendAI(unlockedAI.provider, unlockedAI.apiKey, prompt);
    status.textContent = `Career AI: ${answer}`;
    status.className = "status success";
  } catch (error) {
    status.textContent = `Could not get an AI reply: ${error.message}`;
    status.className = "status error";
  }
}
function sanitizePrompt(value) {
  return Array.from(String(value || "")).filter((char) => char === "\n" || char === "\t" || char.codePointAt(0) >= 0x20)
    .join("").trim().slice(0, 1201);
}
async function sendAI(provider, apiKey, prompt) {
  const endpoints = {
    Gemini: ["https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent", "x-goog-api-key"],
    DeepSeek: ["https://api.deepseek.com/chat/completions", "Authorization"],
    OpenAI: ["https://api.openai.com/v1/chat/completions", "Authorization"]
  };
  const config = endpoints[provider];
  if (!config) throw new Error("Choose a supported provider.");
  const controller = new AbortController();
  const timeout = window.setTimeout(() => controller.abort(), 20000);
  try {
    const headers = { "Content-Type": "application/json" };
    if (config[1] === "Authorization") headers.Authorization = `Bearer ${apiKey}`;
    else headers[config[1]] = apiKey;
    const payload = provider === "Gemini"
      ? { contents: [{ parts: [{ text: prompt }] }] }
      : { model: provider === "DeepSeek" ? "deepseek-chat" : "gpt-4o-mini", messages: [{ role: "user", content: prompt }] };
    const response = await fetch(config[0], {
      method: "POST", headers, body: JSON.stringify(payload), signal: controller.signal,
      credentials: "omit", cache: "no-store", redirect: "error", referrerPolicy: "no-referrer"
    });
    if (!response.ok) throw new Error(`Provider returned HTTP ${response.status}.`);
    const data = await readBoundedJSON(response, 1_000_000);
    const answer = provider === "Gemini"
      ? data.candidates?.[0]?.content?.parts?.[0]?.text
      : data.choices?.[0]?.message?.content;
    if (typeof answer !== "string" || !answer.trim()) throw new Error("Provider returned no answer.");
    return answer.slice(0, 6000);
  } catch (error) {
    if (error.name === "AbortError") throw new Error("The request timed out.");
    if (error instanceof TypeError) throw new Error("Network or browser cross-origin access failed. A secure backend may be required.");
    throw error;
  } finally { window.clearTimeout(timeout); }
}
async function readBoundedJSON(response, maxBytes) {
  const contentLength = Number(response.headers.get("content-length") || 0);
  if (contentLength > maxBytes) throw new Error("Provider response is too large.");
  if (!response.body || typeof response.body.getReader !== "function") {
    const text = await response.text();
    if (new TextEncoder().encode(text).length > maxBytes) throw new Error("Provider response is too large.");
    return JSON.parse(text);
  }
  const reader = response.body.getReader();
  const chunks = [];
  let total = 0;
  try {
    while (true) {
      const { value, done } = await reader.read();
      if (done) break;
      total += value.byteLength;
      if (total > maxBytes) {
        await reader.cancel();
        throw new Error("Provider response is too large.");
      }
      chunks.push(value);
    }
  } finally {
    reader.releaseLock();
  }
  const bytes = new Uint8Array(total);
  let offset = 0;
  chunks.forEach((chunk) => { bytes.set(chunk, offset); offset += chunk.byteLength; });
  return JSON.parse(new TextDecoder().decode(bytes));
}
function signOut() {
  sessionStorage.removeItem("career-path-demo-session");
  state.email = "";
  unlockedAI = null;
  showLogin();
}
window.addEventListener("pagehide", () => { unlockedAI = null; });

init();
