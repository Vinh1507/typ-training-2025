// Smooth scroll for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Add loading states to forms
document.querySelectorAll('form').forEach(form => {
    form.addEventListener('submit', function() {
        const submitBtn = this.querySelector('button[type="submit"]');
        if (submitBtn) {
            submitBtn.classList.add('loading');
            submitBtn.disabled = true;
        }
    });
});

// Lazy loading for images
if ('IntersectionObserver' in window) {
    const imageObserver = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                if (img.dataset && img.dataset.src) {
                    img.src = img.dataset.src;
                }
                img.classList.remove('lazy');
                observer.unobserve(img);
            }
        });
    });

    document.querySelectorAll('img[data-src]').forEach(img => {
        imageObserver.observe(img);
    });
}

// Inject current user id for favorites links and like buttons
(function enhanceUserContext(){
    document.addEventListener('DOMContentLoaded', async function(){
        try{
            const res = await fetch('/api/v1/auth/me', { credentials: 'same-origin' });
            if(!res.ok) return; // not logged in
            const user = await res.json();
            if(!user || !user.userId) return;
            window.CURRENT_USER_ID = user.userId;
            // Rewrite favorites links to append userId
            document.querySelectorAll('a[href="/user/favorites"]').forEach(function(a){
                try{
                    const url = new URL(a.getAttribute('href'), window.location.origin);
                    url.searchParams.set('userId', user.userId);
                    a.setAttribute('href', url.pathname + url.search);
                } catch(e) { /* ignore */ }
            });
            // Rewrite profile links to append userId
            document.querySelectorAll('a[href="/user/profile"]').forEach(function(a){
                try{
                    const url = new URL(a.getAttribute('href'), window.location.origin);
                    url.searchParams.set('userId', user.userId);
                    a.setAttribute('href', url.pathname + url.search);
                } catch(e) { /* ignore */ }
            });
            // Enrich like buttons missing data-user-id
            document.querySelectorAll('.like-btn[data-document-id]').forEach(function(btn){
                btn.setAttribute('data-user-id', String(user.userId));
            });
        } catch(err){
            // ignore if cannot fetch current user
            console.debug('No authenticated user context found');
        }
    });
})();
