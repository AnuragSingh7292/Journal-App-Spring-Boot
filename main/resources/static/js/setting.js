//
//     // Show/hide settings sections
//     function showSection(sectionId) {
//     // Hide all sections
//     document.querySelectorAll('.settings-section').forEach(section => {
//         section.classList.remove('active');
//     });
//
//     // Show selected section
//     document.getElementById(sectionId).classList.add('active');
//
//     // Update active menu item
//     document.querySelectorAll('.settings-menu a').forEach(link => {
//     link.classList.remove('active');
// });
//     document.querySelector(`.settings-menu a[href="#${sectionId}"]`).classList.add('active');
// }
//
//     // Toggle password visibility
//     function togglePassword(inputId) {
//     const input = document.getElementById(inputId);
//     const button = input.nextElementSibling;
//     const icon = button.querySelector('i');
//
//     if (input.type === 'password') {
//     input.type = 'text';
//     icon.classList.remove('fa-eye');
//     icon.classList.add('fa-eye-slash');
// } else {
//     input.type = 'password';
//     icon.classList.remove('fa-eye-slash');
//     icon.classList.add('fa-eye');
// }
// }
//
//     // Password validation
//     document.getElementById('changePasswordForm').addEventListener('submit', function(e) {
//     const newPassword = document.getElementById('newPassword').value;
//     const confirmPassword = document.getElementById('confirmPassword').value;
//
//     if (newPassword !== confirmPassword) {
//     e.preventDefault();
//     alert('New password and confirmation password do not match.');
//     return;
// }
//
//     // Additional password strength validation can be added here
// });
//
//     // Initialize tooltips
//     const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
//     tooltipTriggerList.map(function (tooltipTriggerEl) {
//     return new bootstrap.Tooltip(tooltipTriggerEl);
// });
//
//     // Delete account confirmation
//     document.getElementById('confirmDelete').addEventListener('click', function() {
//     const confirmText = document.getElementById('deleteConfirm').value;
//     if (confirmText === 'DELETE') {
//     // Proceed with account deletion
//     alert('Account deletion process initiated.');
//     // Here you would typically make an API call to delete the account
// } else {
//     alert('Please type "DELETE" to confirm account deletion.');
// }
// });