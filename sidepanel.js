document.addEventListener('DOMContentLoaded', () => {
  // Restore saved notes
  chrome.storage.local.get(['researchNotes'], ({ researchNotes }) => {
    if (researchNotes) {
      document.getElementById('notes').value = researchNotes;
    }
  });

  // Trigger on radio click
  document.querySelectorAll('input[name="operation"]').forEach(radio => {
    radio.addEventListener('click', () => {
      processText(radio.value);
    });
  });

  // Save notes
  document.getElementById('saveNotesBtn')
          .addEventListener('click', saveNotes);

  // Dark-mode toggle
  document.getElementById('darkModeToggle')
          .addEventListener('click', () => {
    document.body.classList.toggle('dark');
  });
});

async function processText(operation) {
  const loading = document.getElementById('loading');
  const radios  = document.querySelectorAll('input[name="operation"]');
  radios.forEach(r => r.disabled = true);
  loading.classList.add('show');

  try {
    // Get selected text from page
    const [tab] = await chrome.tabs.query({
      active: true, currentWindow: true
    });
    const [{ result }] = await chrome.scripting.executeScript({
      target: { tabId: tab.id },
      function: () => window.getSelection().toString()
    });

    if (!result) {
      showResult('⚠️ Please select text first.');
      return;
    }

    // Call backend
    const resp = await fetch(
      'http://localhost:8080/api/thinker/process',
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: result, operation })
      }
    );
    if (!resp.ok) throw new Error(`Server ${resp.status}`);
    const text = await resp.text();
    showResult(text.replace(/\n/g, '<br>'));
  } catch (err) {
    showResult('❌ Error: ' + err.message);
  } finally {
    loading.classList.remove('show');
    document.querySelectorAll('input[name="operation"]')
            .forEach(r => r.disabled = false);
  }
}

function showResult(content) {
  const resultsDiv = document.getElementById('results');
  resultsDiv.innerHTML = `
    <div class="result-item">
      <button class="copy-btn" title="Copy result">📋</button>
      <strong>🔍 Result:</strong>
      <div class="result-content" id="resultText">${content}</div>
    </div>`;

  // Attach copy listener
  resultsDiv.querySelector('.copy-btn')
            .addEventListener('click', copyToClipboard);
}

function copyToClipboard() {
  const text = document.getElementById('resultText').innerText;

  navigator.clipboard.writeText(text)
    .then(showCopyConfirmation)
    .catch(() => {
      // fallback
      const ta = document.createElement('textarea');
      ta.value = text;
      ta.style.position = 'fixed';
      ta.style.opacity  = '0';
      document.body.appendChild(ta);
      ta.focus();
      ta.select();
      document.execCommand('copy');
      document.body.removeChild(ta);
      showCopyConfirmation();
    });
}

function showCopyConfirmation() {
  const btn = document.querySelector('.copy-btn');
  const orig = btn.innerText;
  btn.innerText = '✅';
  setTimeout(() => btn.innerText = orig, 1000);
}

function saveNotes() {
  const notes = document.getElementById('notes').value;
  const btn   = document.getElementById('saveNotesBtn');
  chrome.storage.local.set({ researchNotes: notes }, () => {
    btn.innerText = '✅ Saved!';
    setTimeout(() => btn.innerText = '💾 Save Notes', 1500);
  });
}
