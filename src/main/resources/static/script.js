const method = document.getElementById("method");

const methodButtons =
    document.querySelectorAll(".method-option");

const keySection =
    document.getElementById("key-section");

const keyInput =
    document.getElementById("key");

const keyHint =
    document.getElementById("key-hint");

const messageInput =
    document.getElementById("message");

const output =
    document.getElementById("output");

const status =
    document.getElementById("status");

const charCount =
    document.getElementById("char-count");

const encryptButton =
    document.getElementById("encrypt-btn");

const decryptButton =
    document.getElementById("decrypt-btn");


// ================================
// METHOD SELECTION
// ================================

methodButtons.forEach(button => {

    button.addEventListener("click", () => {

        const selectedMethod =
            button.dataset.method;


        // Update hidden select

        method.value =
            selectedMethod;


        // Update active button

        methodButtons.forEach(btn => {

            btn.classList.remove("active");

        });

        button.classList.add("active");


        // Show / hide key

        updateKeyVisibility();

    });

});


// ================================
// KEY VISIBILITY
// ================================

function updateKeyVisibility() {

    if (
        method.value === "xor" ||
        method.value === "aes"
    ) {

        keySection.classList.remove("key-hidden");


        // Change key label

        if (method.value === "aes") {

            keyHint.textContent = "AES";

            keyInput.placeholder =
                "Enter your AES password...";

        } else {

            keyHint.textContent = "XOR";

            keyInput.placeholder =
                "Enter your secret key...";

        }

    } else {

        keySection.classList.add("key-hidden");

        keyInput.value = "";

    }
}


// Run when page loads

updateKeyVisibility();


// ================================
// CHARACTER COUNT
// ================================

messageInput.addEventListener("input", () => {

    charCount.textContent =
        messageInput.value.length;

});


// ================================
// ENCRYPT
// ================================

encryptButton.addEventListener("click", async () => {

    await processMessage("encrypt");

});


// ================================
// DECRYPT
// ================================

decryptButton.addEventListener("click", async () => {

    await processMessage("decrypt");

});


// ================================
// MAIN REQUEST
// ================================

async function processMessage(action) {

    const message =
        messageInput.value;

    const selectedMethod =
        method.value;

    const key =
        keyInput.value;


    // Empty message

    if (message.trim() === "") {

        showError(
            "Please enter a message."
        );

        return;
    }


    // XOR / AES require key

    if (
        (
            selectedMethod === "xor" ||
            selectedMethod === "aes"
        ) &&
        key.trim() === ""
    ) {

        showError(
            "Please enter an encryption key."
        );

        return;
    }


    // Build URL

    let url =
        `/${action}` +
        `?method=${encodeURIComponent(selectedMethod)}` +
        `&message=${encodeURIComponent(message)}`;


    // Add key for XOR / AES

    if (
        selectedMethod === "xor" ||
        selectedMethod === "aes"
    ) {

        url +=
            `&key=${encodeURIComponent(key)}`;

    }


    // Status

    status.textContent =
        action === "encrypt"
            ? "ENCRYPTING"
            : "DECRYPTING";


    output.innerHTML =
        `<span class="placeholder">Processing...</span>`;


    try {

        const response =
            await fetch(url);


        const result =
            await response.text();


        output.textContent =
            result;


        status.textContent =
            action === "encrypt"
                ? "ENCRYPTED"
                : "DECRYPTED";


    } catch (error) {

        showError(
            "Unable to connect to CipherBox."
        );

    }

}


// ================================
// ERROR
// ================================

function showError(message) {

    output.textContent =
        message;

    status.textContent =
        "ERROR";
}