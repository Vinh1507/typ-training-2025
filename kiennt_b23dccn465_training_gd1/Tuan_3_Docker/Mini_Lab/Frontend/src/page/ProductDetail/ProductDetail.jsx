import { useEffect, useMemo, useState, useRef } from "react";
import { useParams, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { base } from "../../service/Base.jsx";
import { App } from "antd";
import ProductFeedback from "./ProductFeedback.jsx";
import ProductCard from "../../components/ProductCard.jsx";
import Breadcrumb from "../../components/Breadcrumb.jsx";
import "./ProductDetail.css";

export default function ProductDetail() {
	const { id } = useParams();
	const location = useLocation();
	const navigate = useNavigate();
	const { message } = App.useApp();
	const [product, setProduct] = useState(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);
	const [addingToCart, setAddingToCart] = useState(false);
	const [viewHistory, setViewHistory] = useState([]);
	const [relatedProducts, setRelatedProducts] = useState([]);
const relatedRowRef = useRef(null);
const historyRowRef = useRef(null);
const scrollRelated = (delta) => {
    try {
        if (relatedRowRef.current) {
            const container = relatedRowRef.current;
            const step = delta === 0 ? container.clientWidth : delta;
            container.scrollBy({ left: step, behavior: 'smooth' });
        }
    } catch { return; }
};
const scrollHistory = (delta) => {
    try {
        if (historyRowRef.current) {
            const container = historyRowRef.current;
            const step = delta === 0 ? container.clientWidth : delta;
            container.scrollBy({ left: step, behavior: 'smooth' });
        }
    } catch { return; }
};

	const mapToCardProduct = (item = {}) => ({
		id: item.productId ?? item.id ?? item.prod_id ?? null,
		prod_id: item.prod_id ?? item.productId ?? item.id ?? null,
		title: item.title || "",
		price: item.price || 0,
		thumbnail: item.image || item.thumbnail || "",
		Discount:
			item.discount ??
			item.Discount ??
			(item.saleValue && item.saleValue > 0 ? Math.round(item.saleValue * 100) : 0),
		list_product_variation: Array.isArray(item.list_product_variation)
			? item.list_product_variation
			: Array.isArray(item.listVariations)
			? item.listVariations
			: Array.isArray(item.list_prod_variation)
			? item.list_prod_variation
			: [],
	});

const buildHistoryEntry = (item = {}) => ({
		id: item.productId ?? item.id ?? null,
		prod_id: item.prod_id ?? item.id ?? item.productId ?? null,
		title: item.title || "",
		price: item.price || 0,
		thumbnail: item.image || item.thumbnail || "",
		Discount:
			item.discount ??
			item.Discount ??
			(item.saleValue && item.saleValue > 0 ? Math.round(item.saleValue * 100) : 0),
		categoryId: item.categoryId ?? null,
	// lưu thời điểm xem; nếu đã có thì giữ nguyên
	viewedAt: item.viewedAt || Date.now(),
		list_product_variation: Array.isArray(item.list_product_variation)
			? item.list_product_variation
			: Array.isArray(item.list_prod_variation)
			? item.list_prod_variation
			: [],
	});

useEffect(() => {
    try {
        const raw = localStorage.getItem("view_history");
        if (!raw) return;
        const parsed = JSON.parse(raw);
        if (Array.isArray(parsed)) {
            const now = Date.now();
            const threeDays = 3 * 24 * 60 * 60 * 1000;
            const cleaned = parsed
                .map(buildHistoryEntry)
                .filter(entry => entry.id && now - (entry.viewedAt || 0) <= threeDays);
            setViewHistory(cleaned);
            // đồng bộ lại localStorage sau khi loại bỏ item quá hạn
            localStorage.setItem("view_history", JSON.stringify(cleaned));
        }
    } catch {
        setViewHistory([]);
    }
}, []);

	const currentProductId = product?.productId ?? null;
	const historyToShow = useMemo(
		() => viewHistory.filter((item) => item.id && item.id !== currentProductId),
		[viewHistory, currentProductId]
	);

	// Đảm bảo khi vào trang chi tiết luôn ở đầu trang
	useEffect(() => {
		try {
			window.scrollTo({ top: 0, left: 0, behavior: 'auto' });
		} catch { return; }
	}, [id]);

	// Fetch product data from API
	useEffect(() => {
		const fetchProduct = async () => {
			if (!id) {
				setError("Không tìm thấy ID sản phẩm");
				setLoading(false);
				return;
			}

			try {
				setLoading(true);
				setError(null);
				const response = await axios.get(`${base}/products/${id}`);

				if (response.data && response.data.code === 1000 && response.data.result) {
					const productData = response.data.result;
					setProduct(productData);

                    // Update view history (giữ tối đa 10 và không quá 3 ngày)
					try {
						const rawHistory = localStorage.getItem("view_history") || "[]";
						const parsedJson = JSON.parse(rawHistory);
						const parsed = Array.isArray(parsedJson) ? parsedJson : [];
                        const now = Date.now();
                        const threeDays = 3 * 24 * 60 * 60 * 1000;
                        const history = parsed
                            .map(buildHistoryEntry)
                            .filter((item) => item.id && now - (item.viewedAt || 0) <= threeDays);
                        const entry = buildHistoryEntry({ ...productData, viewedAt: now });
                        const filtered = history.filter((item) => item.id !== entry.id);
                        const nextHistory = [entry, ...filtered].slice(0, 10);
                        localStorage.setItem("view_history", JSON.stringify(nextHistory));
                        setViewHistory(nextHistory);
					} catch {
						// ignore localStorage errors
					}

					// Fetch products in the same category
					try {
						const allResp = await axios.get(`${base}/products`);
						if (allResp.status === 200 && Array.isArray(allResp.data?.result)) {
							const allProducts = allResp.data.result || [];
							let currentCategoryId = productData.categoryId;

							if (currentCategoryId == null) {
								const matched = allProducts.find((p) => p.productId === productData.productId);
								currentCategoryId = matched?.categoryId ?? null;
							}

							const sameCategory = allProducts
								.filter(
									(p) =>
										currentCategoryId != null &&
										p.categoryId === currentCategoryId &&
										p.productId !== productData.productId
								)
								.map(mapToCardProduct)
								.filter((p) => p.id != null)
								.slice(0, 8);

							setRelatedProducts(sameCategory);
						} else {
							setRelatedProducts([]);
						}
					} catch {
						setRelatedProducts([]);
					}
				} else {
					setError("Không thể tải thông tin sản phẩm");
					setRelatedProducts([]);
				}
			} catch (err) {
				console.error("Error fetching product:", err);
				setError(err?.response?.data?.message || "Có lỗi khi tải thông tin sản phẩm");
			} finally {
				setLoading(false);
			}
		};

		fetchProduct();
	}, [id]);

	// Transform API data to component format
	const RESPONSE = useMemo(() => {
		if (!product) return null;

		// API c+� th�+� trߦ� v�+� "listVariations" (-�+� nh+�m sߦ�n theo m+�u) hoߦ+c "variations" (array phߦ�ng)
		let list_prod_variation = [];
		
		if (product.listVariations && Array.isArray(product.listVariations)) {
			// Format m�+�i: listVariations -�+� -榦�+�c nh+�m sߦ�n theo m+�u sߦ�c
			list_prod_variation = product.listVariations.map((group) => ({
				product_id: group.productId || product.productId,
				image: group.image || product.image,
				color: group.color,
				list: (group.list || []).map((item) => ({
					id_variation: item.idVariation || item.id,
					size: item.size,
					stock_quantity: item.stockQuantity || 0
				}))
			}));
		} else if (product.variations && Array.isArray(product.variations)) {
			// Format c+�: variations l+� array phߦ�ng, cߦ�n group theo m+�u
			const variationsByColor = {};
			
			product.variations.forEach((variation) => {
				if (!variation.color) return;
				
				if (!variationsByColor[variation.color]) {
					variationsByColor[variation.color] = {
						product_id: product.productId,
						image: variation.image || product.image,
						color: variation.color,
						list: []
					};
				}
				
				variationsByColor[variation.color].list.push({
					id_variation: variation.id,
					size: variation.size,
					stock_quantity: variation.stockQuantity || 0
				});
			});
			
			list_prod_variation = Object.values(variationsByColor);
		}

		return {
			id: product.productId,
			title: product.title || "",
			description: product.description || "",
			price: product.price || 0,
			thumbnail: product.image || "",
			rate: 0, // API kh+�ng trߦ� v�+� rate, mߦ+c -��+�nh 0
			discount: product.saleValue && product.saleValue > 0 ? Math.round(product.saleValue * 100) : undefined, // Convert 0.90 to 90, ch�+� set khi > 0
			list_prod_variation: list_prod_variation
		};
	}, [product]);

	// Tߦ�o gallery ߦ�nh (ߦ�nh ch+�nh + ߦ�nh c+�c biߦ+n th�+�) G�� m�+�i entry c+� key ri+�ng -��+�
	// kh+�ng g�+�p lߦ�i d+� tr+�ng URL ߦ�nh
    const galleryEntries = useMemo(() => {
		if (!RESPONSE) return [];
		const entries = [];
		if (RESPONSE.thumbnail) {
			entries.push({ key: "main", src: RESPONSE.thumbnail });
		}
        (RESPONSE.list_prod_variation || []).forEach((group, idx) => {
            if (group && group.image) entries.push({ key: `color-${idx}`, src: group.image, variation: group });
		});
		return entries;
	}, [RESPONSE]);

    const [activeKey, setActiveKey] = useState(null);
	const activeEntry = useMemo(() => {
		if (!activeKey) return null;
		return galleryEntries.find(e => e.key === activeKey);
	}, [activeKey, galleryEntries]);
	
	// Set initial activeKey when galleryEntries changes
	useEffect(() => {
		if (galleryEntries.length > 0 && !activeKey) {
			setActiveKey(galleryEntries[0]?.key);
		}
	}, [galleryEntries, activeKey]);

	const activeImg = activeEntry?.src || "";
    const firstColorGroup = RESPONSE?.list_prod_variation?.[0];
    const [selectedSize, setSelectedSize] = useState("");
    const [selectedColor, setSelectedColor] = useState("");
	const [qty, setQty] = useState(1);

	// Initialize selectedSize and selectedColor when firstColorGroup is available
	useEffect(() => {
		if (firstColorGroup && !selectedSize && !selectedColor) {
			setSelectedSize(firstColorGroup.list?.[0]?.size || "");
			setSelectedColor(firstColorGroup.color || "");
		}
	}, [firstColorGroup, selectedSize, selectedColor]);

	const hasDiscount = RESPONSE?.discount && RESPONSE.discount > 0;
	const finalPrice = RESPONSE ? (hasDiscount ? Math.round(RESPONSE.price * (1 - RESPONSE.discount / 100)) : RESPONSE.price) : 0;

    const selectedVariation = activeEntry?.variation; // nh+�m theo m+�u hi�+�n tߦ�i
    const sizes = useMemo(() => {
        return selectedVariation?.list?.map(item => item.size) || [];
    }, [selectedVariation]);

    // -��+�ng b�+� color/size theo nh+�m m+�u -�ang active
	useEffect(() => {
        if (selectedVariation) {
            setSelectedColor(selectedVariation.color);
            if (!selectedVariation.list?.some(i => i.size === selectedSize)) {
                const nextSize = selectedVariation.list?.[0]?.size || "";
                setSelectedSize(nextSize);
            }
        }
	}, [selectedVariation, selectedSize]);

	const full = Math.floor(RESPONSE?.rate || 0);
	const hasHalf = (RESPONSE?.rate || 0) - full >= 0.5;
	const empty = 5 - full - (hasHalf ? 1 : 0);

	// Add to cart handler
	const handleAddToCart = async () => {
		// Check if user is logged in
		const token = localStorage.getItem('token');
		if (!token) {
			message.warning('Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng');
			navigate('/login');
			return;
		}

		// Find the selected variation ID
		const matched = selectedVariation?.list?.find(i => i.size === selectedSize);
		if (!matched) {
			message.error('Vui lòng chọn kích thước hợp lệ');
			return;
		}

		// Get variation ID - support multiple formats
		const variationId = matched.id_variation || matched.idVariation || matched.id;
		if (!variationId) {
			console.error('Variation ID not found:', matched);
			message.error('Không tìm thấy ID biến thể. Vui lòng thử lại.');
			return;
		}

		// Check stock
		const stockQty = matched.stock_quantity || matched.stockQuantity || 0;
		if (stockQty <= 0) {
			message.error('Sản phẩm đã hết hàng');
			return;
		}

		// Check quantity
		if (qty > stockQty) {
			message.error(`Chỉ còn ${stockQty} sản phẩm trong kho`);
			return;
		}

		setAddingToCart(true);
		try {
			console.log('Adding to cart:', {
				product_variation_id: variationId,
				quantity: qty,
				selectedSize,
				selectedColor
			});

			const response = await axios.post(`${base}/cart`, {
				product_variation_id: variationId,
				quantity: qty
			}, {
				headers: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
				}
			});

			console.log('Add to cart response:', response);

			if (response.status === 200 || response.status === 201) {
				message.success('Đã thêm sản phẩm vào giỏ hàng!');
				// Reset quantity after successful add
				setQty(1);
			} else {
				const errorMsg = response.data?.message || response.data?.result?.message || 'Không thể thêm vào giỏ hàng';
				message.error(errorMsg);
			}
		} catch (err) {
			console.error('Error adding to cart:', err);
			console.error('Error response:', err?.response?.data);
			
			// Handle different error formats
			let errorMsg = 'Có lỗi khi thêm vào giỏ hàng';
			
			if (err?.response?.data) {
				errorMsg = err.response.data.message 
					|| err.response.data.result?.message 
					|| err.response.data.error 
					|| JSON.stringify(err.response.data);
			} else if (err?.message) {
				errorMsg = err.message;
			}
			
			message.error(errorMsg);
		} finally {
			setAddingToCart(false);
		}
	};

	// Breadcrumb items
	const breadcrumbItems = useMemo(() => {
		const items = [{ label: "Trang chủ", path: "/" }];
		
		// Th+�m breadcrumb cho trang tr���+�c -�+� nߦ+u c+� (NewArrivals hoߦ+c Category)
		const fromState = location.state;
		if (fromState?.from === 'newArrivals') {
			items.push({ 
				label: fromState.fromLabel || 'New Arrivals', 
				path: fromState.fromPath || '/newArrivals' 
			});
		} else if (fromState?.from === 'category') {
			items.push({ 
				label: fromState.fromLabel || 'Danh mục', 
				path: fromState.fromPath 
			});
		}
		
		// Thêm tên sản phẩm hoặc "Chi tiết sản phẩm"
		if (product && product.title) {
			items.push({ label: product.title });
		} else {
			items.push({ label: "Chi tiết sản phẩm" });
		}
		
		return items;
	}, [product, location.state]);

	// Loading state - Skeleton loading
	if (loading) {
		return (
			<>
			<Breadcrumb items={[{ label: "Trang chủ", path: "/" }, { label: "Chi tiết sản phẩm" }]} />
				<div className="pd-container">
				{/* Gallery skeleton */}
				<div className="pd-gallery">
					<div className="pd-main skeleton-image">
						<div className="skeleton-placeholder"></div>
					</div>
				</div>

				{/* Info skeleton */}
				<div className="skeleton-content">
					<div className="skeleton-line skeleton-meta"></div>
					<div className="skeleton-line skeleton-title"></div>
					<div className="skeleton-line skeleton-rating"></div>
					<div className="skeleton-line skeleton-price"></div>
					<div className="skeleton-line skeleton-description"></div>
					<div className="skeleton-line skeleton-description-short"></div>
					
					<div className="skeleton-options">
						<div className="skeleton-line skeleton-option-label"></div>
						<div className="skeleton-dots">
							<div className="skeleton-dot"></div>
							<div className="skeleton-dot"></div>
						</div>
					</div>
					
					<div className="skeleton-options">
						<div className="skeleton-line skeleton-option-label"></div>
						<div className="skeleton-chips">
							<div className="skeleton-chip"></div>
							<div className="skeleton-chip"></div>
							<div className="skeleton-chip"></div>
						</div>
					</div>
					
					<div className="skeleton-actions">
						<div className="skeleton-button"></div>
						<div className="skeleton-button secondary"></div>
					</div>
				</div>
			</div>
			</>
		);
	}

	// Error state
	if (error || !RESPONSE) {
		return (
			<>
				<Breadcrumb items={breadcrumbItems} />
				<div className="pd-container">
				<div style={{ textAlign: "center", padding: "40px", color: "#d32f2f" }}>
					<p>{error || "Không tìm thấy sản phẩm"}</p>
				</div>
			</div>
			</>
		);
	}

	return (
		<>
			<Breadcrumb items={breadcrumbItems} />
			<div className="pd-container">
		{/* Gallery trái */}
			<div className="pd-gallery">
				<div className="pd-main">
					<img src={activeImg} alt={RESPONSE.title} />
				</div>
			</div>

		{/* Thông tin phải */}
			<div>
				<div className="pd-meta">LOK SHOP</div>
				<h1 className="pd-title">{RESPONSE.title}</h1>
				<div className="pd-meta">
					<div>
					{Array.from({ length: full }).map((_, i) => (<span key={"f"+i}>★</span>))}
					{hasHalf && <span>☆</span>}
					{Array.from({ length: empty }).map((_, i) => (<span key={"e"+i}>☆</span>))}
					</div>
					<span>({RESPONSE.rate})</span>
				</div>
				<div className="pd-price">
					{hasDiscount && <span className="pd-badge">SAVE {RESPONSE.discount}%</span>}
					{hasDiscount && <span className="old">{Math.round(RESPONSE.price).toLocaleString('vi-VN')}₫</span>}
					<span className={hasDiscount ? "now" : "normal"}>{Math.round(finalPrice).toLocaleString('vi-VN')}₫</span>
				</div>
				<p>{RESPONSE.description}</p>

                <div className="pd-options">
					{/* Màu sắc: <color> và các nút tròn chọn variation */}
                    <div className="opt-row" style={{ alignItems: "flex-start", flexDirection: "column" }}>
						<span>Màu sắc: <strong>{selectedColor}</strong></span>
					{galleryEntries.some(e => e.key !== "main" && e.variation) && (
						<div className="pd-variations">
							<div className="pd-variation-dots">
                                {galleryEntries.filter(e => e.key !== "main" && e.variation).map(e => (
                                    <span key={e.key} className="var-dot-wrap">
                                        <span className="var-tooltip">{e.variation.color}</span>
                                        <button
                                            className={"var-dot" + (activeKey === e.key ? " active" : "")}
                                            style={{ backgroundImage: `url(${e.src})` }}
									aria-label={`Chọn ${e.variation.color}`}
                                            onClick={() => {
                                                setActiveKey(e.key);
                                                setSelectedColor(e.variation.color);
                                                const sz = e.variation.list?.[0]?.size || "";
                                                setSelectedSize(sz);
                                            }}
                                            type="button"
                                        />
                                    </span>
                                ))}
							</div>
						</div>
					)}
                    </div>

					{/* Kích thước: <size> và danh sách size của màu đang chọn */}
                    <div className="opt-row" style={{ alignItems: "flex-start", flexDirection: "column" }}>
						<span>Kích thước: <strong>{selectedSize}</strong></span>
                        <div style={{ display: "flex", gap: 8, flexWrap: "wrap", marginTop: 8 }}>
                            {sizes.map(s => (
                                <button key={s} className={"chip" + (selectedSize === s ? " active" : "")} onClick={() => setSelectedSize(s)} type="button">{s}</button>
                            ))}
                        </div>
                    </div>

					{/* Tồn kho theo size + color đang chọn */}
                    {(() => {
                        const matched = selectedVariation?.list?.find(i => i.size === selectedSize);
						const qtyText = matched ? (matched.stock_quantity > 0 ? `Còn ${matched.stock_quantity} sản phẩm` : "Hết hàng") : "Không có tồn kho cho lựa chọn này";
						return <div className="opt-row" style={{ color: "#666" }}>Stock: {qtyText}</div>;
					})()}
					<div className="opt-row">
						<span>Quantity:</span>
						<div className="qty">
							<button type="button" onClick={() => setQty(q => Math.max(1, q - 1))}>-</button>
							<input readOnly value={qty} />
							<button type="button" onClick={() => setQty(q => q + 1)}>+</button>
						</div>
					</div>
				</div>

				<div className="pd-actions">
					<button 
						className="btn" 
						type="button" 
						onClick={handleAddToCart}
						disabled={addingToCart}
					>
						{addingToCart ? 'Đang thêm...' : 'Add to cart'}
					</button>
					<button className="btn secondary" type="button">Buy now</button>
				</div>

				<ProductFeedback />
			</div>
		</div>
		{(relatedProducts.length > 0 || historyToShow.length > 0) && (
			<div style={{ marginTop: 48 }}>
				{relatedProducts.length > 0 && (
					<>
						<h2 style={{ margin: "24px 0" }}>Bạn có thể quan tâm</h2>
						<div style={{ position: 'relative' }}>
							<div
								ref={relatedRowRef}
								style={{ display: 'flex', gap: 16, overflow: 'hidden', scrollBehavior: 'smooth', paddingBottom: 8 }}
							>
								{relatedProducts.map((item) => (
									<div key={item.id} style={{ flex: '0 0 calc((100% - 48px)/4)' }}>
										<ProductCard product={item} />
									</div>
								))}
							</div>
							{relatedProducts.length > 4 && (
								<>
									<button
										type="button"
										onClick={() => scrollRelated(-relatedRowRef.current.clientWidth || -0)}
										style={{ position: 'absolute', left: 0, top: '40%', transform: 'translateY(-50%)', background: 'rgba(0,0,0,0.5)', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 10px', cursor: 'pointer' }}
									>&lt;</button>
									<button
										type="button"
										onClick={() => scrollRelated(relatedRowRef.current.clientWidth || 0)}
										style={{ position: 'absolute', right: 0, top: '40%', transform: 'translateY(-50%)', background: 'rgba(0,0,0,0.5)', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 10px', cursor: 'pointer' }}
									>&gt;</button>
								</>
							)}
						</div>
					</>
				)}

				{historyToShow.length > 0 && (
					<>
						<h2 style={{ margin: "32px 0 24px" }}>Sản phẩm đã xem</h2>
						<div style={{ position: 'relative' }}>
							<div
								ref={historyRowRef}
								style={{ display: 'flex', gap: 16, overflow: 'hidden', scrollBehavior: 'smooth', paddingBottom: 8 }}
							>
								{historyToShow.map((item) => (
									<div key={item.id} style={{ flex: '0 0 calc((100% - 48px)/4)' }}>
										<ProductCard product={mapToCardProduct(item)} />
									</div>
								))}
							</div>
							{historyToShow.length > 4 && (
								<>
									<button
										type="button"
										onClick={() => scrollHistory(-historyRowRef.current.clientWidth || -0)}
										style={{ position: 'absolute', left: 0, top: '40%', transform: 'translateY(-50%)', background: 'rgba(0,0,0,0.5)', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 10px', cursor: 'pointer' }}
									>&lt;</button>
									<button
										type="button"
										onClick={() => scrollHistory(historyRowRef.current.clientWidth || 0)}
										style={{ position: 'absolute', right: 0, top: '40%', transform: 'translateY(-50%)', background: 'rgba(0,0,0,0.5)', color: '#fff', border: 'none', borderRadius: 4, padding: '8px 10px', cursor: 'pointer' }}
									>&gt;</button>
								</>
							)}
						</div>
					</>
				)}
			</div>
		)}
		</>
	);
}

