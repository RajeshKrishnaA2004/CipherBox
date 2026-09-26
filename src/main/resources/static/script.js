const method = document.getElementById("method");
const methodButtons = document.querySelectorAll(".method-option");

const keySection = document.getElementById("key-section");
const keyInput = document.getElementById("key");
const keyHint = document.getElementById("key-hint");

const rsaSection = document.getElementById("rsa-section");
const publicKey = document.getElementById("public-key");
const privateKey = document.getElementById("private-key");
const recipientKey = document.getElementById("recipient-key");
const decryptPrivateKey = document.getElementById("decrypt-private-key");
const generateKeys = document.getElementById("generate-keys");
const copyButtons = document.querySelectorAll(".copy-button");

const messageInput = document.getElementById("message");
const output = document.getElementById("output");
const status = document.getElementById("status");
const charCount = document.getElementById("char-count");

const encryptButton = document.getElementById("encrypt-btn");
const decryptButton = document.getElementById("decrypt-btn");


methodButtons.forEach(button => {

    button.addEventListener("click", () => {

        method.value = button.dataset.method;

        methodButtons.forEach(btn => btn.classList.remove("active"));
        button.classList.add("active");

        updateMethodUI();
    });
});


function updateMethodUI() {

    const selectedMethod = method.value;

    if (selectedMethod === "xor" || selectedMethod === "aes") {

        keySection.classList.remove("key-hidden");

        if (selectedMethod === "aes") {
            keyHint.textContent = "AES";
            keyInput.placeholder = "Enter your AES password...";
        } else {
            keyHint.textContent = "XOR";
            keyInput.placeholder = "Enter your secret key...";
        }

    } else {

        keySection.classList.add("key-hidden");
        keyInput.value = "";
    }

    if (selectedMethod === "rsa") {
        rsaSection.classList.remove("rsa-hidden");
    } else {
        rsaSection.classList.add("rsa-hidden");
    }
}


updateMethodUI();


generateKeys.addEventListener("click", async () => {

    status.textContent = "LOADING";
    output.innerHTML =
        `<span class="placeholder">Generating key pair...</span>`;

    try {

        const response = await fetch("/rsa/generate-keys");

        if (!response.ok) {
            throw new Error("Unable to generate keys");
        }

        const keys = await response.json();

        publicKey.textContent = keys.publicKey;
        privateKey.textContent = keys.privateKey;

        status.textContent = "KEYS READY";

        output.innerHTML =
            `<span class="placeholder">RSA key pair generated successfully.</span>`;

    } catch (error) {

        showError("Unable to generate RSA keys.");
    }
});


copyButtons.forEach(button => {

    button.addEventListener("click", async () => {

        const target = document.getElementById(button.dataset.copy);
        const text = target.textContent.trim();

        if (!text || text === "No key generated.") {
            return;
        }

        try {

            await navigator.clipboard.writeText(text);

            const original = button.innerHTML;

            button.innerHTML = "<span>✓</span> COPIED";

            setTimeout(() => {
                button.innerHTML = original;
            }, 1200);

        } catch (error) {

            showError("Unable to copy key.");
        }
    });
});


messageInput.addEventListener("input", () => {
    charCount.textContent = messageInput.value.length;
});


encryptButton.addEventListener("click", async () => {
    await processMessage("encrypt");
});


decryptButton.addEventListener("click", async () => {
    await processMessage("decrypt");
});


async function processMessage(action) {

    const message = messageInput.value;
    const selectedMethod = method.value;

    let key = keyInput.value;


    if (message.trim() === "") {

        showError("Please enter a message.");

        return;
    }


    if (selectedMethod === "rsa") {

        if (action === "encrypt") {

            key = recipientKey.value;

            if (key.trim() === "") {

                showError("Please enter the recipient's public key.");

                return;
            }

        } else {

            key = decryptPrivateKey.value;

            if (key.trim() === "") {

                showError("Please enter the private key for decryption.");

                return;
            }
        }

    } else if (
        (selectedMethod === "xor" || selectedMethod === "aes") &&
        key.trim() === ""
    ) {

        showError("Please enter an encryption key.");

        return;
    }


    const url =
        `/${action}` +
        `?method=${encodeURIComponent(selectedMethod)}` +
        `&message=${encodeURIComponent(message)}` +
        `&key=${encodeURIComponent(key)}`;


    status.textContent =
        action === "encrypt" ? "ENCRYPTING" : "DECRYPTING";

    output.innerHTML =
        `<span class="placeholder">Processing...</span>`;


    try {

        const response = await fetch(url);
        const result = await response.text();

        output.textContent = result;


        if (
            !response.ok ||
            result.startsWith("Encryption failed:") ||
            result.startsWith("Decryption failed:") ||
            result === "Invalid method"
        ) {

            status.textContent = "ERROR";

            return;
        }


        status.textContent =
            action === "encrypt" ? "ENCRYPTED" : "DECRYPTED";

    } catch (error) {

        showError("Unable to connect to CipherBox.");
    }
}


function showError(message) {

    output.textContent = message;
    status.textContent = "ERROR";
}