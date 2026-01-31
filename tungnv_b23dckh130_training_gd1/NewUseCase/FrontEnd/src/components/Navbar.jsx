import {useNavigate} from "react-router-dom";
import { ShoppingCart, Search, LogOut, User } from "lucide-react";

function Navbar() {
    const navigate = useNavigate();
    const username = localStorage.getItem('username') || "Khách";

    const handleLogout = () => {
        localStorage.removeItem('username');
        navigate('/login');
    }

    return (
        <nav className="bg-[#121212] text-white sticky top-0 z-50 border-b border-gray-800">
            {/*Container cố định*/}
            <div className="max-w-7xl mx-auto px-8 h-20 flex items-center justify-between">
                {/*Logo*/}
                <div
                    onClick={() => navigate('/')}
                    className="flex items-center gap-3 cursor-pointer hover:opacity-80 transition"
                >
                    <div className="bg-blue-600 p-2 rounded-xl shadow-lg shadow-blue-500/20">
                        <span className="font-bold text-2xl tracking-tighter text-white">T</span>
                    </div>
                    <span className="font-bold text-2xl tracking-tight text-white">Vinh Tung</span>
                </div>
                {/*Search Bar*/}
                <div className="flex-1 max-w-2xl mx-12 relative">
                    <input
                        type="text"
                        placeholder="Tìm kiếm sản phẩm"
                        className="w-full bg-[#252525] border border-gray-700 text-gray-200 text-sm rounded-2xl py-3 pl-5 pr-12 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
                    />
                    <button className="absolute right-2 top-2 p-1.5 bg-blue-600 rounded-xl hover:bg-blue-700 transition">
                        <Search className="text-white" size={16} />
                    </button>
                </div>

                {/*User Aciton*/}
                <div className="flex items-center gap-8">

                    {/* Cart */}
                    <div className="relative cursor-pointer group flex items-center gap-2">
                        <div className="bg-gray-800 p-2.5 rounded-full group-hover:bg-gray-700 transition">
                            <ShoppingCart size={20} className="text-gray-300" />
                        </div>
                        <span className="absolute -top-1 -right-1 bg-red-500 text-white text-[10px] font-bold h-5 w-5 flex items-center justify-center rounded-full border-2 border-[#121212]">2</span>
                    </div>

                    {/*Divider*/}
                    <div className="h-8 w-[1px] bg-gray-700"></div>

                    {/* User Info */}
                    <div className="flex items-center gap-4">
                        <div className="text-right">
                            <p className="text-[11px] text-gray-400 uppercase font-bold tracking-wider">Thành viên</p>
                            <p className="text-sm font-bold text-white">{username}</p>
                        </div>

                        {/* Avatar giả*/}
                        <div className="h-10 w-10 bg-gradient-to-tr from-blue-500 to-purple-500 rounded-full flex items-center justify-center font-bold text-white shadow-lg">
                            {username.charAt(0).toUpperCase()}
                        </div>

                        <button
                            onClick={handleLogout}
                            className="text-gray-400 hover:text-red-500 transition-colors ml-2"
                            title="Đăng xuất"
                        >
                            <LogOut size={20} />
                        </button>
                    </div>
                </div>
            </div>
        </nav>
)
}

export default Navbar;