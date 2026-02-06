document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('completeProfileForm');
    const submitBtn = document.getElementById('submitBtn');
    
    if (form) {
        // Form validation
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            
            if (validateForm()) {
                submitForm();
            }
        });
        
        // Real-time validation
        const inputs = form.querySelectorAll('input, select');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
            
            input.addEventListener('input', function() {
                clearFieldError(this);
            });
        });
        
        // Auto-focus first field
        const firstInput = form.querySelector('input, select');
        if (firstInput) {
            setTimeout(() => firstInput.focus(), 500);
        }
    }
    
    function validateForm() {
        let isValid = true;
        const fields = ['lastname', 'firstname', 'age', 'role'];
        
        fields.forEach(fieldName => {
            const field = document.getElementById(`id_${fieldName}`);
            if (field && !validateField(field)) {
                isValid = false;
            }
        });
        
        return isValid;
    }
    
    function validateField(field) {
        const value = field.value.trim();
        const fieldName = field.name;
        let isValid = true;
        
        clearFieldError(field);
        
        switch (fieldName) {
            case 'lastname':
                if (!value) {
                    showFieldError(field, 'Vui lòng nhập họ của bạn');
                    isValid = false;
                } else if (value.length < 2) {
                    showFieldError(field, 'Họ phải có ít nhất 2 ký tự');
                    isValid = false;
                }
                break;
                
            case 'firstname':
                if (!value) {
                    showFieldError(field, 'Vui lòng nhập tên của bạn');
                    isValid = false;
                } else if (value.length < 2) {
                    showFieldError(field, 'Tên phải có ít nhất 2 ký tự');
                    isValid = false;
                }
                break;
                
            case 'age':
                if (!value) {
                    showFieldError(field, 'Vui lòng nhập tuổi của bạn');
                    isValid = false;
                } else if (isNaN(value) || value < 1 || value > 120) {
                    showFieldError(field, 'Tuổi phải từ 1 đến 120');
                    isValid = false;
                }
                break;
                
            case 'role':
                if (!value) {
                    showFieldError(field, 'Vui lòng chọn vai trò của bạn');
                    isValid = false;
                }
                break;
        }
        
        return isValid;
    }
    
    function showFieldError(field, message) {
        // Remove existing error
        clearFieldError(field);
        
        // Add error class
        field.classList.add('is-invalid');
        
        // Create error message
        const errorDiv = document.createElement('div');
        errorDiv.className = 'invalid-feedback';
        errorDiv.textContent = message;
        
        // Insert error message after field
        field.parentNode.appendChild(errorDiv);
        
        // Scroll to error field
        field.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
    
    function clearFieldError(field) {
        field.classList.remove('is-invalid');
        const errorMessage = field.parentNode.querySelector('.invalid-feedback');
        if (errorMessage) {
            errorMessage.remove();
        }
    }
    
    function submitForm() {
        // Show loading state
        submitBtn.disabled = true;
        submitBtn.classList.add('btn-loading');
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý...';
        
        // Update progress bar
        updateProgress(100);
        
        // Submit form
        form.submit();
    }
    
    function updateProgress(percentage) {
        const progressBar = document.querySelector('.progress-bar::before');
        if (progressBar) {
            progressBar.style.width = percentage + '%';
        }
    }
    
    // Add smooth animations for form fields
    const formFields = document.querySelectorAll('.form-floating');
    formFields.forEach((field, index) => {
        field.style.opacity = '0';
        field.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            field.style.transition = 'all 0.5s ease';
            field.style.opacity = '1';
            field.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Add focus effects
    const formInputs = document.querySelectorAll('.form-control');
    formInputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentNode.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            this.parentNode.classList.remove('focused');
        });
    });
    
    // Auto-advance to next field on Enter
    formInputs.forEach((input, index) => {
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const nextInput = formInputs[index + 1];
                if (nextInput) {
                    nextInput.focus();
                } else {
                    // If it's the last field, submit the form
                    if (validateForm()) {
                        submitForm();
                    }
                }
            }
        });
    });
    
    // Show success message if redirected from registration
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('registration') === 'success') {
        showSuccessMessage('Đăng ký thành công! Vui lòng hoàn thiện thông tin cá nhân.');
    }
    
    function showSuccessMessage(message) {
        const alertDiv = document.createElement('div');
        alertDiv.className = 'alert alert-success';
        alertDiv.innerHTML = `
            <i class="fas fa-check-circle me-2"></i>
            ${message}
        `;
        
        const form = document.getElementById('completeProfileForm');
        form.insertBefore(alertDiv, form.firstChild);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    }
}); 