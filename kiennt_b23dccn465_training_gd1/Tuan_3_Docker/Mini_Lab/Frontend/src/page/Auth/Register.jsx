import './Register.css';
import anhTheLC from '../../assets/image/anh_the_LC.jpg';
import {Eye, EyeOff} from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { base } from '../../service/Base.jsx';
import { App } from 'antd';

export default function Register() {
    const { message } = App.useApp();
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        password: '',
        phoneNumber: '',
        roles: ["USER1"]
    });

    const handleLogin = async (e) => {
        e.preventDefault();
        const payload = { 
            fullName: formData.fullName, 
            password: formData.password
        };
        
        try {
            message.loading('Đang đăng nhập...');
            const response = await axios.post(`${base}/auth/token`, payload);
            if (response.status === 200) {
                message.success('Đăng nhập thành công');
                localStorage.setItem('token', response.data.result.token);
                window.location.href = '/';
                return;
            }
            message.error('Đăng nhập thất bại');
        } catch (error) {
            message.error(error?.response?.data?.message || 'Đăng nhập thất bại');
        }
    };


    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            message.error('Mật khẩu xác nhận không khớp!');
            return;
        }

        const payload = new FormData();
        payload.append("username", formData.fullName);
        payload.append("email", formData.email);
        payload.append("password", formData.password);
        payload.append("phoneNumber", formData.phoneNumber);
        // payload.append("roles", ["USER"]);

        try {
            const response = await axios.post(`${base}/users`, payload);
            if (response.status === 200) {
                message.success('Đăng ký thành công!');
                handleLogin(e);
                return;
            }
            message.error('Đăng ký thất bại!');
        } catch (error) {
            message.error(error?.response?.data?.message || 'Đăng ký thất bại!');
        }
    };

    return (
        <div className="register-container">
            {/* Left side - Image */}
            <div className="register-image">
                <img src={anhTheLC} alt="Background" />
            </div>
            
            {/* Right side - Register Form */}
            <div className="register-form-container">
                <div className="register-form">
                    <h1 className="brand-name">LOK SHOP</h1>
                    <h2 className="register-title">Đăng ký</h2>
                   
                    <form className="register-form-element" onSubmit={handleSubmit}>
                        <div className="register-input-group">
                            <input 
                                type="text" 
                                name="fullName"
                                placeholder="Tên tài khoản" 
                                className="register-form-input"
                                value={formData.fullName}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        
                        <div className="register-input-group">
                            <input 
                                type="email" 
                                name="email"
                                placeholder="Email" 
                                className="register-form-input"
                                value={formData.email}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        
                        <div className="register-input-group">
                            <input 
                                type="tel" 
                                name="phoneNumber"
                                placeholder="Số điện thoại" 
                                className="register-form-input"
                                value={formData.phoneNumber}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        
                        <div className="register-input-group register-password-group">
                            <input 
                                type={showPassword ? "text" : "password"} 
                                name="password"
                                placeholder="Mật khẩu" 
                                className="register-form-input register-password-input"
                                value={formData.password}
                                onChange={handleInputChange}
                                required
                            />
                            <button 
                                type="button" 
                                className="register-password-toggle"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                            </button>
                        </div>
                        
                        <div className="register-input-group register-password-group">
                            <input 
                                type={showConfirmPassword ? "text" : "password"} 
                                name="confirmPassword"
                                placeholder="Xác nhận mật khẩu" 
                                className="register-form-input register-password-input"
                                value={formData.confirmPassword}
                                onChange={handleInputChange}
                                required
                            />
                            <button 
                                type="button" 
                                className="register-password-toggle"
                                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                            >
                                {showConfirmPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                            </button>
                        </div>
                        
                        <div className="register-button-group">
                            <button type="submit" className="register-submit-btn">
                                Đăng ký
                            </button>
                            
                            <button type="button" className="register-login-btn" onClick={() => navigate('/login')}>
                                Đăng nhập
                            </button>
                        </div>
                        
                        <div className="register-login-link">
                            <p>Đã có tài khoản? <a href="#" onClick={() => navigate('/login')}>Đăng nhập ngay</a></p>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}