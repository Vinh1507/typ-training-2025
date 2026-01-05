
document.addEventListener('DOMContentLoaded', function() {
    // Smooth scroll cho navigation links
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
    
    // Form validation và xử lý submit
    const contactForm = document.querySelector('#contact form');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            // Lấy giá trị form
            const name = document.getElementById('name').value.trim();
            const email = document.getElementById('email').value.trim();
            const phone = document.getElementById('phone').value.trim();
            const subject = document.getElementById('subject').value;
            const message = document.getElementById('message').value.trim();
            
            // Validation
            if (!validateForm(name, email, subject, message)) {
                return;
            }
            
            // Hiển thị thông báo thành công
            showNotification('Cảm ơn bạn đã liên hệ! Tôi sẽ phản hồi sớm nhất có thể.', 'success');
            
            // Reset form
            contactForm.reset();
        });
        
        // Real-time validation
        const emailInput = document.getElementById('email');
        emailInput.addEventListener('blur', function() {
            if (this.value && !isValidEmail(this.value)) {
                this.style.borderColor = 'red';
                showFieldError(this, 'Email không hợp lệ');
            } else {
                this.style.borderColor = '';
                removeFieldError(this);
            }
        });
        
        const phoneInput = document.getElementById('phone');
        phoneInput.addEventListener('blur', function() {
            if (this.value && !isValidPhone(this.value)) {
                this.style.borderColor = 'red';
                showFieldError(this, 'Số điện thoại không hợp lệ');
            } else {
                this.style.borderColor = '';
                removeFieldError(this);
            }
        });
    }
    
    // Animation khi scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Áp dụng animation cho các section
    const sections = document.querySelectorAll('section');
    sections.forEach(section => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        section.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(section);
    });
});

// Hàm validate form
function validateForm(name, email, subject, message) {
    if (!name) {
        showNotification('Vui lòng nhập họ và tên', 'error');
        return false;
    }
    
    if (!email || !isValidEmail(email)) {
        showNotification('Vui lòng nhập email hợp lệ', 'error');
        return false;
    }
    
    if (!subject) {
        showNotification('Vui lòng chọn chủ đề', 'error');
        return false;
    }
    
    if (!message || message.length < 10) {
        showNotification('Vui lòng nhập nội dung (ít nhất 10 ký tự)', 'error');
        return false;
    }
    
    return true;
}

// Kiểm tra email hợp lệ
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Kiểm tra số điện thoại hợp lệ
function isValidPhone(phone) {
    const phoneRegex = /^[0-9]{10,11}$/;
    return phoneRegex.test(phone.replace(/\s/g, ''));
}

// Hiển thị thông báo
function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        background: ${type === 'success' ? '#4CAF50' : '#f44336'};
        color: white;
        border-radius: 5px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        z-index: 1000;
        animation: slideIn 0.3s ease;
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(notification);
        }, 300);
    }, 3000);
}

// Hiển thị lỗi cho field
function showFieldError(field, message) {
    removeFieldError(field);
    const error = document.createElement('span');
    error.className = 'field-error';
    error.textContent = message;
    error.style.cssText = 'color: red; font-size: 0.9rem; display: block; margin-top: 5px;';
    field.parentNode.appendChild(error);
}

// Xóa lỗi field
function removeFieldError(field) {
    const error = field.parentNode.querySelector('.field-error');
    if (error) {
        error.remove();
    }
}

