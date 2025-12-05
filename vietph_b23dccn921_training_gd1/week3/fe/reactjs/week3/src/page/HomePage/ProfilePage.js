import React from "react";
import "./ProfilePage.scss";

const ProfilePage = () => {
  const handleSubmit = (e) => {
    e.preventDefault();
    alert("Cảm ơn bạn đã gửi tin nhắn!");
    e.target.reset();
  };

  return (
    <div className="profile-page">
      <header className="profile-header">
        <img
          src="https://res.cloudinary.com/dqh8sw4o6/image/upload/v1761382814/chat-app/ykorfvlstazut7lnlybk.jpg"
          alt="Ảnh đại diện"
        />
        <h1>Phạm Hoàng Việt</h1>
        <p>Sinh viên PTIT | Đam mê lập trình Web</p>
      </header>

      <main className="profile-main">
        <section>
          <h2>Giới thiệu</h2>
          <p>
            Mình là lập trình viên web với niềm yêu thích tạo ra giao diện đẹp
            và thân thiện. Mục tiêu của mình là trở thành một Fullstack
            Developer trong tương lai.
          </p>
        </section>

        <section>
          <h2>Kỹ năng</h2>
          <ul>
            <li>HTML, CSS, JavaScript</li>
            <li>ReactJS, Node.js</li>
            <li>Git, REST API</li>
            <li>Thiết kế giao diện (UI/UX cơ bản)</li>
          </ul>
        </section>

        <section>
          <h2>Học vấn & Kinh nghiệm</h2>
          <table>
            <thead>
              <tr>
                <th>Thời gian</th>
                <th>Chi tiết</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>2018 - 2022</td>
                <td>
                  Sinh viên CNTT - Học viện Công nghệ Bưu Chính Viễn Thông
                </td>
              </tr>
            </tbody>
          </table>
        </section>

        <section>
          <h2>Liên hệ</h2>
          <form onSubmit={handleSubmit}>
            <input type="text" id="name" placeholder="Tên của bạn" required />
            <input
              type="email"
              id="email"
              placeholder="Email của bạn"
              required
            />
            <textarea
              id="message"
              placeholder="Nội dung tin nhắn..."
              required
            ></textarea>
            <button type="submit">Gửi</button>
          </form>
        </section>
      </main>

      <footer>
        © 2025 Phạm Hoàng Việt - All rights reserved
      </footer>
    </div>
  );
};

export default ProfilePage;
