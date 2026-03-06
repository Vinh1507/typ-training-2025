import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { base } from "../../../service/Base.jsx";
import "./Sale.css";

export default function Sale() {
    const navigate = useNavigate();
    const [sale, setSale] = useState(null);
    const [loading, setLoading] = useState(true);
    const [productsDetails, setProductsDetails] = useState({}); // Lưu thông tin chi tiết sản phẩm

    // State quản lý đếm ngược và carousel
    const [timeLeft, setTimeLeft] = useState({ days: 0, hours: 0, minutes: 0, seconds: 0 }); // Thời gian còn lại
    const [currentSlide, setCurrentSlide] = useState(0); // Slide hiện tại trong carousel
    const [salePhase, setSalePhase] = useState('ended'); // 'preparing', 'selling', hoặc 'ended'

    // Fetch active sale from API - Không cần authentication
    useEffect(() => {
        const fetchActiveSale = async () => {
            setLoading(true);
            try {
                const response = await axios.get(`${base}/sales`);

                if (response.status === 200 && response.data?.result) {
                    const sales = response.data.result || [];
                    const now = new Date().getTime();

                    // Tìm sale đang active (thời gian hiện tại nằm giữa stDate và endDate)
                    const activeSale = sales.find(s => {
                        const stDate = new Date(s.stDate).getTime();
                        const endDate = new Date(s.endDate).getTime();
                        return now >= stDate && now < endDate;
                    });

                    if (activeSale) {
                        setSale(activeSale);
                        
                        // Fetch thông tin chi tiết sản phẩm
                        if (activeSale.list_product && activeSale.list_product.length > 0) {
                            const productIds = activeSale.list_product.map(p => p.id).filter(Boolean);
                            
                            if (productIds.length > 0) {
                                const promises = productIds.map(id => 
                                    axios.get(`${base}/products/${id}`).catch(err => {
                                        console.log(`Error fetching product ${id}:`, err);
                                        return null;
                                    })
                                );
                                
                                const responses = await Promise.all(promises);
                                const details = {};
                                
                                responses.forEach((response, index) => {
                                    if (response && response.status === 200) {
                                        details[productIds[index]] = response.data.result;
                                    }
                                });
                                
                                setProductsDetails(details);
                            }
                        }
                    } else {
                        setSale(null);
                        setProductsDetails({});
                    }
                }
            } catch (error) {
                console.error('Error fetching sales:', error);
                setSale(null);
                setProductsDetails({});
            } finally {
                setLoading(false);
            }
        };

        fetchActiveSale();
    }, []);

    // useEffect xử lý đếm ngược thời gian sale
    useEffect(() => {
        if (!sale) {
            setSalePhase('ended');
            setTimeLeft({ days: 0, hours: 0, minutes: 0, seconds: 0 });
            return;
        }

        const startDate = new Date(sale.stDate).getTime();
        const endDate = new Date(sale.endDate).getTime();
        
        // Timer chạy mỗi giây để cập nhật đếm ngược
        const timer = setInterval(() => {
            const now = new Date().getTime();
            
            if (now < startDate) {
                // Giai đoạn chuẩn bị sale: đếm ngược từ hiện tại đến ngày bắt đầu sale
                setSalePhase('preparing');
                const difference = startDate - now;
                
                if (difference > 0) {
                    const days = Math.floor(difference / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((difference % (1000 * 60)) / 1000);
                    
                    setTimeLeft({ days, hours, minutes, seconds });
                } else {
                    setTimeLeft({ days: 0, hours: 0, minutes: 0, seconds: 0 });
                }
            } else if (now >= startDate && now < endDate) {
                // Giai đoạn đang sale: đếm ngược từ hiện tại đến ngày kết thúc sale
                setSalePhase('selling');
                const difference = endDate - now;
                
                if (difference > 0) {
                    const days = Math.floor(difference / (1000 * 60 * 60 * 24));
                    const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                    const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
                    const seconds = Math.floor((difference % (1000 * 60)) / 1000);
                    
                    setTimeLeft({ days, hours, minutes, seconds });
                } else {
                    setTimeLeft({ days: 0, hours: 0, minutes: 0, seconds: 0 });
                }
            } else {
                // Sale đã kết thúc
                setSalePhase('ended');
                setTimeLeft({ days: 0, hours: 0, minutes: 0, seconds: 0 });
            }
        }, 1000);

        return () => clearInterval(timer);
    }, [sale]);

    // Hàm chuyển slide tiếp theo (vòng lặp)
    const nextSlide = () => {
        const productCount = sale?.list_product?.length || 0;
        if (productCount > 0) {
            setCurrentSlide((prev) => (prev + 1) % productCount);
        }
    };

    // Hàm chuyển slide trước đó (vòng lặp)
    const prevSlide = () => {
        const productCount = sale?.list_product?.length || 0;
        if (productCount > 0) {
            setCurrentSlide((prev) => (prev - 1 + productCount) % productCount);
        }
    };


    // Chỉ hiển thị sale đang active (đang bán), không hiển thị khi đang chuẩn bị hoặc đã kết thúc
    if (loading || !sale || salePhase !== 'selling') {
        return null;
    }

    const products = sale.list_product || [];
    const saleName = sale.name || 'Sale';
    const saleDescription = sale.description || '';
    
    // Tính discount percentage từ value (0-1) -> (0-100%)
    const getDiscountPercent = (product) => {
        if (product.value) {
            return Math.round(product.value * 100);
        }
        return 0;
    };

    return (
        <section className="sale-section">
            <div className="sale-container">
                {/* Cột trái: Thông tin khuyến mãi + đếm ngược */}
                <div className="sale-info">
                    {/* Tiêu đề và mô tả khuyến mãi */}
                    <h2 className="sale-title">
                        {saleName.toUpperCase()}
                    </h2>
                    <p className="sale-description">
                        {saleDescription}
                    </p>
                    <button className="sale-btn">
                        Mua ngay
                    </button>
                    
                    {/* Phần đếm ngược thời gian */}
                    <div className="countdown-section">
                        <h3 className="countdown-title">
                            Hãy nhanh tay để mua hàng ngay!
                        </h3>
                        <div className="countdown-timer">
                            {/* Ô hiển thị số ngày còn lại */}
                            <div className="time-box">
                                <span className="time-number">{String(timeLeft.days).padStart(2, '0')}</span>
                                <span className="time-label">Ngày</span>
                            </div>
                            {/* Ô hiển thị số giờ còn lại */}
                            <div className="time-box">
                                <span className="time-number">{String(timeLeft.hours).padStart(2, '0')}</span>
                                <span className="time-label">Giờ</span>
                            </div>
                            {/* Ô hiển thị số phút còn lại */}
                            <div className="time-box">
                                <span className="time-number">{String(timeLeft.minutes).padStart(2, '0')}</span>
                                <span className="time-label">Phút</span>
                            </div>
                            {/* Ô hiển thị số giây còn lại */}
                            <div className="time-box">
                                <span className="time-number">{String(timeLeft.seconds).padStart(2, '0')}</span>
                                <span className="time-label">Giây</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Cột phải: Carousel sản phẩm */}
                {products.length > 0 && (
                    <div className="sale-carousel">
                        {/* Container chứa các slide */}
                        <div className="carousel-container">
                            {/* Track chứa tất cả slide, di chuyển theo currentSlide */}
                            <div 
                                className="carousel-track" 
                                style={{ transform: `translateX(-${currentSlide * 100}%)` }}
                            >
                                {/* Render từng slide sản phẩm */}
                                {products.map((product, index) => {
                                    const productDetail = productsDetails[product.id];
                                    const productName = productDetail?.title || productDetail?.name || `Sản phẩm ${product.id || index + 1}`;
                                    const productId = productDetail?.productId || product.id;
                                    
                                    return (
                                        <div 
                                            key={product.id || index} 
                                            className="carousel-slide"
                                            onClick={() => {
                                                if (productId) {
                                                    navigate(`/product/${productId}`);
                                                }
                                            }}
                                            style={{ cursor: productId ? 'pointer' : 'default' }}
                                        >
                                            <img src={product.image || productDetail?.image} alt={productName} />
                                            {/* Overlay hiển thị thông tin khuyến mãi */}
                                            <div className="slide-overlay">
                                                <span className="slide-number">{productName}</span>
                                                <span className="slide-discount">{getDiscountPercent(product)}% OFF</span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>
                        
                        {/* Điều khiển carousel: nút prev/next và dots indicator */}
                        <div className="carousel-controls">
                            <button className="carousel-btn prev" onClick={prevSlide}>‹</button>
                            <button className="carousel-btn next" onClick={nextSlide}>›</button>
                            {/* Dots indicator - hiển thị slide hiện tại */}
                            <div className="carousel-dots">
                                {products.map((_, index) => (
                                    <button
                                        key={index}
                                        className={`dot ${index === currentSlide ? 'active' : ''}`}
                                        onClick={() => setCurrentSlide(index)}
                                    />
                                ))}
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </section>
    );
}