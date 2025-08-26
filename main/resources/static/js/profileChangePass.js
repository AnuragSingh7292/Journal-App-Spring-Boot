// document.addEventListener("DOMContentLoaded", function () {
//     // DOM Elements
//     const journalForm = document.getElementById("changePassForm");
//     const journalModal = document.getElementById("changePassModal");
//     const closeModalButton = document.getElementById("changePassCloseModal");
//     const cancelJournalButton = document.getElementById("changePassCancelJournal");
//     const newJournalButton = document.getElementById("changePassBtn");
//
//     // Show modal
//     function showCreateModal() {
//         journalModal.classList.add('active');
//         document.body.style.overflow = 'hidden';
//     }
//
//     // Hide modal
//     function hideCreateModal() {
//         journalModal.classList.remove('active');
//         document.body.style.overflow = '';
//         journalForm.reset();
//     }
//
//     // Event Listeners
//     newJournalButton.addEventListener("click", showCreateModal);
//     closeModalButton.addEventListener("click", hideCreateModal);
//     cancelJournalButton.addEventListener("click", hideCreateModal);
//
//     // Form submission
//     // journalForm.addEventListener("submit", function (e) {
//     //     e.preventDefault();
//     //
//     //     const title = document.getElementById("journalTitle").value.trim();
//     //     const content = document.getElementById("journalContent").value.trim();
//     //     const isPrivate = document.getElementById("isPrivate").checked;
//     //
//     //     if (!title) {
//     //         alert("Title is required.");
//     //         return;
//     //     }
//     //
//     //     // fetch(`${BASE_PATH}journal`, {
//     //     //     method: "POST",
//     //     //     headers: {
//     //     //         "Content-Type": "application/json"
//     //     //     },
//     //     //     body: JSON.stringify({
//     //     //         title,
//     //     //         content,
//     //     //         isPrivate
//     //     //     })
//     //     // })
//     //     //     .then(response => {
//     //     //         if (!response.ok) throw new Error("Failed to save journal");
//     //     //         return response.text();
//     //     //     })
//     //     //     .then(() => {
//     //     //         // showSuccessMessage("Journal saved successfully!");
//     //     //         alert("Journal saved successfully!");
//     //     //         hideCreateModal();
//     //     //
//     //     //         // Optional: Refresh the page or update UI
//     //     //         if (typeof loadJournals === 'function') {
//     //     //             loadJournals(); // If this function exists in main file
//     //     //         } else {
//     //     //             window.location.reload(); // Fallback
//     //     //         }
//     //     //     })
//     //     //     .catch(err => {
//     //     //         console.error("Error saving journal:", err);
//     //     //         alert("Error saving journal. Please try again.");
//     //     //     });
//     // });
//
//     //  this  method to  show the  message  like "Journal saved successfully!"
//     // function showSuccessMessage(message) {
//     //     const messageDiv = document.getElementById("successMessage");
//     //     messageDiv.textContent = message;
//     //     messageDiv.classList.add("show");
//     //
//     //     setTimeout(() => {
//     //         messageDiv.classList.remove("show");
//     //     }, 4000); // Message disappears after 4 seconds
//     // }
//
// });


document.addEventListener("DOMContentLoaded", function () {
    // DOM Elements
    const journalForm = document.getElementById("changePassForm");
    const journalModal = document.getElementById("changePassModal");
    const closeModalButton = document.getElementById("changePassCloseModal");
    const cancelJournalButton = document.getElementById("changePassCancelJournal");
    const newJournalButton = document.getElementById("changePassBtn");

    // Show modal
    function showCreateModal() {
        journalModal.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    // Hide modal
    function hideCreateModal() {
        journalModal.classList.remove('active');
        document.body.style.overflow = '';
        journalForm.reset();

        // Reset all password fields to hidden state
        const passwordInputs = document.querySelectorAll('input[type="text"][id$="Pass"], input[type="password"]');
        passwordInputs.forEach(input => {
            input.type = 'password';
        });

        // Reset all eye icons to default state
        const eyeIcons = document.querySelectorAll('.eye-icon');
        eyeIcons.forEach(icon => {
            icon.textContent = '👁️';
        });
    }

    // Password visibility toggle functionality
    const toggleButtons = document.querySelectorAll('.toggle-password');

    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const passwordInput = document.getElementById(targetId);
            const eyeIcon = this.querySelector('.eye-icon');

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                eyeIcon.textContent = '🔒';
            } else {
                passwordInput.type = 'password';
                eyeIcon.textContent = '👁️';
            }
        });
    });

    // Form submission handling
    journalForm.addEventListener("submit", function (e) {
        e.preventDefault();

        // Get form values
        const oldPassword = document.getElementById("oldPass").value;
        const newPassword = document.getElementById("newPass").value;
        const confirmPassword = document.getElementById("conPass").value;

        // In your form submission handler, replace the basic validation with:
        const validationError = validatePassword(newPassword, confirmPassword);
        if (validationError) {
            alert(validationError);
            return;
        }

        // Here you would typically send the data to your server
        console.log("Password change submitted:", {
            oldPassword,
            newPassword
        });
        fetch(`${BASE_PATH}profileController/api/change-password`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                oldPassword,
                newPassword
            })
        })
            .then(response => response.text())
            .then(data => {
                alert(data); // show backend response
                hideCreateModal();
            })
            .catch(err => {
                console.error("Error:", err);
                alert("Something went wrong!");
            });
    });

    // Enhanced password validation
    function validatePassword(newPassword, confirmPassword) {
        // Check if passwords match
        if (newPassword !== confirmPassword) {
            return "New password and confirm password do not match!";
        }

        // Check minimum length
        if (newPassword.length < 8) {
            return "Password should be at least 8 characters long!";
        }

        // Check for at least one uppercase letter
        if (!/[A-Z]/.test(newPassword)) {
            return "Password must contain at least one uppercase letter!";
        }

        // Check for at least one lowercase letter
        if (!/[a-z]/.test(newPassword)) {
            return "Password must contain at least one lowercase letter!";
        }

        // Check for at least one digit
        if (!/\d/.test(newPassword)) {
            return "Password must contain at least one number!";
        }

        // Check for at least one special character
        if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(newPassword)) {
            return "Password must contain at least one special character!";
        }

        // Check for no spaces
        if (/\s/.test(newPassword)) {
            return "Password should not contain spaces!";
        }

        // All validation passed
        return null;
    }

    // Event Listeners
    if (newJournalButton) {
        newJournalButton.addEventListener("click", showCreateModal);
    }

    if (closeModalButton) {
        closeModalButton.addEventListener("click", hideCreateModal);
    }

    if (cancelJournalButton) {
        cancelJournalButton.addEventListener("click", hideCreateModal);
    }


    // Close modal when clicking outside of it
    journalModal.addEventListener("click", function (e) {
        if (e.target === journalModal) {
            hideCreateModal();
        }
    });
});