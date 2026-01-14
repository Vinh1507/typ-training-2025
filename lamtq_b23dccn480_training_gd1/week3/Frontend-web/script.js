// Hàm hiển thị toast
function showToast(message, type = 'success') {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toast-body');
    toastEl.classList.remove('bg-success', 'bg-danger', 'bg-warning');
    toastBody.classList.remove('text-dark', 'text-white');

    // Thiết lập màu theo loại
    switch (type) {
        case 'success':
            toastEl.classList.add('bg-success');
            toastBody.classList.add('text-white');
            break;
        case 'error':
            toastEl.classList.add('bg-danger');
            toastBody.classList.add('text-white');
            break;
        case 'warning':
            toastEl.classList.add('bg-warning');
            toastBody.classList.add('text-dark');
            break;
    }

    toastBody.textContent = message;
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
}


const form = document.querySelector('form');

form.addEventListener('submit', e => {
    e.preventDefault();

    const name = document.querySelector('#name').value.trim();
    const email = document.querySelector('#email').value.trim();
    const msg = document.querySelector('#message').value.trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;

    if (!name || !email || !msg) {
        showToast('Vui lòng điền đầy đủ thông tin!', 'warning');
        return;
    }

    if (!emailRegex.test(email)) {
        showToast('Email không hợp lệ. Vui lòng nhập Gmail hợp lệ!', 'error');
        return;
    }

    showToast(`Cảm ơn ${name}! Mình đã nhận được tin nhắn của bạn.`, 'success');
    form.reset();
});

const text = "Backend Developer | IT Student tại PTIT";
let index = 0;

function typingEffect() {
    const element = document.querySelector('#hero p.lead');
    if (element) {
        if (index < text.length) {
            element.textContent += text.charAt(index);
            index++;
            setTimeout(typingEffect, 70);
        }
    }
}

window.addEventListener('load', () => {
    const el = document.querySelector('#hero p.lead');
    el.textContent = ""; // xoá sẵn text gốc
    typingEffect();
});
