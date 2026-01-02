// =========================
// 1. OOP: Class định nghĩa kỹ năng & kinh nghiệm
// =========================
class Skill {
    constructor(category, items) {
        this.category = category;
        this.items = items;
    }

    render() {
        const div = document.createElement('div');
        div.className = 'skill-card';
        div.innerHTML = `
            <strong>${this.category}</strong>
            <ul>${this.items.map(i => `<li>${i}</li>`).join('')}</ul>
        `;
        return div;
    }
}

class Experience {
    constructor(year, title, desc) {
        this.year = year;
        this.title = title;
        this.desc = desc;
    }

    render() {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${this.year}</td>
            <td>${this.title}</td>
            <td>${this.desc}</td>
        `;
        return tr;
    }
}

// =========================
// 2. Giả lập dữ liệu JSON (thay cho fetch file ngoài)
// =========================
const dataJSON = {
    "skills": [
        {"category": "Frontend", "items": ["HTML5", "CSS3", "JavaScript", "React", "Responsive Design"]},
        {"category": "Backend", "items": ["Spring Boot", "MySQL", "RESTful APIs"]},
        {"category": "Sở Thích", "items": ["Lập trình", "Đọc sách công nghệ", "Chơi thể thao"]}
    ],
    "experience": [
        {"year": "2023-2024", "title": "Học viện Công nghệ Bưu chính Viễn thông", "desc": "Chuyên ngành CNTT, GPA: 3.7/4.0"},
        {"year": "2024-2025", "title": "Lập trình viên Backend", "desc": "Công ty ABC Tech"},
        {"year": "2025-Hiện tại", "title": "Lập trình viên Full Stack", "desc": "Công ty XYZ Solutions - Phát triển ứng dụng web toàn diện"}
    ]
};

// =========================
// 3. Promise.all + async/await để nạp dữ liệu
// =========================
async function loadPortfolioData() {
    // Giả lập fetch dữ liệu (Promise)
    const fetchSkills = new Promise(resolve => setTimeout(() => resolve(dataJSON.skills), 500));
    const fetchExperience = new Promise(resolve => setTimeout(() => resolve(dataJSON.experience), 500));

    const [skills, experiences] = await Promise.all([fetchSkills, fetchExperience]);
    renderSkills(skills);
    renderExperience(experiences);
}

// =========================
// 4. Render dữ liệu ra DOM
// =========================
function renderSkills(skills) {
    const container = document.querySelector('.skills-grid');
    container.innerHTML = ''; // Xóa nội dung cũ
    skills.forEach(s => {
        const skill = new Skill(s.category, s.items);
        container.appendChild(skill.render());
    });
}

function renderExperience(experiences) {
    const tableBody = document.querySelector('#experience tbody');
    tableBody.innerHTML = '';
    experiences.forEach(e => {
        const exp = new Experience(e.year, e.title, e.desc);
        tableBody.appendChild(exp.render());
    });
}

// =========================
// 5. Cuộn mượt + highlight nav khi scroll
// =========================
document.querySelectorAll('nav a').forEach(link => {
    link.addEventListener('click', e => {
        e.preventDefault();
        const target = document.querySelector(link.getAttribute('href'));
        window.scrollTo({
            top: target.offsetTop - 60,
            behavior: 'smooth'
        });
    });
});

window.addEventListener('scroll', () => {
    const sections = document.querySelectorAll('section');
    const navLinks = document.querySelectorAll('nav a');
    let current = '';

    sections.forEach(section => {
        const sectionTop = section.offsetTop - 80;
        if (scrollY >= sectionTop) current = section.id;
    });

    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === '#' + current) {
            link.classList.add('active');
        }
    });
});

// =========================
// 6. Hiệu ứng hiện dần khi cuộn
// =========================
const fadeEls = document.querySelectorAll('section, header');
function fadeInOnScroll() {
    fadeEls.forEach(el => {
        const rect = el.getBoundingClientRect();
        if (rect.top < window.innerHeight - 100) {
            el.classList.add('visible');
        }
    });
}
window.addEventListener('scroll', fadeInOnScroll);
window.addEventListener('load', fadeInOnScroll);

// =========================
// 7. Gửi form liên hệ (fetch + async/await)
// =========================
document.querySelector('form').addEventListener('submit', async e => {
    e.preventDefault();
    const data = {
        name: e.target.name.value,
        email: e.target.email.value,
        message: e.target.message.value
    };

    try {
        const res = await fetch('https://jsonplaceholder.typicode.com/posts', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });
        const result = await res.json();
        alert(`✅ Cảm ơn bạn, ${data.name}! Tin nhắn của bạn đã được gửi.\nMã phản hồi: ${result.id}`);
        e.target.reset();
    } catch (error) {
        alert('❌ Gửi tin nhắn thất bại. Vui lòng thử lại.');
    }
});

// =========================
// 8. Dark Mode toggle (DOM)
// =========================
const darkBtn = document.createElement('button');
darkBtn.textContent = '🌙 Dark Mode';
darkBtn.className = 'dark-toggle';
document.body.appendChild(darkBtn);

darkBtn.addEventListener('click', () => {
    document.body.classList.toggle('dark');
    darkBtn.textContent = document.body.classList.contains('dark') ? '☀️ Light Mode' : '🌙 Dark Mode';
});

// =========================
// 9. Khởi tạo khi tải trang
// =========================
window.addEventListener('DOMContentLoaded', loadPortfolioData);
