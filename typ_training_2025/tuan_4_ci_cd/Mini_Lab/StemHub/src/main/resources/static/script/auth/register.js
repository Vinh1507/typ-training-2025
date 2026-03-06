document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('registerForm');
    const submitBtn = document.getElementById('submitBtn');
    const progressBar = document.querySelector('.progress-bar::after');
    
    // Form validation state
    let formState = {
        username: false,
        email: false,
        password1: false,
        password2: false,
        terms: false
    };
    
    // Update form progress
    function updateProgress() {
        const completed = Object.values(formState).filter(Boolean).length;
        const total = Object.keys(formState).length;
        const percentage = (completed / total) * 33.33; // Chỉ 1/3 của toàn bộ quá trình
        
        document.querySelector('.progress-bar').style.setProperty('--progress', percentage + '%');
        
        // Update submit button
        const allValid = Object.values(formState).every(Boolean);
        submitBtn.disabled = !allValid;
        
        if (allValid) {
            submitBtn.classList.add('btn-ready');
        } else {
            submitBtn.classList.remove('btn-ready');
        }
    }
    
    // Add CSS for progress
    const style = document.createElement('style');
    style.textContent = `
        .progress-bar::after { width: var(--progress, 0%); }
        .btn-ready { 
            background: var(--success-gradient) !important; 
            animation: pulse 2s infinite;
        }
        @keyframes pulse {
            0% { box-shadow: 0 4px 15px rgba(25, 135, 84, 0.3); }
            50% { box-shadow: 0 8px 25px rgba(25, 135, 84, 0.5); }
            100% { box-shadow: 0 4px 15px rgba(25, 135, 84, 0.3); }
        }
    `;
    document.head.appendChild(style);
    
    // Username validation
    const usernameInput = document.getElementById('id_username');
    usernameInput.addEventListener('input', function() {
        const value = this.value;
        const isValid = /^[a-zA-Z0-9_]{3,30}$/.test(value);
        const validation = document.getElementById('usernameValidation');
        
        if (value.length === 0) {
            this.classList.remove('is-valid', 'is-invalid');
            validation.innerHTML = '';
            formState.username = false;
        } else if (isValid) {
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
            validation.innerHTML = '<i class="fas fa-check"></i> Tên tài khoản hợp lệ';
            validation.className = 'username-validation valid';
            formState.username = true;
        } else {
            this.classList.remove('is-valid');
            this.classList.add('is-invalid');
            
            // Specific error messages
            let errorMessage = '';
            if (value.length < 3) {
                errorMessage = '<i class="fas fa-times"></i> Tên tài khoản phải có ít nhất 3 ký tự';
            } else if (value.length > 30) {
                errorMessage = '<i class="fas fa-times"></i> Tên tài khoản không được quá 30 ký tự';
            } else if (!/^[a-zA-Z0-9_]+$/.test(value)) {
                errorMessage = '<i class="fas fa-times"></i> Tên tài khoản chỉ được chứa chữ cái, số và dấu gạch dưới';
            } else {
                errorMessage = '<i class="fas fa-times"></i> Tên tài khoản không hợp lệ';
            }
            
            validation.innerHTML = errorMessage;
            validation.className = 'username-validation invalid';
            formState.username = false;
        }
        
        updateProgress();
    });
    
    // Email validation
    const emailInput = document.getElementById('id_email');
    emailInput.addEventListener('input', function() {
        const value = this.value;
        const isValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
        const verification = document.getElementById('emailVerification');
        
        if (value.length === 0) {
            this.classList.remove('is-valid', 'is-invalid');
            verification.innerHTML = '';
            formState.email = false;
        } else if (isValid) {
            this.classList.remove('is-invalid');
            this.classList.add('is-valid');
            verification.innerHTML = '<i class="fas fa-check"></i> Email hợp lệ';
            verification.className = 'email-verification valid';
            formState.email = true;
        } else {
            this.classList.remove('is-valid');
            this.classList.add('is-invalid');
            verification.innerHTML = '<i class="fas fa-times"></i> Email không hợp lệ';
            verification.className = 'email-verification invalid';
            formState.email = false;
        }
        
        updateProgress();
    });
    
    // Password strength validation
    const password1Input = document.getElementById('id_password1');
    password1Input.addEventListener('input', function() {
        const password = this.value;
        updatePasswordStrength(password);
        checkPasswordMatch();
    });
    
    // Confirm password validation
    const password2Input = document.getElementById('id_password2');
    password2Input.addEventListener('input', function() {
        checkPasswordMatch();
    });
    
    // Terms checkbox
    const termsCheckbox = document.getElementById('agreeTerms');
    termsCheckbox.addEventListener('change', function() {
        formState.terms = this.checked;
        updateProgress();
    });
    
    function updatePasswordStrength(password) {
        const requirements = {
            length: /.{8,}/,
            lower: /[a-z]/,
            upper: /[A-Z]/,
            number: /[0-9]/,
            special: /[^A-Za-z0-9]/
        };
        
        let strength = 0;
        Object.entries(requirements).forEach(([key, regex]) => {
            const element = document.getElementById(key + 'Req');
            if (regex.test(password)) {
                element.classList.add('met');
                element.querySelector('i').className = 'fas fa-check text-success';
                strength++;
            } else {
                element.classList.remove('met');
                element.querySelector('i').className = 'fas fa-times text-danger';
            }
        });
        
        // Update strength bar and text
        const strengthProgress = document.getElementById('strengthProgress');
        const strengthText = document.getElementById('strengthText');
        
        const levels = ['weak', 'fair', 'good', 'strong', 'very-strong'];
        const texts = ['Rất yếu', 'Yếu', 'Trung bình', 'Mạnh', 'Rất mạnh'];
        
        strengthProgress.className = 'strength-progress ' + (levels[strength - 1] || '');
        strengthText.textContent = password.length > 0 ? texts[strength - 1] || 'Rất yếu' : 'Nhập mật khẩu để kiểm tra độ mạnh';
        
        // Update form state
        formState.password1 = strength >= 3;
        if (formState.password1) {
            password1Input.classList.add('is-valid');
            password1Input.classList.remove('is-invalid');
        } else if (password.length > 0) {
            password1Input.classList.add('is-invalid');
            password1Input.classList.remove('is-valid');
        }
        
        updateProgress();
    }
    
    function checkPasswordMatch() {
        const password1 = password1Input.value;
        const password2 = password2Input.value;
        const matchDiv = document.getElementById('passwordMatch');
        
        if (password2.length === 0) {
            matchDiv.innerHTML = '';
            password2Input.classList.remove('is-valid', 'is-invalid');
            formState.password2 = false;
        } else if (password1 === password2 && formState.password1) {
            matchDiv.innerHTML = '<i class="fas fa-check"></i> Mật khẩu khớp';
            matchDiv.className = 'password-match match';
            password2Input.classList.add('is-valid');
            password2Input.classList.remove('is-invalid');
            formState.password2 = true;
        } else {
            matchDiv.innerHTML = '<i class="fas fa-times"></i> Mật khẩu không khớp';
            matchDiv.className = 'password-match no-match';
            password2Input.classList.add('is-invalid');
            password2Input.classList.remove('is-valid');
            formState.password2 = false;
        }
        
        updateProgress();
    }
    
    // Form submission
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        
        if (Object.values(formState).every(Boolean)) {
            submitBtn.classList.add('btn-loading');
            submitBtn.innerHTML = '<span><i class="fas fa-user-plus me-2"></i>Tạo tài khoản</span>';
            
            setTimeout(() => {
                form.submit();
            }, 1000);
        }
    });
    
    // Initialize progress
    updateProgress();
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

// Social registration
function socialRegister(provider) {
    alert(`Đăng ký với ${provider} sẽ được triển khai sớm!`);
}

// Terms modal functions
function acceptTerms() {
    document.getElementById('agreeTerms').checked = true;
    document.getElementById('agreeTerms').dispatchEvent(new Event('change'));
    bootstrap.Modal.getInstance(document.getElementById('termsModal')).hide();
}

function acceptPrivacy() {
    bootstrap.Modal.getInstance(document.getElementById('privacyModal')).hide();
}