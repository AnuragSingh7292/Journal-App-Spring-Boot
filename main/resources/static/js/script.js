document.addEventListener('DOMContentLoaded', function() {
    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();

            const targetId = this.getAttribute('href');
            if (targetId === '#') return;

            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 80,
                    behavior: 'smooth'
                });
            }
        });
    });
    // Sticky header on scroll
    const header = document.querySelector('.header');
    if (header) {
        window.addEventListener('scroll', function() {
            if (window.scrollY > 100) {
                header.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
            } else {
                header.style.boxShadow = 'none';
            }
        });
    }

    // Mobile menu toggle (would need HTML elements for mobile menu)
    // This is a placeholder for future mobile menu implementation
    // const mobileMenuButton = document.querySelector('.mobile-menu-button');
    // const mobileMenu = document.querySelector('.mobile-menu');
    // if (mobileMenuButton && mobileMenu) {
    //     mobileMenuButton.addEventListener('click', function() {
    //         mobileMenu.classList.toggle('active');
    //     });
    // }

    // Animation on scroll
    // This would require an animation library or custom implementation
    // For a complete solution, you might want to use AOS.js or similar

    // Form submission handling
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            // Here you would typically handle form submission via AJAX
            // For now, we'll just show an alert
            alert('Thank you for your submission!');
            form.reset();
        });
    });
});