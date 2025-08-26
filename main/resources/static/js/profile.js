document.addEventListener('DOMContentLoaded', function() {

    function loadLoggedUserJournal() {
        // First load user data
        fetch(`${BASE_PATH}user/me`)
            .then(res => {
                if (!res.ok) throw new Error('Failed to fetch user data');
                return res.json();
            })
            .then(user => {
                // Update sidebar with user data
                document.getElementById('sidebarUsername').textContent = user.username || 'Username';
                document.getElementById('sidebarEmail').textContent = user.email || 'No email';

                // Then load journals
                return fetch(`${BASE_PATH}user/journals/me`);
            })
            .then(res => {
                if (!res.ok) throw new Error('Failed to fetch journals');
                return res.json();
            })
            .then(data => {
                // manually we are diong here to  count all private and public journal

                // // Normalize values
                // journals.forEach(j => {
                //     if (typeof j.isPrivate === 'string') {
                //         j.isPrivate = j.isPrivate === "true";
                //     }
                //     if (!j._id && j.id && j.id.timestamp) {
                //         j._id = j.id.timestamp.toString();
                //     } else if (!j._id) {
                //         j._id = Math.random().toString(36).substr(2, 9);
                //     }
                // });
                // let publicCount = journals.filter(j => j.isPrivate === false).length;
                // let privateCount = journals.filter(j => j.isPrivate === true).length;

                // here backennd  sending all journal  with  all count of public private
                const {counts, journals} = data;
                document.getElementById('totalJournals').textContent = counts.total;
                document.getElementById('publicJournals').textContent = counts.publicCount;
                document.getElementById('privateJournals').textContent = counts.privateCount;

                const container = document.getElementById('journalsContainer');
                if (journals.length === 0) {
                    container.innerHTML = `
                    <div class="empty-state">
                        <i class="fas fa-book-open"></i>
                        <p>You haven't created any journals yet</p>
                    </div>
                `;
                } else {
                    container.innerHTML = journals.map(journal => `
                    <div class="journal-card">
                        <div class="journal-header">
                            <h3 class="journal-title">${journal.title || 'Untitled Journal'}</h3>
                            <div class="journal-date">
                                <i class="fas ${journal.isPrivate ? 'fa-lock journal-private' : 'fa-globe journal-public'}"></i>
                                ${journal.createdAt.split(",")[0]}
                                <span class="journal-time">${new Date(journal.createdAt).toLocaleTimeString([], {
                        hour: '2-digit',
                        minute: '2-digit'
                    })}</span>
                            </div>
                        </div>
                        <p class="journal-content">${
                        journal.content.length > 230 ?
                            journal.content.substring(0, 230) + '...' :
                            journal.content
                    }</p>
                        <div class="journal-footer">
                            <button class="read-more" data-id="${journal.id}">Read</button>
                            <div class="journal-actions">
                                <button class="journal-action like-btn" data-id="${journal.id}">
                                    <i class="far fa-heart"></i>
                                    <span>Like</span>
                                </button>
                                <button class="journal-action comment-btn" data-id="${journal.id}">
                                    <i class="far fa-comment"></i>
                                    <span>Comment</span>
                                </button>
                                <button class="journal-action share-btn" data-id="${journal.id}">
                                    <i class="fas fa-share"></i>
                                    <span>Share</span>
                                </button>
                                
                                                                <div class="action-menu">
                                      <button class="more-actions" aria-label="More options">
                                                <i class="fas fa-ellipsis-v"></i>
                                     </button>
                                      <div class="dropdown-menu">
                                                <button class="dropdown-item edit-btn" data-id="${journal.id}">
                                                    <i class="fas fa-pencil-alt"></i> Edit
                                                </button>
                                                <button class="dropdown-item delete-btn" data-id="${journal.id}">
                                                    <i class="fas fa-trash-alt"></i> Delete
                                                </button>
                                </div>
                        </div>
                            </div>
                        </div>
                    </div>
                `).join('');

                    document.querySelectorAll('.read-more').forEach(btn => {
                        btn.addEventListener('click', function (e) {
                            e.stopPropagation();
                            const journalId = this.getAttribute('data-id');
                            const journal = journals.find(j => j.id === journalId);
                            showJournalOverlay(journal);
                        });
                    });
                }
            })
            .catch(err => {
                console.error('Error:', err);
                document.getElementById('journalsContainer').innerHTML = `
                <div class="error-message">
                    <i class="fas fa-exclamation-triangle"></i>
                    <span>Failed to load journals. Please try again.</span>
                </div>
            `;
            });
    }

    loadLoggedUserJournal();
    window.loadLoggedUserJournal = loadLoggedUserJournal;

    // Overlay function
    function showJournalOverlay(journal) {
        const overlayHTML = `
            <div class="journal-overlay">
                <div class="overlay-content">
                    <button class="close-overlay"><i class="fas fa-times"></i></button>
                    <div class="journal-header">
                        <h3>${journal.title || 'Untitled Journal'}</h3>
                        <div class="journal-date">
                            <span>${journal.createdAt.split(",")[0]}</span>
                            <span class="journal-time">${new Date(journal.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                        </div>
                    </div>
                    <div class="journal-full-content">${journal.content}</div>
                    <div class="journal-footer">
                        <div class="journal-actions">
                            <button class="journal-action like-btn" data-id="${journal.id}">
                                <i class="far fa-heart"></i>
                                <span>Like</span>
                            </button>
                            <button class="journal-action comment-btn" data-id="${journal.id}">
                                <i class="far fa-comment"></i>
                                <span>Comment</span>
                            </button>
                            <button class="journal-action share-btn" data-id="${journal.id}">
                                <i class="fas fa-share"></i>
                                <span>Share</span>
                            </button>
                        </div>
                        <span class="journal-status ${journal.isPrivate ? 'journal-private' : 'journal-public'}">
                            <i class="fas ${journal.isPrivate ? 'fa-lock' : 'fa-globe'}"></i>
                            ${journal.isPrivate ? 'Private' : 'Public'}
                        </span>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', overlayHTML);

        document.querySelector('.close-overlay').addEventListener('click', () => {
            document.querySelector('.journal-overlay').remove();
        });
    }

    function showEditForm() {
        alert('Edit profile functionality will be added soon');
    }
}); // 👈 Missing brace was here!
