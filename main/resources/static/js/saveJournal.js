document.addEventListener("DOMContentLoaded", function () {
    // DOM Elements
    const journalForm = document.getElementById("journalForm");
    const journalModal = document.getElementById("journalModal");
    const newJournalButton = document.getElementById("newJournalButton");
    const closeModalButton = document.getElementById("closeModal");
    const cancelJournalButton = document.getElementById("cancelJournal");

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
    }

    // Event Listeners
    newJournalButton.addEventListener("click", showCreateModal);
    closeModalButton.addEventListener("click", hideCreateModal);
    cancelJournalButton.addEventListener("click", hideCreateModal);

    // Form submission
    journalForm.addEventListener("submit", function (e) {
        e.preventDefault();

        const title = document.getElementById("journalTitle").value.trim();
        const content = document.getElementById("journalContent").value.trim();
        const isPrivate = document.getElementById("isPrivate").checked;

        if (!title) {
            alert("Title is required.");
            return;
        }

        fetch(`${BASE_PATH}journal`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title,
                content,
                isPrivate
            })
        })
            .then(response => {
                if (!response.ok) throw new Error("Failed to save journal");
                return response.text();
            })
            .then(() => {
                // showSuccessMessage("Journal saved successfully!");
                alert("Journal saved successfully!");
                hideCreateModal();

                // Optional: Refresh the page or update UI
                if (typeof loadJournals === 'function') {
                    loadJournals(); // If this function exists in main file
                } else {
                    window.location.reload(); // Fallback
                }
            })
            .catch(err => {
                console.error("Error saving journal:", err);
                alert("Error saving journal. Please try again.");
            });
    });

    //  this  method to  show the  message  like "Journal saved successfully!"
    // function showSuccessMessage(message) {
    //     const messageDiv = document.getElementById("successMessage");
    //     messageDiv.textContent = message;
    //     messageDiv.classList.add("show");
    //
    //     setTimeout(() => {
    //         messageDiv.classList.remove("show");
    //     }, 4000); // Message disappears after 4 seconds
    // }

});