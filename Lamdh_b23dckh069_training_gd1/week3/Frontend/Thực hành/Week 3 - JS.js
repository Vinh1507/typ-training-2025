//nhập thông tin liên hệ và hiển thị người dùng
const submitButton = document.getElementById('submit');
const nameInput = document.getElementById('name');
const emailInput = document.getElementById('email');
const phoneInput = document.getElementById('sdt');
const messageInput = document.getElementById('loinhan');
const infoContainer = document.getElementById('PersonalInfo');

const msgBox = document.createElement('p');
msgBox.id = 'form-message';
msgBox.style.marginTop = '10px';
msgBox.style.fontWeight = 'bold';
msgBox.style.color = '#333';
submitButton.insertAdjacentElement('afterend', msgBox);

function showMessage(text, color = 'green') {
  msgBox.textContent = text;
  msgBox.style.color = color;
  setTimeout(() => (msgBox.textContent = ''), 3000);
}

function HienThiNguoiDung() {
  const savedUsers = JSON.parse(localStorage.getItem('userInfoList')) || [];
  const lastUser = savedUsers[savedUsers.length - 1];

  if (lastUser && infoContainer) {
    infoContainer.innerHTML = `
      <p><strong>Họ và tên:</strong> ${lastUser.name}</p>
      <p><strong>Email:</strong> ${lastUser.email}</p>
      <p><strong>Số điện thoại:</strong> ${lastUser.phone}</p>
      <p><strong>Nội dung:</strong> ${lastUser.message}</p>
    `;
  }
}

submitButton?.addEventListener('click', () => {
  const name = nameInput.value.trim();
  const email = emailInput.value.trim();
  const phone = phoneInput.value.trim();
  const message = messageInput.value.trim();

  if (!name || !email || !phone || !message) {
    showMessage('Vui lòng điền đầy đủ thông tin!', 'red');
    return;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const phoneRegex = /^[0-9]{9,11}$/;

  if (!emailRegex.test(email)) {
    showMessage('Email không hợp lệ!', 'red');
    return;
  }

  if (!phoneRegex.test(phone)) {
    showMessage('Số điện thoại không hợp lệ!', 'red');
    return;
  }

  const savedUsers = JSON.parse(localStorage.getItem('userInfoList')) || [];
  savedUsers.push({ name, email, phone, message });

  localStorage.setItem('userInfoList', JSON.stringify(savedUsers));

  showMessage('Cảm ơn bạn đã gửi thông tin!');

  nameInput.value = '';
  emailInput.value = '';
  phoneInput.value = '';
  messageInput.value = '';

  HienThiNguoiDung();
});

window.addEventListener('load', HienThiNguoiDung);
window.addEventListener('load', HienThiNguoiDung);

// cuộn đầu trang
const scrollBtn = document.createElement('button');
scrollBtn.textContent = '⬆';
scrollBtn.id = 'toTop';
Object.assign(scrollBtn.style, {
  position: 'fixed',
  bottom: '20px',
  right: '20px',
  padding: '10px 15px',
  border: 'none',
  borderRadius: '6px',
  background: '#007BFF',
  color: '#fff',
  cursor: 'pointer',
  display: 'none',
  transition: '0.3s'
});
document.body.appendChild(scrollBtn);

window.addEventListener('scroll', () => {
  scrollBtn.style.display = window.scrollY > 300 ? 'block' : 'none';
});

scrollBtn.addEventListener('click', () => {
  window.scrollTo({ top: 0, behavior: 'smooth' });
});
