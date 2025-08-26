document.addEventListener('DOMContentLoaded', function() {
    // DOM Elements - only keeping user-related elements
    const usernameElement = document.querySelector('.username');
    const userAvatarElement = document.querySelector('.user-avatar');
    const pageNumbers = document.getElementById('pageNumbers');
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    let loggedInUser = null;
    const JOURNALS_PER_PAGE = 10;
    let currentPage = 0;

    userName = '';
    // Initialize dashboard
    initDashboard();

    function initDashboard() {
        loadUserData();  // this  will  add the all logged user  info
        loadPublicJournals(0); // You'll add  the all public journal  of other user like  feed
    }

    // fetch user  details
    function loadUserData() {
        fetch(`${BASE_PATH}user/me`)
            .then(response => {
                if (!response.ok) throw new Error("Failed to fetch user data");
                return response.json();
            })
            .then(user => {
                loggedInUser = user;
                displayUserData(user);
            })
            .catch(error => {
                console.error("Error loading user data:", error);
                // Show default/fallback data
                displayUserData({ username: 'User', avatarUrl: '/default-avatar.jpg' });
            });
    }
    // its showing all fetch  user details on display
    function displayUserData(user) {
        // Update username
        if (usernameElement) {
            usernameElement.textContent = user.username || 'User';
            userName = user.username;
        }

        // Update avatar
        if (userAvatarElement) {
            userAvatarElement.src = user.avatarUrl || '/Journal/default-avatar.jpg';
            userAvatarElement.onerror = function() {
                this.src = 'Journal/default-avatar.jpg';
            }
        }
    }



    function loadPublicJournals(page = 0,  username = null, sortOrder = 1) {
        const container = document.getElementById('journalsContainer');
        container.innerHTML = `
      <div class="loading-message">
        <i class="fas fa-spinner fa-spin"></i>
        <span>Loading journals...</span>
      </div>
    `;
        let url;
        if (username) {
            // Fetch by username
            url = `${BASE_PATH}api/journals/public/by-user?username=${encodeURIComponent(username)}&page=${page}&size=${JOURNALS_PER_PAGE}&sortOrder=${sortOrder}`;
        } else {
            // Fetch all public
            url = `${BASE_PATH}api/journals/public?page=${page}&size=${JOURNALS_PER_PAGE}&sortOrder=${sortOrder}`;
        }
        fetch(url)
            .then(res => {
                if (!res.ok) {
                    if (res.status === 404) {
                        container.innerHTML = `<div class="error-message">
                    <i class="fas fa-exclamation-triangle"></i>
                    <span>User not found</span>
                </div>`;
                    } else {
                        container.innerHTML = `<div class="error-message">
                    <i class="fas fa-exclamation-triangle"></i>
                    <span>Failed to load journals. Please try again.</span>
                </div>`;
                    }
                    return;
                }
                return res.json();
            })
            .then(data => {
                const container = document.getElementById('journalsContainer');
                const pagination = document.getElementById('pageNumbers');

                // Clear existing content
                container.innerHTML = '';
                pagination.innerHTML = ``;

                if (data.content.length === 0) {
                    container.innerHTML = `
                    <div class="empty-state">
                        <i class="fas fa-book-open"></i>
                        <p>No public journals found</p>
                    </div>
                `;
                    prevBtn.disabled = true;
                    nextBtn.disabled = true;
                    return;
                }

                // Render journals
                data.content.forEach(journal => {
                    console.log(journal);
                    const journalCard = document.createElement('div');
                    journalCard.className = 'journal-card';
                    journalCard.innerHTML = `
                    <div class="journal-header">
                        <div class="user-info">
                            <img src="${journal.avatarUrl || '/Journal/images/avatar.jpg'}" 
                                 class="avatar-sm" 
                                 alt="${journal.username}" 
                                 onerror="this.src='/Journal/images/avatar.jpg'">
                            <span>${journal.username}</span>
                        </div>
                        <div class="journal-date">
                            <i class="fas fa-globe journal-public"  id="worldIcon"></i>
                            ${journal.createdAt.split(",")[0]}
                            <span class="journal-time">${new Date(journal.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                        </div>
                    </div>
                    <h3 class="journal-title">${journal.title || 'Untitled Journal'}</h3>
                    <p class="journal-content">
                        ${journal.content.length > 200 ?
                        journal.content.substring(0, 200) + '...' :
                        journal.content}
                    </p>
                    <div class="journal-footer">
                        <button class="read-more" 
                                        data-id="${journal.id}" 
                                            data-full-content="${journal.content.replace(/"/g, '&quot;')}">Read</button>

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
                    </div>
                `;
                    container.appendChild(journalCard);
                });

                // Render pagination
                renderPagination(data.totalPages, page);
                currentPage = page; // Update current page
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



    function renderPagination(totalPages, currentPage) {

        // Clear existing page numbers
        pageNumbers.innerHTML = '';

        // Set button states
        prevBtn.disabled = currentPage <= 0;
        nextBtn.disabled = currentPage >= totalPages - 1;

        // Create page number buttons
        for (let i = 0; i < totalPages; i++) {
            const pageBtn = document.createElement('button');
            pageBtn.className = `page-number ${i === currentPage ? 'active' : ''}`;
            pageBtn.textContent = i + 1;
            pageBtn.addEventListener('click', () => loadPublicJournals(i,currentUser,currentSortOrder));
            pageNumbers.appendChild(pageBtn);
        }
    }
    // Initialize event listeners
    document.getElementById('prevPage').addEventListener('click', () => {
        if (currentPage > 0) {
            loadPublicJournals(currentPage - 1, currentUser,currentSortOrder);
        }
    });

    document.getElementById('nextPage').addEventListener('click', () => {
        loadPublicJournals(currentPage + 1, currentUser,currentSortOrder);
    });


/* overly  html will comes  here */
    const container = document.getElementById('journalsContainer');
    const overlayContainer = document.getElementById('overlayContainer');
    const overlayCardContainer = document.getElementById('overlayCardContainer');
    const overlayCloseBtn = document.getElementById('overlayCloseBtn');



    container.addEventListener('click', e => {
        if(e.target.classList.contains('read-more')) {
            const journalCard = e.target.closest('.journal-card');
            if(!journalCard) return;

            // Clear previous content
            overlayCardContainer.querySelectorAll(':not(#overlayCloseBtn)').forEach(el => el.remove());

            // Clone the clicked card
            const cloneCard = journalCard.cloneNode(true);

            // Replace truncated content with full content from a data attribute
            // (You need to add full content as data attribute on Read button when rendering cards)

            const readBtn = journalCard.querySelector('.read-more');
            const fullContent = readBtn.getAttribute('data-full-content');
            if(fullContent) {
                const contentP = cloneCard.querySelector('.journal-content');
                if(contentP) contentP.textContent = fullContent;  // replace with full content
            }

            // Style the cloned card
            cloneCard.style.margin = '30px';
            cloneCard.style.maxWidth = '600px';  // fixed width good
            cloneCard.style.height = 'auto';  // height adjusts to content
            cloneCard.style.overflowY = 'visible';  // allow full content display

            const contentP = cloneCard.querySelector('.journal-content');
            if(contentP) {
                contentP.innerHTML = fullContent;

                // Override CSS styles to remove truncation
                contentP.style.display = 'block';       // remove -webkit-box
                contentP.style.webkitLineClamp = 'unset';  // remove line clamp
                contentP.style.webkitBoxOrient = 'unset';  // remove box orient
                contentP.style.overflow = 'visible';    // show full content
                contentP.style.whiteSpace = 'pre-wrap'; // preserve line breaks
            }



            // Append clone inside overlay container
            overlayCardContainer.appendChild(cloneCard);

            // Show overlay
            overlayContainer.style.display = 'flex';


            // Close overlay on clicking X
            overlayCloseBtn.addEventListener('click', () => {
                overlayContainer.style.display = 'none';
            });
        }


    });


    // ✅ Make sure other files can access it
    window.loadPublicJournals = loadPublicJournals;

});