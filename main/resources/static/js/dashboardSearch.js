// search  journal by  user name
const searchInput = document.getElementById('searchInput');
const suggestionsBox = document.getElementById('suggestionsBox');
// References
const allJournalsLink = document.getElementById('allJournalsLink');
const myJournalsLink = document.getElementById('myJournalsLink');
currentUser = null;

searchInput.addEventListener('input', async function () {
    const query = this.value.trim();

    if (query.length === 0) {
        suggestionsBox.style.display = 'none';
        suggestionsBox.innerHTML = '';
        return;
    }

    try {
        const res = await fetch(`${BASE_PATH}user/search-usernames?query=${encodeURIComponent(query)}`);
        const users = await res.json(); // [{ username: "kiran", avatarUrl: "/..." }, ...]

        suggestionsBox.innerHTML = '';

        const filteredUsers = users.filter(u => u.username !== userName);

        if (filteredUsers.length > 0) {
            filteredUsers.forEach(user => {
                const item = document.createElement('div');
                item.classList.add('item');

                // Avatar
                const img = document.createElement('img');
                img.src = user.avatarUrl;
                img.classList.add('avatar');

                // Name
                const nameSpan = document.createElement('span');
                nameSpan.textContent = user.username;

                item.appendChild(img);
                item.appendChild(nameSpan);

                item.addEventListener('click', () => {
                    // Set search box value
                    searchInput.value = user.username;
                    suggestionsBox.style.display = 'none';

                    // Set existing link text
                    // Set link text
                    myJournalsLink.textContent = `${user.username} Journals`;

                    // Set active class to user link
                    allJournalsLink.classList.remove('active');
                    myJournalsLink.classList.add('active');
                    searchJournalInput.value = '';

                    // ✅ Call loadPublicJournals for that username
                    if (typeof loadPublicJournals === 'function') {
                        currentUser = user.username;
                        loadPublicJournals(0, user.username);
                    } else {
                        console.error("loadPublicJournals function not found in dashboard.js");
                    }
                });

                suggestionsBox.appendChild(item);
            });

            suggestionsBox.style.display = 'block';
        } else {
            suggestionsBox.style.display = 'none';
        }

    } catch (err) {
        console.error('Error fetching suggestions:', err);
    }



});


//  search journal  by  title
const searchJournalInput = document.getElementById('searchJournalInput');
const suggestionsBoxTitle = document.getElementById('suggestionsBoxTitle');


