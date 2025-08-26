function togglePassword() {
        const passwordField = document.getElementById('password');
        const eyeIcon = document.querySelector('.password-toggle i');
        if (passwordField.type === "password") {
            passwordField.type = "text";
            eyeIcon.classList.replace('fa-eye', 'fa-eye-slash');
        } else {
            passwordField.type = "password";
            eyeIcon.classList.replace('fa-eye-slash', 'fa-eye');
        }
}

document.addEventListener("DOMContentLoaded", function () {
    const message = document.querySelector(".error-message, .success-message");
    if (message) {
        setTimeout(() => {
            // Fade out
            message.style.transition = "opacity 0.8s ease";
            message.style.opacity = "0";

            setTimeout(() => {
                message.remove();

                // ✅ Remove query params from URL without reload
                if (window.history.replaceState) {
                    const cleanUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
                    window.history.replaceState({}, document.title, cleanUrl);
                }
            }, 800);
        }, 3000);
    }
});

