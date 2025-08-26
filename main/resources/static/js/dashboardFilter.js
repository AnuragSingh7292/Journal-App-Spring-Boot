const sortSelect = document.getElementById('sortSelect');
currentSortOrder = 1; // default: newest first

sortSelect.addEventListener('change', () => {
    currentSortOrder = (sortSelect.value === 'newest') ? 1 : 2;
    console.log(currentUser,currentSortOrder);

    // Reload journals from first page with the selected sort order
    loadPublicJournals(0, currentUser, currentSortOrder);
});

const clearAllBtn = document.querySelector('.section-clear');
// Handle "Clear All" click
clearAllBtn.addEventListener('click', () => {
    // Reset sort dropdown to default
    sortSelect.value = 'newest';
    currentSortOrder = 1;

    // Reload journals from first page with default values
    loadPublicJournals(0, currentUser, currentSortOrder);
});
