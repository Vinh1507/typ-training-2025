import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Swal from "sweetalert2";
import { ArrowLeft, Clock, Zap, Loader2 } from "lucide-react";
import orderApi from "../api/orderApi.js";
import productApi from "../api/productApi.js";

function ProductDetail() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [product, setProduct] = useState(null);
    const [stock, setStock] = useState(0);
    const [timeLeft, setTimeLeft] = useState(5);
    const [isLive, setIsLive] = useState(false);
    const [loading, setLoading] = useState(false);
    const [fetchingProduct, setFetchingProduct] = useState(true);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                setFetchingProduct(true);
                const response = await productApi.getDetail(id);
                const productData = response.data;
                setProduct(productData);
                setStock(productData.total_stock - productData.sold);
            } catch (error) {
                console.error('Lỗi khi lấy thông tin sản phẩm:', error);
                Swal.fire({
                    title: 'Lỗi!',
                    text: 'Không tìm thấy sản phẩm',
                    icon: 'error',
                    confirmButtonText: 'OK'
                }).then(() => {
                    navigate('/');
                });
            } finally {
                setFetchingProduct(false);
            }
        };

        fetchProduct();
    }, [id, navigate]);

    useEffect(() => {
        if (timeLeft > 0) {
            const timer = setInterval(() => setTimeLeft((prev) => prev - 1), 1000);
            return () => clearInterval(timer);
        } else {
            setIsLive(true);
        }
    }, [timeLeft]);

    const handleBuy = async () => {
        const username = localStorage.getItem('username')
        if (!username) {
            Swal.fire('Chưa đăng nhập', 'Vui lòng đăng nhập để săn sale', 'warning');
            navigate('/login');
            return;
        }
        setLoading(true);
        try {
            const response = await orderApi.buy({
                username: username,
                product_id: id
            });

            const data = response.data;
            if (data.status === 'success') {
                Swal.fire({
                    title: 'Thành công!',
                    text: data.msg,
                    icon: 'success',
                    confirmButtonText: 'Tuyệt vời'
                });
                setStock((prev) => prev > 0 ? prev - 1 : prev);
            } else {
                Swal.fire({
                    title: 'Hết hàng!',
                    text: data.msg,
                    icon: 'error',
                    confirmButtonText: 'Buồn quá'
                });
                setStock(0);
            }
        } catch (error) {
            console.error(error);
            Swal.fire('Lỗi', 'Không kết nối được Server!', 'error');
        } finally {
            setLoading(false);
        }
    }

    if (fetchingProduct) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <Loader2 className="animate-spin mx-auto mb-4 text-blue-600" size={48} />
                    <p className="text-gray-600 font-medium">Đang tải thông tin sản phẩm...</p>
                </div>
            </div>
        );
    }

    if (!product) {
        return null;
    }

    // Tính toán các giá trị sau khi đã chắc chắn product tồn tại
    const stockPercent = (stock / product.total_stock) * 100;
    
    const discountPercent = product.original_price > 0 
        ? Math.round(((product.original_price - product.price) / product.original_price) * 100)
        : 0;
    return (
        <div>
            {/*Nut quay lai*/}
            <div className={"max-w-6xl mx-auto px-4 py-6"}>
                <button onClick={() => navigate('/')} className="flex items-center gap-2 text-gray-500 hover:text-blue-600 transition">
                    <ArrowLeft size={20} /> Quay lại danh sách
                </button>
            </div>

            {/*Khung san pham*/}
            <div className="max-w-6xl mx-auto px-4">
                <div className="bg-white rounded-3xl shadow-xl overflow-hidden flex flex-col md:flex-row">
                    {/*COT TRAI*/}
                    <div className="md:w-1/2 bg-gray-50 p-10 flex items-center justify-center relative">
                        <img
                            src={product.image}
                            alt={product.name}
                            className="max-h-[400px] object-contain mix-blend-multiply hover:scale-105 transition duration-500"
                        />
                        {/*Flash Sale Badge - chỉ hiển thị nếu là sản phẩm hot*/}
                        {product.is_hot && (
                            <div className="absolute top-6 left-6 bg-red-600 text-white font-bold px-4 py-2 rounded-lg shadow-lg animate-pulse flex items-center gap-2">
                                <Zap size={18} fill={'white'} /> FLASH SALE
                            </div>
                        )}
                    </div>

                    {/*COT PHAI*/}
                    <div className="md:w-1/2 p-10 flex flex-col justify-center">
                        <h1 className="text-3xl font-extrabold text-gray-800 mb-4">{product.name}</h1>
                        {/*GIA TIEN*/}
                        <div className="flex items-end gap-4 mb-8">
                            <span className="text-5xl font-black text-red-800">{product.price.toLocaleString()}₫</span>
                            <span className="text-xl text-gray-400 line-through mb-2">{product.original_price.toLocaleString()}₫</span>
                            {discountPercent > 0 && (
                                <span className="bg-red-100 text-red-600 font-bold px-2 py-1 rounded mb-2">-{discountPercent}%</span>
                            )}
                        </div>

                        {/*THANH STOCK & NUT MUA*/}
                        <div className="bg-gray-50 p-6 rounded-2xl border border-gray-200 mb-6">
                            <div className="flex justify-between text-sm font-bold text-gray-600 mb-2">
                                <span>Trạng thái kho</span>
                                <span className={stock === 0 ? "text-gray-400" : "text-orange-500"}>
                                    {stock === 0 ? "Hết hàng" : `Còn ${stock} sản phẩm`}
                                </span>
                            </div>
                            <div className="w-full bg-gray-200 rounded-full h-4 overflow-hidden mb-6">
                                <div
                                    className={`h-full transition-all duration-500 ease-out ${stock === 0 ? 'bg-gray-400' : 'bg-gradient-to-r from-orange-500 to-red-600'}`}
                                    style={{ width: `${stockPercent}%` }}
                                ></div>
                            </div>

                            {/*NUT BAM*/}
                            <button
                                onClick={handleBuy}
                                disabled={!isLive || loading || stock === 0}
                                className={`w-full py-4 rounded-xl font-bold text-lg shadow-lg transition-all active:scale-95 flex items-center justify-center gap-2 
                                    ${!isLive
                                    ? 'bg-slate-800 text-gray-400 cursor-not-allowed'
                                    : stock === 0
                                        ? 'bg-gray-300 text-gray-500 cursor-not-allowed'
                                        : 'bg-red-600 hover:bg-red-700 text-white animate-bounce-slow'
                                    }
                                `}
                            >
                                {!isLive ? (
                                    <><Clock /> Mở bán sau: 00:{timeLeft.toString().padStart(2, '0')}</>
                                ) : loading ? (
                                    <div className="flex items-center gap-2">
                                        <Loader2 className="animate-spin" /> Đang xử lý...
                                    </div>
                                ) : stock === 0 ? (
                                    'HẾT HÀNG'
                                ) : (
                                    <><Zap fill={'white'} />SĂN NGAY</>
                                )}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ProductDetail;