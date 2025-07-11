# Thynkly Chrome Extension

**Thynkly** is an AI-powered research assistant Chrome extension that seamlessly integrates with a Spring Boot + Gemini backend to provide on-demand text summarization and context insights directly in your browser.

---

## 🚀 Features

* **Inline AI Summaries**: Highlight any text on a webpage and get an instant, concise summary displayed in a sidebar.
* **Sidebar Interface**: Clean, responsive sidebar for displaying summaries, suggested queries, and related context.
* **Customizable Backend**: Connects to a configurable REST API endpoint; easy to point at local or remote Spring Boot + Gemini services.
* **One-Click Activation**: Quick access via toolbar button; no extra clicks to start summarizing.
* **Styling & Theming**: Lightweight CSS (Tailwind compatible) for easy customization of the sidebar’s look and feel.

---

## 📂 Repository Structure

```
Thynkly-Extension/         # Chrome extension source
├── background.js          # Background script for toolbar activation & messaging
├── sidepanel.html         # Sidebar HTML template
├── sidepanel.css          # Sidebar styling
├── sidepanel.js           # Sidebar logic and API calls
├── manifest.json          # Chrome extension manifest (v3)
├── Thinker-Brief/         # Design docs and UI/UX mockups
├── .idea/                 # IDE settings (optional)
└── README.md              # Project overview and usage instructions
```

---

## 🛠️ Installation & Setup

1. **Clone the repo**

   ```bash
   git clone https://github.com/otAdarshP/Thynkly-Extension.git
   cd Thynkly-Extension
   ```
2. **Configure your backend URL**

   * Open `sidepanel.js` and update the `API_ENDPOINT` constant to point to your running Spring Boot + Gemini service (default: `http://localhost:8080/api/research/process`).
3. **Load in Chrome**

   1. Open `chrome://extensions/` in your browser.
   2. Enable "Developer mode" (toggle in top right).
   3. Click **Load unpacked** and select the `Thynkly-Extension` directory.
4. **Use the extension**

   * Navigate to any webpage, highlight text, then click the Thynkly toolbar icon to open the sidebar and view summaries.

---

## 🧑‍💻 Development

* **Background script** (`background.js`): Listens for toolbar icon clicks and injects the sidebar into the current page.
* **Sidebar UI** (`sidepanel.html` + `sidepanel.css`): Defines the HTML structure and styling for the pop‑out panel.
* **Sidebar logic** (`sidepanel.js`): Handles user interactions, sends selected text to the backend, and renders responses.
* **Manifest** (`manifest.json`): Configures permissions, scripts, and extension metadata (V3 format).

Best practices:

* Keep sidebar CSS self‑contained to avoid conflicts with host pages.
* Use Promises and `async/await` in `sidepanel.js` for clean API calls.
* Update version and description in `manifest.json` for each release.

---

## ✅ Usage Example

1. Highlight a paragraph on any webpage.
2. Click the Thynkly icon in the toolbar.
3. The sidebar will slide in, showing a summarized version of the selected content.
4. Optionally, click suggested links or queries for deeper research insights.

---

## 🤝 Contributing

Contributions are welcome! Feel free to submit issues or pull requests:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m "Add YourFeature"`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a Pull Request.

Please follow the existing code style and include clear documentation for any new features.

---

## 📄 License

This project is released under the MIT License. See [LICENSE](LICENSE) for details.

---

*Happy researching!* 🚀
