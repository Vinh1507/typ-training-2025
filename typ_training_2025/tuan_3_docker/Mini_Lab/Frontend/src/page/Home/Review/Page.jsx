import { useState } from 'react'
import './Page.css'


export default function Page() {
    const [currentIndex, setCurrentIndex] = useState(0)
    
    const response = [
        {
            user_fullName: "Đinh Việt Dũng",
            user_img: "https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg",
            comment: "Shop rất tốt, quá xịn, chất lượng tốt, giá cả phải chăng, giao hàng nhanh"
        },
        {
            user_fullName: "Nguyễn Văn A",
            user_img: "https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg",
            comment: "Shop rất tốt, quá xịn, chất lượng kém, giá cả quá đắt, giao hàng chậm"
        },
        {
            user_fullName: "Nguyễn Văn B",
            user_img: "https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg",
            comment: "Shop rất tốt, quá xịn, chất lượng tốt, giá cả phải chăng, giao hàng nhanh"
        },
        {
            user_fullName: "Nguyễn Văn C",
            user_img: "https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg",
            comment: "Shop rất tốt, quá xịn, chất lượng tốt, giá cả phải chăng, giao hàng nhanh"
        },
        {
            user_fullName: "Nguyễn Văn D",
            user_img: "https://cdn2.fptshop.com.vn/small/avatar_trang_1_cd729c335b.jpg",
            comment: "Shop rất tốt, quá xịn, chất lượng tốt, giá cả phải chăng, giao hàng nhanh"
        }
    ]

    const goToNextReview = () => {
        setCurrentIndex((prevIndex) => 
            prevIndex === response.length - 1 ? 0 : prevIndex + 1
        )
    }

    const goToPrevReview = () => {
        setCurrentIndex((prevIndex) => 
            prevIndex === 0 ? response.length - 1 : prevIndex - 1
        )
    }

    const getPrevIndex = () => currentIndex === 0 ? response.length - 1 : currentIndex - 1
    const getNextIndex = () => currentIndex === response.length - 1 ? 0 : currentIndex + 1

    const prevReviewData = response[getPrevIndex()]
    const currentReviewData = response[currentIndex]
    const nextReviewData = response[getNextIndex()]

    return (
        <div className="review-container">
            <div className="review-header">
                <h1>This Is What Our Customers Say</h1>
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Scelerisque duis</p>
            </div>
            
            <div className="review-carousel-container">
                <div className="review-stack">
                    {/* Previous review - left side, behind */}
                    <div className="review-item review-item-prev">
                        <div className="review-item-img">
                            <img src={prevReviewData.user_img} alt={prevReviewData.user_fullName} />
                        </div>
                        <div className="review-item-content">
                            <p>{prevReviewData.comment}</p>
                            <div className="review-separator"></div>
                            <div className="reviewer-info">
                                <div className="reviewer-name">{prevReviewData.user_fullName}</div>
                            </div>
                        </div>
                    </div>

                    {/* Next review - right side, behind */}
                    <div className="review-item review-item-next">
                        <div className="review-item-img">
                            <img src={nextReviewData.user_img} alt={nextReviewData.user_fullName} />
                        </div>
                        <div className="review-item-content">
                            <p>{nextReviewData.comment}</p>
                            <div className="review-separator"></div>
                            <div className="reviewer-info">
                                <div className="reviewer-name">{nextReviewData.user_fullName}</div>
                            </div>
                        </div>
                    </div>

                    {/* Current review - center, on top */}
                    <div className="review-item review-item-current">
                        <div className="review-item-img">
                            <img src={currentReviewData.user_img} alt={currentReviewData.user_fullName} />
                        </div>
                        <div className="review-item-content">
                            <p>{currentReviewData.comment}</p>
                            <div className="review-separator"></div>
                            <div className="reviewer-info">
                                <div className="reviewer-name">{currentReviewData.user_fullName}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div className="carousel-nav">
                <button 
                    className="nav-btn" 
                    onClick={goToPrevReview}
                >
                    ‹
                </button>
                <button 
                    className="nav-btn" 
                    onClick={goToNextReview}
                >
                    ›
                </button>
            </div>
        </div>
    )
}