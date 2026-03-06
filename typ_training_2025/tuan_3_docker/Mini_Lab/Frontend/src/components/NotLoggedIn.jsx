import { useNavigate } from 'react-router-dom'
import { Lock, LogIn, UserPlus } from 'lucide-react'
import '../assets/style/NotLoggedIn.css'

export default function NotLoggedIn() {
    const navigate = useNavigate()

    return (
        <section className="not-logged-in-container">
            <div className="not-logged-in-card" role="region" aria-label="Yêu cầu đăng nhập">
                <div className="not-logged-in-icon" aria-hidden>
                    <Lock size={28} strokeWidth={2.5} />
                </div>
                <h2 className="not-logged-in-title">Bạn cần đăng nhập để tiếp tục</h2>
                <p className="not-logged-in-description">
                    Vui lòng đăng nhập để truy cập nội dung này, theo dõi đơn hàng và trải nghiệm đầy đủ các tính năng của LOK SHOP.
                </p>
                <div className="not-logged-in-actions">
                    <button
                        type="button"
                        className="not-logged-in-btn not-logged-in-btn--primary"
                        onClick={() => navigate('/login')}
                        aria-label="Đi tới trang đăng nhập"
                    >
                        <LogIn size={18} /> Đăng nhập
                    </button>
                    <button
                        type="button"
                        className="not-logged-in-btn"
                        onClick={() => navigate('/register')}
                        aria-label="Đi tới trang đăng ký"
                    >
                        <UserPlus size={18} /> Đăng ký
                    </button>
                </div>
                <div className="not-logged-in-note">
                    Chưa có tài khoản? Đăng ký miễn phí chỉ mất 1 phút.
                </div>
            </div>
        </section>
    )
}