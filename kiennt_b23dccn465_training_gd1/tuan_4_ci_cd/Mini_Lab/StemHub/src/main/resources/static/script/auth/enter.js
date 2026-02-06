document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('loginForm');
    if (!form) return;

    // If form login (server-side session via Spring Security), do not override submit
    // Only attach AJAX login when action points to API endpoint
    const actionUrl = form.getAttribute('action') || form.action || '';
    const isApiLogin = actionUrl.includes('/api/');
    if (!isApiLogin) {
        // Optional: basic client-side validation styles without preventing submit
        const inputs = form.querySelectorAll('.form-control');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                if (this.value.trim()) {
                    this.classList.remove('is-invalid');
                    this.classList.add('is-valid');
                }
            });
        });
        return; // Let default form submission happen
    }

    const submitBtn = form.querySelector('button[type="submit"]');
    
    // Enhanced form validation + AJAX login for API mode only
    form.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        const username = document.getElementById('username');
        const password = document.getElementById('password');
        let isValid = true;
        
        // Reset validation
        [username, password].forEach(input => {
            input.classList.remove('is-valid', 'is-invalid');
        });
        
        // Validate username
        if (!username.value.trim()) {
            username.classList.add('is-invalid');
            isValid = false;
        } else {
            username.classList.add('is-valid');
        }
        
        // Validate password
        if (!password.value) {
            password.classList.add('is-invalid');
            isValid = false;
        } else {
            password.classList.add('is-valid');
        }
        
        if (isValid) {
            // Show loading state
            submitBtn.disabled = true;
            submitBtn.classList.add('btn-loading');
            submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Đang đăng nhập...';

            try {
                // Call login API
                const response = await fetch('/api/v1/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        username: username.value,
                        password: password.value
                    })
                });

                const data = await response.json();

                if (response.ok) {
                    // Save token to localStorage
                    localStorage.setItem('token', data.token);

                    // Save remember me
                    const rememberMe = document.getElementById('rememberMe');
                    if (rememberMe && rememberMe.checked) {
                        localStorage.setItem('remembered_username', username.value);
                    } else {
                        localStorage.removeItem('remembered_username');
                    }

                    // Show success message
                    submitBtn.innerHTML = '<i class="fas fa-check me-2"></i>Đăng nhập thành công!';
                    submitBtn.classList.remove('btn-loading');
                    submitBtn.classList.add('btn-success');

                    // Redirect to home page
                    setTimeout(() => {
                        window.location.href = '/';
                    }, 1000);
                } else {
                    // Show error message
                    showError(data.message || 'Username hoặc password không đúng');
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = '<i class="fas fa-sign-in-alt me-2"></i>Đăng nhập';
                    submitBtn.classList.remove('btn-loading');
                }
            } catch (error) {
                console.error('Login error:', error);
                showError('Có lỗi xảy ra. Vui lòng thử lại sau.');
                submitBtn.disabled = false;
                submitBtn.innerHTML = '<i class="fas fa-sign-in-alt me-2"></i>Đăng nhập';
                submitBtn.classList.remove('btn-loading');
            }
        } else {
            // Shake animation for invalid form
            form.style.animation = 'shake 0.5s ease-in-out';
            setTimeout(() => {
                form.style.animation = '';
            }, 500);
        }
    });
    
    // Show error message function
    function showError(message) {
        // Remove existing alerts
        const existingAlert = form.querySelector('.alert');
        if (existingAlert) {
            existingAlert.remove();
        }

        // Create new alert
        const alert = document.createElement('div');
        alert.className = 'alert alert-danger';
        alert.role = 'alert';
        alert.innerHTML = `<i class="fas fa-exclamation-triangle me-2"></i>${message}`;

        // Insert at the beginning of form
        form.insertBefore(alert, form.firstChild);

        // Auto dismiss after 5 seconds
        setTimeout(() => {
            alert.remove();
        }, 5000);
    }

    // Real-time validation
    const inputs = form.querySelectorAll('.form-control');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.value.trim()) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            }
        });
        
        input.addEventListener('input', function() {
            if (this.classList.contains('is-invalid') && this.value.trim()) {
                this.classList.remove('is-invalid');
                this.classList.add('is-valid');
            }
        });
    });
    
    // Remember me functionality (API mode only)
    const rememberMe = document.getElementById('rememberMe');
    const username = document.getElementById('username');
    
    if (rememberMe && username && localStorage.getItem('remembered_username')) {
        username.value = localStorage.getItem('remembered_username');
        rememberMe.checked = true;
    }
});

// Password visibility toggle
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(inputId + 'ToggleIcon');
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Social login placeholder functions
function socialLogin(provider) {
    alert(`Đăng nhập với ${provider} sẽ được triển khai sớm!`);
}

// Shake animation CSS
const shakeCSS = `
@keyframes shake {
    0%, 100% { transform: translateX(0); }
    10%, 30%, 50%, 70%, 90% { transform: translateX(-5px); }
    20%, 40%, 60%, 80% { transform: translateX(5px); }
}
`;

// Add shake animation to page
const style = document.createElement('style');
style.textContent = shakeCSS;
document.head.appendChild(style);