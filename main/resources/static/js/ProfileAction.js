document.addEventListener("DOMContentLoaded", function () {
    // Edit Journal Functionality
    var journalId;
    document.addEventListener('click', async (e) => {
        // Handle edit button click from dropdown
        if (e.target.closest('.edit-btn')) {
            journalId = e.target.closest('.edit-btn').dataset.id;
            console.log('Editing journal ID:', journalId);
            await openEditModal(journalId);
        }
    });

    async function openEditModal(journalId) {
        try {
            // Fetch journal data
            const response = await fetch(`${BASE_PATH}journal/id/${journalId}`);
            if (!response.ok) throw new Error('Failed to fetch journal');

            const journal = await response.json();
            console.log('Journal data:', journal);

            // Populate edit modal
            document.getElementById('editJournalTitle').value = journal.title;
            document.getElementById('editJournalContent').value = journal.content;
            document.getElementById('editIsPrivate').checked = journal.isPrivate;
            updateToggleLabel(journal.isPrivate);

            // Show modal
            document.getElementById('editJournalModal').classList.add('active');

        } catch (error) {
            console.error('Error:', error);
            alert('Failed to load journal for editing');
        }
    }

    // Update privacy toggle label
    function updateToggleLabel(isPrivate) {
        const label = document.querySelector('#editIsPrivate + label .toggle-label');
        if (label) {
            label.textContent = isPrivate ? 'Private Journal' : 'Public Journal';
            label.style.color = isPrivate ? '#1a73e8' : '#5f6368';
        }
    }

    // Toggle change handler
    const editIsPrivateCheckbox = document.getElementById('editIsPrivate');
    if (editIsPrivateCheckbox) {
        editIsPrivateCheckbox.addEventListener('change', function() {
            updateToggleLabel(this.checked);
        });
    }

    // Close modal handlers
    const closeEditModalButton = document.getElementById('editCloseModal');
    if (closeEditModalButton) {
        closeEditModalButton.addEventListener('click', () => {
            document.getElementById('editJournalModal').classList.remove('active');
        });
    }

    const cancelEditJournalButton = document.getElementById('editCancelJournal');
    if (cancelEditJournalButton) {
        cancelEditJournalButton.addEventListener('click', () => {
            document.getElementById('editJournalModal').classList.remove('active');
        });
    }

    // // Close modal when clicking outside content
    // const editModal = document.getElementById('editJournalModal');
    // if (editModal) {
    //     editModal.addEventListener('click', (e) => {
    //         if (e.target === editModal) {
    //             editModal.classList.remove('active');
    //         }
    //     });
    // }


    /* sending data to backend to  update the  journal */
    const editForm = document.getElementById("editJournalForm");

    editForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        // Grab values from form
        const title = document.getElementById("editJournalTitle").value;
        const content = document.getElementById("editJournalContent").value;
        const isPrivate = document.getElementById("editIsPrivate").checked;
        document.getElementById('editJournalModal').classList.remove('active');

        const data = {
            title: title,
            content: content,
            isPrivate: isPrivate  // must match your Java field name
        };

        try {
            const response = await fetch(`${BASE_PATH}journal/id/${journalId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                const message = await response.text();  // get message instead of JSON
                alert(message);  // shows "Journal updated successfully ✅"
                loadLoggedUserJournal();
            } else {
                const errorMessage = await response.text();
                console.error("Update failed:", errorMessage);
                alert("Update failed: " + errorMessage);
            }
        } catch (error) {
            console.error("Error:", error);
        }
    });



    /* deleting the  journal  by id */

    // Attach delete event listener
    document.addEventListener("click", async function(e) {
        if (e.target.closest(".delete-btn")) {
            const deleteJournalId = e.target.closest(".delete-btn").getAttribute("data-id");

            // Ask for confirmation
            const confirmDelete = confirm("Are you sure you want to delete this journal?");
            if (!confirmDelete) return; // stop if user cancels

            try {
                const response = await fetch(`${BASE_PATH}journal/id/${deleteJournalId}`, {
                    method: "DELETE"
                });

                if (response.ok) {
                    // remove from UI without reloading
                    e.target.closest(".journal-card").remove();
                } else {
                    alert("Failed to delete journal.");
                }
            } catch (error) {
                console.error("Error deleting journal:", error);
                alert("Something went wrong.");
            }
        }
    });


});