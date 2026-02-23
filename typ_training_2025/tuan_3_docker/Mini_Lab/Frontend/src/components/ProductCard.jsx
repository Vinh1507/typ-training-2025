import "../assets/style/ProductCard.css";
import { useMemo, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function ProductCard({ product, fromPage, categoryName }) {
	// Tính toán giá sau giảm (nếu có)
	// Chỉ có discount khi giá trị > 0 và !== null/undefined
	const hasDiscount = product.Discount && product.Discount > 0;
	const finalPrice = hasDiscount
		? Math.round(product.price * (1 - product.Discount / 100))
		: product.price;

	// Ảnh đang hiển thị: ưu tiên ảnh chính
	const initialImage = product.thumbnail || product?.list_product_variation?.[0]?.thumbnail || "";
	const [currentImage, setCurrentImage] = useState(initialImage);
	const [selectedKey, setSelectedKey] = useState("main");

	// Lấy unique variations (đã được group theo color từ data source)
	const uniqueVariations = useMemo(() => {
		const variations = [];

		// Thêm ảnh chính nếu có
		if (product.thumbnail) {
			variations.push({ 
				key: "main", 
				thumbnail: product.thumbnail,
				color: null 
			});
		}

		// Thêm variations - mỗi item đã đại diện cho 1 color group
		// Data đã được clean ngay từ data source (NAinPage, etc.)
		if (Array.isArray(product.list_product_variation)) {
			product.list_product_variation.forEach((v, index) => {
				if (v && v.thumbnail) {
					// Ưu tiên dùng colorKey nếu có, fallback sang id_variation
					const key = v.colorKey || v.id_variation || `var-${index}`;
					variations.push({ 
						key: key,
						thumbnail: v.thumbnail,
						color: v.color || null
					});
				}
			});
		}

		return variations;
	}, [product.thumbnail, product.list_product_variation]);
	
	const navigate = useNavigate();
	const location = useLocation();
	
	const gotoProductDetail = (id) => {
		// Truyền state để breadcrumb biết đang từ trang nào
		const state = {};
		
		// Ưu tiên dùng prop fromPage nếu có, nếu không thì tự detect
		if (fromPage === 'newArrivals' || location.pathname === '/newArrivals') {
			state.from = 'newArrivals';
			state.fromLabel = 'New Arrivals';
			state.fromPath = '/newArrivals';
		} else if (fromPage === 'category' || location.pathname.startsWith('/category/')) {
			state.from = 'category';
			state.fromLabel = categoryName || location.state?.categoryName || 'Danh mục';
			state.fromPath = location.pathname;
		}
		
		navigate(`/product/${id}`, { state });
	}

	const product_id = product.prod_id || product.id;

	return (
		<div className="product-card" onClick={() => { gotoProductDetail(product_id); }}>
			<div className="product-thumb">
				<img src={currentImage} alt={product.title} />
				{hasDiscount && (
					<span className="badge-discount">-{product.Discount}%</span>
				)}
			</div>
			<div className="product-body">
				<h3 className="product-title">{product.title}</h3>
				
				<div className="product-price">
					{hasDiscount ? (
						<>
							<span className="price-old">{product.price.toLocaleString('vi-VN')}₫</span>
							<span className="price-now">{finalPrice.toLocaleString('vi-VN')}₫</span>
						</>
					) : (
						<span className="price-normal">{finalPrice.toLocaleString('vi-VN')}₫</span>
					)}
				</div>

				{uniqueVariations.length > 0 && (
					<div className="variant-list" role="list">
						{uniqueVariations.map((v) => {
							const isActive = v.key === selectedKey;
							return (
								<button
									key={v.key}
									className={"variant-thumb" + (isActive ? " active" : "")}
									aria-label={v.color ? `Màu ${v.color}` : "Chọn biến thể"}
									title={v.color || ""}
									data-color={v.color || ""}
									onClick={(e) => { 
										e.stopPropagation();
										setCurrentImage(v.thumbnail); 
										setSelectedKey(v.key); 
									}}
									type="button"
								>
									<img src={v.thumbnail} alt={v.color || "variation"} />
								</button>
							);
						})}
					</div>
				)}
			</div>
		</div>
	);
}
