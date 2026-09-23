const button = document.getElementById("encrypt-btn");

button.addEventListener("click", async () => {

    const message = document.getElementById("message").value;

    const response = await fetch(
        `/encrypt?message=${encodeURIComponent(message)}`
    );

    const result = await response.text();

    document.getElementById("output").textContent = result;
});

const decryptButton = document.getElementById("decrypt-btn");

decryptButton.addEventListener("click", async () => {

    console.log("DECRYPT CLICKED");

    const message = document.getElementById("message").value;

    const response = await fetch(
        `/decrypt?message=${encodeURIComponent(message)}`
    );

    const result = await response.text();

    console.log("RESULT:", result);

    document.getElementById("output").textContent = result;
});