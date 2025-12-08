document.addEventListener('DOMContentLoaded', function() {
    const backToTopBtn = document.getElementById('backToTop');
    const newsletterForm = document.querySelector('.newsletter-form');
    
    // Back to top functionality (guard when element is absent)
    window.addEventListener('scroll', function() {
        if (!backToTopBtn) return;
        if (window.scrollY > 300) {
            backToTopBtn.classList.add('show');
        } else {
            backToTopBtn.classList.remove('show');
        }
    });
    
    if (backToTopBtn) {
        backToTopBtn.addEventListener('click', function() {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        });
    }

    // Newsletter form
    if (newsletterForm) {
        newsletterForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = this.querySelector('input[type="email"]').value;
            
            if (email) {
                // Show success message
                const btn = this.querySelector('button');
                const originalContent = btn.innerHTML;
                
                btn.innerHTML = '<i class="fas fa-check"></i>';
                btn.classList.remove('btn-primary');
                btn.classList.add('btn-success');
                
                setTimeout(() => {
                    btn.innerHTML = originalContent;
                    btn.classList.remove('btn-success');
                    btn.classList.add('btn-primary');
                    this.reset();
                }, 2000);
            }
        });
    }
    
    // Animate stats on scroll
    const observerOptions = {
        threshold: 0.5,
        triggerOnce: true
    };
    
    const statsObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const numbers = entry.target.querySelectorAll('.stat-number');
                numbers.forEach(number => {
                    const value = parseInt(number.textContent);
                    animateNumber(number, 0, value, 2000);
                });
            }
        });
    }, observerOptions);
    
    const statsSection = document.querySelector('.footer-stats');
    if (statsSection) {
        statsObserver.observe(statsSection);
    }
    
    // Animate number function
    function animateNumber(element, start, end, duration) {
        const startTime = performance.now();
        const suffix = element.textContent.replace(/[0-9]/g, '');
        
        function updateNumber(currentTime) {
            const elapsed = currentTime - startTime;
            const progress = Math.min(elapsed / duration, 1);
            const current = Math.floor(start + (end - start) * progress);
            
            element.textContent = current + suffix;
            
            if (progress < 1) {
                requestAnimationFrame(updateNumber);
            }
        }
        
        requestAnimationFrame(updateNumber);
    }
});