import { useNavigate } from 'react-router-dom';
import { Zap, Clock, Flame, ArrowRight } from 'lucide-react';
import Navbar from '../components/Navbar';
import { useEffect, useState } from "react";
import productApi from "../api/productApi.js";

function Home() {
    const navigate = useNavigate();
    const [products, setProducts] = useState([]);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                const response = await productApi.getAll();
                console.log(response.data);
                setProducts(response.data);
            } catch (error) {
                console.error(error);
            }
        }
        fetchProducts();
    }, []);
    

    return (
        <div className="min-h-screen bg-[#F5F7FA] font-sans">
            {/* --- HERO BANNER (Fixed Layout) --- */}
            <div className="bg-[#0F172A] text-white h-[550px] relative overflow-hidden flex items-center">
                {/* Background Gradients */}
                <div className="absolute top-0 right-0 w-[800px] h-[800px] bg-blue-600 rounded-full mix-blend-screen filter blur-[150px] opacity-20 translate-x-1/3 -translate-y-1/3"></div>
                <div className="absolute bottom-0 left-0 w-[600px] h-[600px] bg-purple-600 rounded-full mix-blend-screen filter blur-[150px] opacity-20 -translate-x-1/3 translate-y-1/3"></div>

                <div className="max-w-7xl mx-auto px-8 w-full grid grid-cols-2 gap-12 relative z-10 items-center">

                    {/* Cột Trái: Text */}
                    <div>
                        <div className="inline-flex items-center gap-2 bg-gradient-to-r from-orange-500 to-red-600 text-white text-xs font-bold px-4 py-1.5 rounded-full uppercase tracking-widest mb-6 shadow-lg shadow-orange-500/30 animate-pulse">
                            <Flame size={14} fill="white" /> Đang diễn ra
                        </div>

                        <h1 className="text-7xl font-extrabold mb-6 leading-tight tracking-tight">
                            SĂN SALE <br/>
                            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400">GIỜ VÀNG 1K</span>
                        </h1>

                        <p className="text-gray-300 mb-10 text-xl max-w-lg leading-relaxed">
                            Cơ hội duy nhất trong năm để sở hữu siêu phẩm công nghệ.
                            Số lượng cực giới hạn, không nhanh tay là mất lượt!
                        </p>

                        <div className="flex gap-4">
                            <button className="bg-white text-[#0F172A] font-bold py-4 px-10 rounded-2xl hover:bg-gray-100 hover:scale-105 transition transform shadow-xl flex items-center gap-2">
                                Tham Gia Ngay <ArrowRight size={20} />
                            </button>
                            <button className="bg-white/10 backdrop-blur-md border border-white/20 text-white font-bold py-4 px-10 rounded-2xl hover:bg-white/20 transition">
                                Xem Thể Lệ
                            </button>
                        </div>
                    </div>

                    {/*Cột Phải: Ảnh Minh Họa*/}
                    <div className="relative h-full flex items-center justify-center">
                        {/* Vòng tròn trang trí */}
                        <div className="absolute w-[450px] h-[450px] border border-white/10 rounded-full animate-[spin_10s_linear_infinite]"></div>
                        <div className="absolute w-[350px] h-[350px] border border-white/20 rounded-full animate-[spin_15s_linear_infinite_reverse]"></div>

                        {/* Icon sấm sét to */}
                        <Zap size={300} className="text-yellow-400 drop-shadow-[0_0_50px_rgba(250,204,21,0.5)] rotate-12 hover:rotate-0 transition-all duration-700 cursor-pointer" fill="currentColor" />
                    </div>
                </div>
            </div>

            {/*FLASH SALE LIST*/}
            <div className="max-w-7xl mx-auto px-8 -mt-8 relative z-20 pb-20">

                {/* Header Thanh Ngang */}
                <div className="bg-white rounded-t-3xl p-8 shadow-sm border-b border-gray-100 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        <div className="bg-red-50 p-3 rounded-2xl">
                            <Zap className="text-red-600 fill-red-600" size={32} />
                        </div>
                        <div>
                            <h2 className="text-3xl font-bold text-gray-800">Flash Sale</h2>
                            <p className="text-gray-500 text-sm mt-1">Nhanh tay kẻo lỡ - Chỉ còn số lượng ít</p>
                        </div>
                    </div>

                    {/*Đồng hồ đếm ngược to*/}
                    <div className="flex items-center gap-4 bg-[#FFF0F0] px-6 py-3 rounded-2xl border border-red-100">
                        <div className="flex flex-col items-center">
                            <span className="text-xs text-red-500 font-bold uppercase tracking-wider mb-1">Kết thúc sau</span>
                            <div className="flex items-center gap-2 text-red-600 font-mono font-bold text-2xl">
                                <Clock size={24} /> 02 : 15 : 45
                            </div>
                        </div>
                    </div>
                </div>

                {/*LƯỚI SẢN PHẨM (Cố định 4 cột)*/}
                <div className="bg-white rounded-b-3xl p-8 shadow-2xl grid grid-cols-4 gap-8">
                    {products.map((product) => {
                        const percentSold = (1 - (product.sold / product.total_stock)) * 100;

                        return (
                            <div
                                key={product.id}
                                className="group relative border border-gray-100 rounded-3xl p-5 hover:border-blue-200 hover:shadow-2xl hover:-translate-y-2 transition-all duration-300 cursor-pointer bg-white flex flex-col h-full"
                                onClick={() => navigate(`/product/${product.id}`)}
                            >
                                {/*Badge HOT*/}
                                {product.is_hot && (
                                    <div className="absolute top-4 left-4 bg-[#FF424F] text-white text-[10px] font-bold px-3 py-1.5 rounded-lg shadow-lg shadow-red-500/30 z-10 flex items-center gap-1">
                                        <Flame size={12} fill="white" /> HOT DEAL
                                    </div>
                                )}

                                {/*Ảnh sản phẩm*/}
                                <div className="h-64 flex items-center justify-center mb-6 relative overflow-hidden rounded-2xl bg-[#F8F9FB]">
                                    <img
                                        src={product.image}
                                        alt={product.name}
                                        className="object-contain h-56 w-56 mix-blend-multiply group-hover:scale-110 transition-transform duration-500 ease-out"
                                    />
                                </div>

                                {/*Thông tin*/}
                                <div className="flex-1 flex flex-col">
                                    <h3 className="font-bold text-gray-800 text-lg mb-2 line-clamp-2 group-hover:text-blue-600 transition">{product.name}</h3>

                                    <div className="mt-auto">
                                        <div className="flex items-baseline gap-3 mb-4">
                                            <span className="text-2xl font-extrabold text-[#FF424F]">{product.price.toLocaleString()}₫</span>
                                            <span className="text-sm text-gray-400 line-through font-medium">{product.original_price.toLocaleString()}₫</span>
                                        </div>

                                        {/* Thanh trạng thái (Sold Bar) */}
                                        <div className="relative pt-1">
                                            <div className="flex justify-between text-[11px] font-bold text-gray-500 mb-1.5 uppercase tracking-wide">
                                                <span className="flex items-center gap-1"><Zap size={12} className="text-orange-500"/> Đã bán {product.sold}</span>
                                                <span className="text-orange-500">{percentSold.toFixed(0)}%</span>
                                            </div>
                                            <div className="w-full bg-gray-100 rounded-full h-3 overflow-hidden border border-gray-100">
                                                <div
                                                    className="bg-gradient-to-r from-[#FF9C36] to-[#FF424F] h-full rounded-full relative"
                                                    style={{ width: `${percentSold}%` }}
                                                >
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                {/* Nút giả lập (Chỉ hiện khi Hover) */}
                                <div className="absolute bottom-5 right-5 opacity-0 translate-x-4 group-hover:opacity-100 group-hover:translate-x-0 transition-all duration-300">
                                    <button className="bg-[#0F172A] text-white p-3 rounded-xl shadow-lg hover:bg-blue-600 transition">
                                        <ArrowRight size={20} />
                                    </button>
                                </div>
                            </div>
                        );
                    })}
                </div>

            </div>
        </div>
    );
}

export default Home;