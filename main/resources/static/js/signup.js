// Toggle password visibility
    function togglePassword(fieldId) {
    const field = document.getElementById(fieldId);
    const icon = document.querySelector(`#${fieldId} + .password-toggle i`);
    if (field.type === "password") {
    field.type = "text";
    icon.classList.replace("fa-eye", "fa-eye-slash");
} else {
    field.type = "password";
    icon.classList.replace("fa-eye-slash", "fa-eye");
}
}

    // Form submission handler
    document.getElementById("signupForm").addEventListener("submit", async function (e) {
        e.preventDefault();

        const username = document.getElementById("username").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        let isValid = true;

        // Username
        if (username.length < 3) {
            document.getElementById("usernameError").style.display = "flex";
            isValid = false;
        } else {
            document.getElementById("usernameError").style.display = "none";
        }

        // Email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            document.getElementById("emailError").style.display = "flex";
            isValid = false;
        } else {
            document.getElementById("emailError").style.display = "none";
        }

        // Password
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (!passwordRegex.test(password)) {
            document.getElementById("passwordError").style.display = "flex";
            isValid = false;
        } else {
            document.getElementById("passwordError").style.display = "none";
        }

        // Confirm Password
        if (password !== confirmPassword) {
            document.getElementById("confirmPasswordError").style.display = "flex";
            isValid = false;
        } else {
            document.getElementById("confirmPasswordError").style.display = "none";
        }

        if (!isValid) return;

        // Show loading
        document.getElementById("signupBtnText").textContent = "Creating account...";
        document.getElementById("signupSpinner").classList.remove("hidden");
        let st;
        try {
            const response = await fetch("/Journal/public/create-user", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({username, email, password}),
            });
            if (response.ok) {
               console.log(response.status);
                // document.getElementById("successMessage").style.display = "flex";
                document.getElementById("signupBtnText").textContent = "Create Account";
                document.getElementById("signupSpinner").classList.add("hidden");
                    window.location.href = "/Journal/login";
            } else {
                const errorData = await response.json();
                if (response.status === 400) {
                    let handled = false;
                    if (errorData.message.includes("email")) {
                        document.getElementById("emailError").textContent = "Email already exists";
                        document.getElementById("emailError").style.display = "flex";
                        handled = true;
                    }
                    if (errorData.message.includes("username")) {
                        document.getElementById("usernameError").textContent = "Username already exists";
                        document.getElementById("usernameError").style.display = "flex";
                        handled = true;
                    }
                    if (!handled) {
                        alert("Signup failed. Please try again.");
                    }
                } else {
                    alert("Signup failed. Please try again.");
                }
            }
        } catch (err) {
            console.error("Signup error:", err);
            alert("An error occurred during signup.");
        } finally {
            document.getElementById("signupBtnText").textContent = "Create Account";
            document.getElementById("signupSpinner").classList.add("hidden");
        }
    });