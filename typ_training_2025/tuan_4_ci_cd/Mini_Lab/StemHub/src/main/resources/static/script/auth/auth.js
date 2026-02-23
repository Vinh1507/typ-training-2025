document.addEventListener('DOMContentLoaded', function() {
    // Form validation feedback
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            if (submitBtn && this.checkValidity()) {
                submitBtn.classList.add('btn-loading');
                submitBtn.innerHTML = '<span style="opacity: 0;">Loading...</span>';
            }
        });
    });

    // Enhanced form interactions
    const inputs = document.querySelectorAll('.form-control');
    inputs.forEach(input => {
        // Focus effects
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            this.parentElement.classList.remove('focused');
        });

        // Real-time validation feedback
        input.addEventListener('input', function() {
            if (this.checkValidity()) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            } else {
                this.classList.remove('is-valid');
                this.classList.add('is-invalid');
            }
        });
    });

    // Smooth page transitions
    const links = document.querySelectorAll('a[href]');
    links.forEach(link => {
        link.addEventListener('click', function(e) {
            if (this.href && !this.href.startsWith('#') && !this.target) {
                e.preventDefault();
                document.body.style.opacity = '0.7';
                setTimeout(() => {
                    window.location.href = this.href;
                }, 200);
            }
        });
    });

    // Auto-dismiss alerts
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        if (!alert.querySelector('.btn-close')) {
            setTimeout(() => {
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 300);
            }, 5000);
        }
    });
});

// Password strength indicator (will be used in register page)
function checkPasswordStrength(password) {
    let strength = 0;
    const checks = [
        /.{8,}/, // At least 8 characters
        /[a-z]/, // Lowercase
        /[A-Z]/, // Uppercase  
        /[0-9]/, // Numbers
        /[^A-Za-z0-9]/ // Special characters
    ];
    
    checks.forEach(check => {
        if (check.test(password)) strength++;
    });
    
    return strength;
}
