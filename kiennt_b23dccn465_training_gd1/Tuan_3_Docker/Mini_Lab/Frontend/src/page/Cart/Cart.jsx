import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { base } from "../../service/Base.jsx";
import { App } from "antd";
import { Trash2 } from "lucide-react";
import Breadcrumb from "../../components/Breadcrumb.jsx";
import "./Cart.css";

export default function Cart() {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [orderNote, setOrderNote] = useState("");
    const [selectedItems, setSelectedItems] = useState(new Set());
    const navigate = useNavigate();
    const { message } = App.useApp();

    // Breadcrumb items
    const breadcrumbItems = [
        { label: "Trang chủ", path: "/" },
        { label: "Giỏ hàng" }
    ];

    // Fetch cart data từ API
    useEffect(() => {
        const fetchCartData = async () => {
            // Check if user is logged in
            const token = localStorage.getItem('token');
            if (!token) {
                setLoading(false);
                setCartItems([]);
                return;
            }

            try {
                setLoading(true);
                
                // Fetch cart từ API
                const cartResponse = await axios.get(`${base}/cart`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                
                if (cartResponse.status === 200 && cartResponse.data?.result) {
                    const cartData = cartResponse.data.result;
                    
                    // cartData có thể là array hoặc object với result array
                    const cartArray = Array.isArray(cartData) ? cartData : (cartData.result || []);
                    
                    if (cartArray.length === 0) {
                        setCartItems([]);
                        setLoading(false);
                        return;
                    }
                    
                    // Với mỗi item trong cart, fetch thông tin chi tiết sản phẩm
                    const itemsWithDetails = await Promise.all(
                        cartArray.map(async (item) => {
                            try {
                                const productResponse = await axios.get(`${base}/products/${item.product_id}`, {
                                    headers: {
                                        'Authorization': `Bearer ${token}`
                                    }
                                });
                                
                                if (productResponse.status === 200 && productResponse.data?.result) {
                                    const productData = productResponse.data.result;
                                    
                                    // Tìm variation tương ứng
                                    const selectedVariation = findVariationById(productData, item.product_variation_id);
                                    
                                    return {
                                        id: item.id, // cart item ID để dùng cho update/delete
                                        product_id: item.product_id,
                                        product_variation_id: item.product_variation_id,
                                        quantity: item.quantity,
                                        product: productData,
                                        selectedVariation: selectedVariation
                                    };
                                }
                                return null;
                            } catch (err) {
                                console.error(`Error fetching product ${item.product_id}:`, err);
                                return null;
                            }
                        })
                    );
                    
                    // Lọc bỏ items null (fetch failed)
                    setCartItems(itemsWithDetails.filter(item => item !== null));
                } else {
                    setCartItems([]);
                }
            } catch (error) {
                console.error("Error fetching cart data:", error);
                message.error(error?.response?.data?.message || "Có lỗi khi tải giỏ hàng");
                
                // Nếu lỗi 401 (unauthorized), redirect to login
                if (error?.response?.status === 401) {
                    message.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.');
                    localStorage.removeItem('token');
                    navigate('/login');
                }
            } finally {
                setLoading(false);
            }
        };

        fetchCartData();
    }, [navigate, message]);

    // Helper function để tìm variation theo ID
    const findVariationById = (product, variationId) => {
        // Xử lý cho format API mới (listVariations)
        if (product.listVariations && Array.isArray(product.listVariations)) {
            for (const colorGroup of product.listVariations) {
                for (const variation of colorGroup.list || []) {
                    const varId = variation.idVariation || variation.id;
                    if (varId === variationId) {
                        return {
                            id_variation: varId,
                            size: variation.size,
                            stock_quantity: variation.stockQuantity || 0,
                            color: colorGroup.color,
                            image: colorGroup.image || product.image
                        };
                    }
                }
            }
        }
        
        // Xử lý cho format API cũ (variations array)
        if (product.variations && Array.isArray(product.variations)) {
            const variation = product.variations.find(v => v.id === variationId);
            if (variation) {
                return {
                    id_variation: variation.id,
                    size: variation.size,
                    stock_quantity: variation.stockQuantity || 0,
                    color: variation.color,
                    image: variation.image || product.image
                };
            }
        }
        
        return null;
    };

    // Tính tổng tiền chỉ cho những item được chọn
    const calculateTotal = () => {
        return cartItems.reduce((total, item) => {
            const itemKey = `${item.product_id}-${item.product_variation_id}`;
            if (selectedItems.has(itemKey)) {
                const price = item.product.price;
                const discount = item.product.discount || 0;
                const finalPrice = price * (1 - discount / 100);
                return total + (finalPrice * item.quantity);
            }
            return total;
        }, 0);
    };

    // Toggle chọn/bỏ chọn item
    const toggleItemSelection = (productId, variationId) => {
        const itemKey = `${productId}-${variationId}`;
        setSelectedItems(prev => {
            const newSet = new Set(prev);
            if (newSet.has(itemKey)) {
                newSet.delete(itemKey);
            } else {
                newSet.add(itemKey);
            }
            return newSet;
        });
    };

    // Chọn tất cả
    const selectAll = () => {
        const allKeys = cartItems.map(item => `${item.product_id}-${item.product_variation_id}`);
        setSelectedItems(new Set(allKeys));
    };

    // Bỏ chọn tất cả
    const deselectAll = () => {
        setSelectedItems(new Set());
    };

    // Cập nhật số lượng
    const updateQuantity = async (productId, variationId, newQuantity) => {
        if (newQuantity < 1) return;
        
        const token = localStorage.getItem('token');
        if (!token) {
            message.error('Vui lòng đăng nhập để cập nhật giỏ hàng');
            return;
        }

        // Tìm cart item để lấy quantity hiện tại (phục vụ revert nếu lỗi)
        const cartItem = cartItems.find(
            item => item.product_id === productId && item.product_variation_id === variationId
        );
        if (!cartItem) {
            message.error('Không tìm thấy sản phẩm trong giỏ hàng');
            return;
        }

        // Optimistic update
        setCartItems(prev => 
            prev.map(item => 
                item.product_id === productId && item.product_variation_id === variationId
                    ? { ...item, quantity: newQuantity }
                    : item
            )
        );

        try {
            // Thử endpoint mới: /cart/{product_variation_id}
            const response = await axios.put(`${base}/cart/${variationId}`, {
                quantity: newQuantity
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.status === 200 || response.status === 204) {
                // Success - đã update optimistically rồi, không cần hiện thông báo
                return;
            }

            // Nếu không thành công rõ ràng, ném lỗi để vào nhánh catch xử lý chung
            throw new Error('Unexpected status when updating quantity');
        } catch (error) {
            const shouldFallback = error?.response?.status === 404 || error?.response?.status === 405;
            if (shouldFallback && cartItem.id) {
                try {
                    // Fallback endpoint cũ: /cart/update/{cart_item_id}
                    const legacy = await axios.put(`${base}/cart/update/${cartItem.id}`, {
                        quantity: newQuantity
                    }, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });

                    if (legacy.status === 200 || legacy.status === 204) {
                        return; // đã update thành công theo endpoint cũ
                    }
                } catch {
                    // Tiếp tục xuống để revert và báo lỗi bằng error ban đầu
                }
            }

            // Revert khi thất bại
            setCartItems(prev => 
                prev.map(item => 
                    item.product_id === productId && item.product_variation_id === variationId
                        ? { ...item, quantity: cartItem.quantity }
                        : item
                )
            );
            message.error(error?.response?.data?.message || 'Có lỗi khi cập nhật số lượng');
        }
    };

    // Xóa item khỏi cart
    const removeItem = async (productId, variationId) => {
        const token = localStorage.getItem('token');
        if (!token) {
            message.error('Vui lòng đăng nhập');
            return;
        }

        // Tìm cart item ID
        const cartItem = cartItems.find(
            item => item.product_id === productId && item.product_variation_id === variationId
        );
        
        if (!cartItem) {
            message.error('Không tìm thấy sản phẩm trong giỏ hàng');
            return;
        }

        // Optimistic delete
        const oldItems = [...cartItems];
        setCartItems(prev => 
            prev.filter(item => 
                !(item.product_id === productId && item.product_variation_id === variationId)
            )
        );

        // Also remove from selected items
        const itemKey = `${productId}-${variationId}`;
        setSelectedItems(prev => {
            const newSet = new Set(prev);
            newSet.delete(itemKey);
            return newSet;
        });

        try {
            // Thử endpoint mới: /cart/{product_variation_id}
            const response = await axios.delete(`${base}/cart/${variationId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.status === 200 || response.status === 204) {
                message.success('Đã xóa sản phẩm khỏi giỏ hàng');
                return;
            }

            // Nếu không thành công rõ ràng, ném lỗi để vào nhánh catch
            throw new Error('Unexpected status when deleting cart item');
        } catch (error) {
            const shouldFallback = error?.response?.status === 404 || error?.response?.status === 405;
            if (shouldFallback && cartItem.id) {
                try {
                    // Fallback endpoint cũ: /cart/remove/{cart_item_id}
                    const legacy = await axios.delete(`${base}/cart/remove/${cartItem.id}`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });

                    if (legacy.status === 200 || legacy.status === 204) {
                        message.success('Đã xóa sản phẩm khỏi giỏ hàng');
                        return;
                    }
                } catch {
                    // Tiếp tục xuống để revert và báo lỗi bằng error ban đầu
                }
            }

            // Revert on error
            setCartItems(oldItems);
            message.error(error?.response?.data?.message || 'Có lỗi khi xóa sản phẩm');
        }
    };

    // Xử lý checkout (tạm thôi, sau bổ sung sau)
    const handleCheckout = () => {
        const selectedItemsList = cartItems.filter(item => {
            const itemKey = `${item.product_id}-${item.product_variation_id}`;
            return selectedItems.has(itemKey);
        });
        
        if (selectedItemsList.length === 0) {
            alert("Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
            return;
        }
        
        console.log("Checkout with selected items:", selectedItemsList);
        console.log("Order note:", orderNote);
        alert("Tính năng thanh toán đang được phát triển!");
    };

    if (loading) {
        return (
            <>
                <Breadcrumb items={breadcrumbItems} />
                <div className="cart-container">
                    <div className="loading">Đang tải giỏ hàng...</div>
                </div>
            </>
        );
    }

    if (cartItems.length === 0) {
        return (
            <>
                <Breadcrumb items={breadcrumbItems} />
                <div className="cart-container">
                <div className="empty-cart">
                    <div className="empty-cart-icon">
                        <div className="shopping-bag">🛍️</div>
                        <div className="empty-x">❌</div>
                    </div>
                    <h2>Giỏ Hàng Của Bạn Đang Trống</h2>
                    <p>Mua Sắm Ngay tại trang chủ nhé!!!</p>
                    <button 
                        className="shop-now-btn"
                        onClick={() => navigate('/')}
                    >
                        Mua sắm ngay
                    </button>
                </div>
            </div>
            </>
        );
    }

    return (
        <>
            <Breadcrumb items={breadcrumbItems} />
            <div className="cart-container">
            <h1>Giỏ hàng</h1>
            
            <div className="cart-content">
                <div className="cart-items">
                    {/* Select All Controls */}
                    <div className="select-all-controls">
                        <button onClick={selectAll} className="select-all-btn">
                            Chọn tất cả
                        </button>
                        <button onClick={deselectAll} className="deselect-all-btn">
                            Bỏ chọn tất cả
                        </button>
                    </div>
                    
                    {cartItems.map((item) => {
                        const itemKey = `${item.product_id}-${item.product_variation_id}`;
                        const isSelected = selectedItems.has(itemKey);
                        
                        return (
                            <div key={itemKey} className={`cart-item ${isSelected ? 'selected' : ''}`}>
                                <div className="item-checkbox">
                                    <input
                                        type="checkbox"
                                        checked={isSelected}
                                        onChange={() => toggleItemSelection(item.product_id, item.product_variation_id)}
                                    />
                                </div>
                                
                                <button 
                                    className="remove-item"
                                    onClick={() => removeItem(item.product_id, item.product_variation_id)}
                                >
                                    <Trash2 size={16} />
                                </button>
                            
                            <div className="product-image">
                                <img 
                                    src={item.selectedVariation?.image || item.product.thumbnail} 
                                    alt={item.product.title}
                                    onError={(e) => {
                                        e.target.src = item.product.thumbnail;
                                    }}
                                />
                            </div>
                            
                            <div className="product-info">
                                <h3 className="product-title">{item.product.title}</h3>
                                <div className="product-variant">
                                    {item.selectedVariation?.color} / {item.selectedVariation?.size}
                                </div>
                                <div className="product-price">
                                    {item.product.discount ? (
                                        <>
                                            <span className="price-old">
                                                {item.product.price.toLocaleString()}₫
                                            </span>
                                            <span className="price-now">
                                                {Math.round(item.product.price * (1 - item.product.discount / 100)).toLocaleString()}₫
                                            </span>
                                        </>
                                    ) : (
                                        <span className="price-normal">
                                            {item.product.price.toLocaleString()}₫
                                        </span>
                                    )}
                                </div>
                            </div>
                            
                            <div className="quantity-controls">
                                <button 
                                    onClick={() => updateQuantity(item.product_id, item.product_variation_id, item.quantity - 1)}
                                    disabled={item.quantity <= 1}
                                >
                                    -
                                </button>
                                <span className="quantity">{item.quantity}</span>
                                <button 
                                    onClick={() => updateQuantity(item.product_id, item.product_variation_id, item.quantity + 1)}
                                >
                                    +
                                </button>
                            </div>
                        </div>
                        );
                    })}
                    
                    <div className="order-notes">
                        <h3>Ghi chú đơn hàng</h3>
                        <textarea
                            value={orderNote}
                            onChange={(e) => setOrderNote(e.target.value)}
                            placeholder="Nhập ghi chú cho đơn hàng..."
                        />
                    </div>
                </div>
                
                <div className="order-summary">
                    <div className="total-section">
                        <h3>TỔNG CỘNG</h3>
                        <div className="total-amount">
                            {calculateTotal().toLocaleString()}₫
                        </div>
                    </div>
                    
                    <button 
                        className="checkout-btn"
                        onClick={handleCheckout}
                    >
                        Thanh Toán
                    </button>
                </div>
            </div>
        </div>
        </>
    );
}