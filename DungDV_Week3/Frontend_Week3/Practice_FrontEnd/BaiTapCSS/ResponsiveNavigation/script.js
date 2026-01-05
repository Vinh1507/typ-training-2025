
document.addEventListener('DOMContentLoaded', function() {
    // Smooth scroll cho các link navigation
    const navLinks = document.querySelectorAll('.nav-menu a');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            const href = this.getAttribute('href');
            if (href.startsWith('#')) {
                e.preventDefault();
                const targetId = href.substring(1);
                const targetElement = document.getElementById(targetId);
                if (targetElement) {
                    targetElement.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            }
        });
    });
    
    // Active link khi scroll
    const sections = document.querySelectorAll('section[id]');
    const navItems = document.querySelectorAll('.nav-menu a');
    
    window.addEventListener('scroll', function() {
        let current = '';
        sections.forEach(section => {
            const sectionTop = section.offsetTop;
            const sectionHeight = section.clientHeight;
            if (window.pageYOffset >= sectionTop - 200) {
                current = section.getAttribute('id');
            }
        });
        
        navItems.forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('href') === `#${current}`) {
                item.classList.add('active');
            }
        });
    });
    
    // Đóng menu khi click bên ngoài (cho mobile)
    document.addEventListener('click', function(e) {
        const menuToggle = document.querySelectorAll('#menu-toggle');
        const navMenu = document.querySelectorAll('.nav-menu');
        
        menuToggle.forEach((toggle, index) => {
            if (toggle && !toggle.contains(e.target) && 
                navMenu[index] && !navMenu[index].contains(e.target)) {
                toggle.checked = false;
            }
        });
    });
});

