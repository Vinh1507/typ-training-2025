import './Information.css'
import axios from 'axios';
import { base } from '../../service/Base.jsx';
import { useEffect, useState } from 'react';
import NotLoggedIn from '../../components/NotLoggedIn.jsx';
import Breadcrumb from '../../components/Breadcrumb.jsx';

export default function Information() {
    const token = localStorage.getItem('token')
    
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!token) {
            setLoading(false);
            return;
        }
        (async () => {
            const response = await axios.get(`${base}/users/myInfor`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.status === 200) {
                setUserData(response.data);
                setLoading(false);
            }
        })();
    }, [token]);

    // Breadcrumb items
    const breadcrumbItems = [
        { label: "Trang chủ", path: "/" },
        { label: "Thông tin cá nhân" }
    ];

    if (!token) {
        return <NotLoggedIn />
    }

    return (
        loading ? (
            <>
                <Breadcrumb items={breadcrumbItems} />
                <div>Loading...</div>
            </>
        ) : (
        <>
            <Breadcrumb items={breadcrumbItems} />
            <div className="profile-container">
                <div className="profile-layout">
                    <aside className="sidebar">
                        <div className="hello">TRANG TÀI KHOẢN</div>
                        <div className="greeting">Xin chào, <strong>{userData.userName || userData.fullName || 'Người dùng'}</strong> !</div>
                        <nav className="sidebar-nav">
                            <button className="active">Thông tin tài khoản</button>
                            <button>Sổ địa chỉ (1)</button>
                            <button className="logout" onClick={() => { localStorage.removeItem('token'); window.location.href = '/'; }}>Đăng xuất</button>
                        </nav>
                    </aside>

                    <main className="content">
                        <h2 className="section-title">TÀI KHOẢN</h2>
                        <section className="user-info">
                            <div className="user-avatar">
                                <img src={userData.avatar || '/ava_user.webp'} alt="User avatar" />
                            </div>
                            <div className="user-details">
                                <div className="user-name">Tên tài khoản: <strong>{userData.userName || userData.fullName || 'Người dùng'}</strong></div>
                                <div className="user-email">Địa chỉ: <strong>Vietnam</strong></div>
                                <div className="user-phone">Điện thoại: <strong>{userData.phoneNumber || '—'}</strong></div>
                                <div className="user-email">Email: <strong>{userData.email || '—'}</strong></div>
                            </div>
                        </section>

                        <h3 className="orders-title">ĐƠN HÀNG CỦA BẠN</h3>
                        <div className="orders-table">
                            <div className="orders-head">
                                <span>Mã đơn hàng</span>
                                <span>Ngày đặt</span>
                                <span>Thành tiền</span>
                                <span>TT thanh toán</span>
                                <span>TT vận chuyển</span>
                            </div>
                            <div className="orders-empty">Không có đơn hàng nào.</div>
                        </div>
                    </main>
                </div>
            </div>
        </>
    ))
}