// Listen for typing in the search input
searchJournalInput.addEventListener('input', async function () {
    const query = this.value.trim();

    if (query.length === 0) {
        suggestionsBoxTitle.style.display = 'none';
        suggestionsBoxTitle.innerHTML = '';
        return;
    }

    try {
        // Call backend API to search journals by title
        const res = await fetch(`${BASE_PATH}user/search-title?query=${encodeURIComponent(query)}`);
        const journals = await res.json(); // [{ id: "...", title: "...", user: "username" }, ...]

        suggestionsBoxTitle.innerHTML = '';

        if (journals.length > 0)
        {
            journals.forEach(journal =>
            {
                const item = document.createElement('div');
                item.classList.add('item'); // Existing CSS will apply
                item.textContent = journal.title;

                item.addEventListener('click', () => {
                    suggestionsBoxTitle.style.display = 'none';
                    searchJournalInput.value = journal.title;

                    const container = document.getElementById('journalsContainer');
                    const pageNumbers = document.getElementById('pageNumbers');
                    const prevBtn = document.getElementById('prevPage');
                    const nextBtn = document.getElementById('nextPage');
                    container.innerHTML = `
                    <div class="loading-message">
                        <i class="fas fa-spinner fa-spin"></i>
                       <span>Loading journal...</span>
                    </div>`;

                    if (journal.id) {
                        const url = `${BASE_PATH}api/journals/id/${journal.id}`;
                        console.log("Fetching journal ID:", journal.id);

                        fetch(url)
                            .then(res => {
                                if (!res.ok) {
                                    if (res.status === 404) {
                                        container.innerHTML = `
                                       <div class="error-message">
                                       <i class="fas fa-exclamation-triangle"></i>
                                       <span>Journal not found</span>
                                        </div>`;
                                    } else {
                                        container.innerHTML = `
                                        <div class="error-message">
                                       <i class="fas fa-exclamation-triangle"></i>
                                       <span>Failed to load journal. Please try again.</span>
                                       </div>`;
                                    }
                                    return;
                                }
                                return res.json();
                            })
                            .then(data => {
                                if (!data) return;


                                container.innerHTML = ''; // clear loader

                                const journalCard = document.createElement('div');
                                journalCard.className = 'journal-card';
                                journalCard.innerHTML = `
                                <div class="journal-header">
                                 <div class="user-info">
                                 <img src="${data.avatarUrl || '/Journal/images/avatar.jpg'}" 
                                 class="avatar-sm" 
                                 alt="${data.username}" 
                                 onerror="this.src='/Journal/images/avatar.jpg'">
                            <span>${data.username}</span>
                        </div>
                        <div class="journal-date">
                            <i class="fas fa-globe journal-public"  id="worldIcon"></i>
                            ${data.createdAt.split(",")[0]}
                            <span class="journal-time">${new Date(data.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                        </div>
                        </div>
                        <h3 class="journal-title">${data.title || 'Untitled Journal'}</h3>
                         <p class="journal-content">${data.content}</p>
                            <div class="journal-footer">
                            <button class="read-more" 
                                data-id="${data.id}" 
                                data-full-content="${data.content.replace(/"/g, '&quot;')}">Read</button>
                        <div class="journal-actions">
                            <button class="journal-action like-btn" data-id="${data.id}">
                                <i class="far fa-heart"></i>
                                <span>Like</span>
                            </button>
                            <button class="journal-action comment-btn" data-id="${data.id}">
                                <i class="far fa-comment"></i>
                                <span>Comment</span>
                            </button>
                            <button class="journal-action share-btn" data-id="${data.id}">
                                <i class="fas fa-share"></i>
                                     <span>Share</span>
                                    </button>
                                        </div>
                                    </div>`;
                                container.appendChild(journalCard);
                                myJournalsLink.textContent = `${data.username} Journals`;
                                    })
                                    .catch(err => {
                                console.error('Error:', err);
                                container.innerHTML = `
                                 <div class="error-message">
                                    <i class="fas fa-exclamation-triangle"></i>
                                    <span>Failed to load journal. Please try again.</span>
                             </div>`;
                            });
                    } else {
                        alert(`Journal not found by title: ${journal.title}`);
                    }

                    // Set existing link text


                    // Set active class to user link
                    allJournalsLink.classList.remove('active');
                    myJournalsLink.classList.add('active');
                    searchInput.value='';

                    // Clear existing page numbers
                    pageNumbers.innerHTML = '';
                    prevBtn.disabled = true;
                    nextBtn.disabled = true;
                });
                suggestionsBoxTitle.appendChild(item);
            });
            suggestionsBoxTitle.style.display = 'block';
        } else {
            suggestionsBoxTitle.style.display = 'none';
        }
    } catch (err) {
        console.error('Error fetching journal suggestions:', err);
    }

});



// Clicking "All Journals" to go back
allJournalsLink.addEventListener('click', (e) => {
    e.preventDefault();

    // Set active class to all journals
    allJournalsLink.classList.add('active');
    myJournalsLink.classList.remove('active');
    // Remove the username journal text
    myJournalsLink.textContent = '';


    // Clear the search input
    searchInput.value = '';
    searchJournalInput.value = '';

    // Hide suggestions box if visible
    suggestionsBox.style.display = 'none';
    suggestionsBox.innerHTML = '';

    // Load all public journals
    loadPublicJournals(0); // no username => all
});



