import { useState } from 'react';
import { User, Eye, EyeOff, Star } from 'lucide-react'; // Icon
import myBackground from '../assets/IMG_2700.JPG';
import Swal from 'sweetalert2';
import authApi from "../api/authApi.js";

function Login() {
    const [activeTab, setActiveTab] = useState('signin');
    const [showPassword, setShowPassword] = useState(false);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const res = await authApi.login({
                username: email,
                password: password
            });

            Swal.fire('Thành công', res.data.msg, 'success');
            localStorage.setItem('access_token', res.data.access_token);
            localStorage.setItem('username', res.data.user_info.username);
            window.location.href = '/';

        } catch (error) {
            Swal.fire('Lỗi', error.response?.data?.detail || 'Đăng nhập thất bại', 'error');
        }
    };

    return (
        <div className="min-h-screen bg-[#121212] flex items-center justify-center p-4 font-sans">

            {/* KHUNG CHÍNH*/}
            <div className="bg-white rounded-[40px] shadow-2xl overflow-hidden flex w-full max-w-[1100px] min-h-[650px] transition-all duration-300">

                {/*CỘT TRÁI*/}
                <div className="w-full lg:w-1/2 p-10 md:p-14 flex flex-col justify-center bg-[#F8F9FD]">

                    {/* Logo */}
                    <div className="flex items-center gap-2 mb-6">
                        <div className="bg-black text-white p-2 rounded-xl shadow-lg shadow-black/20">
                            <Star size={18} fill="white" />
                        </div>
                        <span className="text-2xl font-bold text-gray-800 tracking-tight">Vinh Tung DZ</span>
                    </div>

                    {/* Heading */}
                    <h1 className="text-3xl font-extrabold text-gray-900 mb-2">Xin chào đằng ấy</h1>
                    <p className="text-gray-500 mb-8 font-medium">Chào mừng đến với bình nguyên vô tận</p>

                    {/*Tab Switcher Mới */}
                    <div className="relative bg-gray-200 p-1 rounded-full grid grid-cols-2 w-full max-w-sm mb-8">
                        <div
                            className={`absolute top-1 bottom-1 w-[calc(50%-4px)] bg-blue-600 rounded-full shadow-md transition-all duration-300 ease-in-out ${activeTab === 'signin' ? 'left-1' : 'left-[50%]'}`}
                        ></div>

                        {/* Nút Sign In */}
                        <button
                            type="button"
                            onClick={() => setActiveTab('signin')}
                            className={`relative z-10 py-2.5 text-sm font-bold rounded-full transition-colors duration-300 ${activeTab === 'signin' ? 'text-white' : 'text-gray-500 hover:text-gray-700'}`}
                        >
                            Đăng nhập
                        </button>

                        {/* Nút Sign Up */}
                        <button
                            type="button"
                            onClick={() => setActiveTab('signup')}
                            className={`relative z-10 py-2.5 text-sm font-bold rounded-full transition-colors duration-300 ${activeTab === 'signup' ? 'text-white' : 'text-gray-500 hover:text-gray-700'}`}
                        >
                            Đăng ký
                        </button>

                    </div>

                    {/* Form */}
                    <form onSubmit={handleLogin} className="space-y-5">

                        {/* Input Username */}
                        <div className="relative group">
                            <input
                                type="text"
                                placeholder="Nhập tên đăng nhập"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="w-full bg-white border border-gray-200 text-gray-800 text-sm rounded-2xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 block p-4 pl-4 pr-10 outline-none transition-all shadow-sm group-hover:shadow-md font-medium"
                            />
                            <div className="absolute inset-y-0 right-0 pr-4 flex items-center pointer-events-none">
                                <User size={20} className="text-gray-400" />
                            </div>
                        </div>

                        {/* Input Password */}
                        <div className="relative group">
                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder="Nhập mật khẩu"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="w-full bg-white border border-gray-200 text-gray-800 text-sm rounded-2xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 block p-4 pl-4 pr-10 outline-none transition-all shadow-sm group-hover:shadow-md font-medium"
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className="absolute inset-y-0 right-0 pr-4 flex items-center text-gray-400 hover:text-gray-600 cursor-pointer"
                            >
                                {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
                            </button>
                        </div>

                        {/* Remember & Login Button */}
                        <div className="flex items-center justify-between text-sm font-medium pt-2">
                            <label className="flex items-center text-gray-500 cursor-pointer hover:text-gray-700">
                                <input type="checkbox" className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500 mr-2 accent-blue-600" defaultChecked />
                                Ghi nhớ tôi
                            </label>
                            <a href="#" className="text-blue-600 hover:underline">Quên mật khẩu?</a>
                        </div>

                        <button
                            type="submit"
                            className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:ring-blue-300 font-bold rounded-2xl text-base px-5 py-4 text-center shadow-xl shadow-blue-500/30 transition-transform active:scale-[0.98]"
                        >
                            Đăng nhập
                        </button>
                    </form>
                </div>

                {/* --- CỘT PHẢI: IMAGE & GLASS CARD --- */}
                <div className="hidden lg:block w-1/2 relative bg-blue-900">
                    {/* Ảnh nền sóng xanh */}
                    <img
                        src={myBackground}
                        alt="My Background"
                        className="absolute inset-0 w-full h-full object-cover opacity-100"
                    />
                </div>

            </div>
        </div>
    );
}

export default Login;