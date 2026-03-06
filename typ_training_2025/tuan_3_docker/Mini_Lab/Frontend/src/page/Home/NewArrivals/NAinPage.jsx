import { useState, useEffect, useMemo, useCallback } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { base } from "../../../service/Base.jsx";
import ProductCard from "../../../components/ProductCard.jsx";
import Breadcrumb from "../../../components/Breadcrumb.jsx";
import "./NewArrivals.css";

export default function NAinPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeSales, setActiveSales] = useState([]);
  
  // Chỉ hiện breadcrumb khi ở trang /newArrivals (không phải trang chủ)
  const showBreadcrumb = location.pathname === '/newArrivals';
  const showViewMoreButton = !showBreadcrumb; // Chỉ hiện nút "Xem thêm" ở homepage

  // Fetch active sales để tính discount
  useEffect(() => {
    const fetchActiveSales = async () => {
      try {
        const response = await axios.get(`${base}/sales`);

        if (response.status === 200 && response.data?.result) {
          const sales = response.data.result || [];
          const now = new Date().getTime();

          // Lọc sales đang active (thời gian hiện tại nằm giữa stDate và endDate)
          const active = sales.filter(s => {
            const stDate = new Date(s.stDate).getTime();
            const endDate = new Date(s.endDate).getTime();
            return now >= stDate && now < endDate;
          });

          setActiveSales(active);
        }
      } catch (error) {
        console.error('Error fetching active sales:', error);
        // Không ảnh hưởng đến việc hiển thị products nếu không fetch được sales
      }
    };

    fetchActiveSales();
  }, []);

  // Fetch products từ API - Không cần authentication
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      setError(null);
      try {
        const response = await axios.get(`${base}/products`);

        if (response.status === 200 && response.data?.result) {
          setProducts(response.data.result || []);
        } else {
          setError('Không thể tải danh sách sản phẩm');
        }
      } catch (err) {
        console.error('Error fetching products:', err);
        setError(err?.response?.data?.message || 'Có lỗi khi tải danh sách sản phẩm');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Helper function để tìm discount cho sản phẩm
  const getProductDiscount = useCallback((productId) => {
    for (const sale of activeSales) {
      const productInSale = sale.list_product?.find(p => 
        (p.id === productId || p.productId === productId)
      );
      if (productInSale) {
        return Math.round((productInSale.value || 0) * 100); // Convert 0.30 to 30
      }
    }
    return 0;
  }, [activeSales]);

  // Helper function: Group variations theo COLOR
  const groupVariationsByColor = useCallback((variations) => {
    if (!Array.isArray(variations) || variations.length === 0) return [];

    const colorMap = {};

    variations.forEach(v => {
      // Chuẩn hoá color key (lowercase, trim)
      const colorKey = (v.color?.trim().toLowerCase() || 'default');
      
      if (!colorMap[colorKey]) {
        colorMap[colorKey] = {
          key: colorKey,
          color: v.color || 'Mặc định',
          representativeImage: v.image, // Ảnh đầu tiên làm đại diện
          images: [v.image],
          sizes: {},
          totalStock: 0,
          variationIds: []
        };
      } else {
        // Tránh trùng ảnh khi nhiều size có cùng file
        if (v.image && !colorMap[colorKey].images.includes(v.image)) {
          colorMap[colorKey].images.push(v.image);
        }
      }

      // Thêm size vào group
      colorMap[colorKey].sizes[v.size] = {
        id: v.id,
        stock: v.stockQuantity || 0
      };
      colorMap[colorKey].totalStock += (v.stockQuantity || 0);
      colorMap[colorKey].variationIds.push(v.id);
    });

    return Object.values(colorMap);
  }, []);

  // Transform và lọc sản phẩm mới (trong 10 ngày gần nhất)
  const newArrivalsProducts = useMemo(() => {
    if (!products || products.length === 0) return [];

    const now = new Date();
    const tenDaysAgo = new Date(now);
    tenDaysAgo.setDate(now.getDate() - 10);

    // Lọc sản phẩm có createdAt trong vòng 10 ngày
    const filtered = products.filter(product => {
      if (!product.createdAt) return false;

      const createdAt = new Date(product.createdAt);
      return createdAt >= tenDaysAgo && createdAt <= now;
    });

    // Transform data để phù hợp với ProductCard
    return filtered.map(product => {
      // Transform variations: Group theo COLOR, mỗi màu chỉ 1 ảnh đại diện
      const list_product_variation = [];
      
      if (product.variations && Array.isArray(product.variations)) {
        const colorGroups = groupVariationsByColor(product.variations);
        
        // Mỗi color group tạo 1 variation item duy nhất
        colorGroups.forEach(group => {
          if (group.representativeImage) {
            list_product_variation.push({
              id_variation: group.variationIds[0], // Dùng ID của variation đầu tiên
              product_id: product.productId,
              thumbnail: group.representativeImage,
              color: group.color,
              colorKey: group.key
            });
          }
        });
      }

      // Get discount từ active sales
      const discount = getProductDiscount(product.productId);

      // Loại bỏ text placeholder "(Đừng xóa)" và các ký tự không cần thiết
      let cleanDescription = (product.description || '').trim();
      cleanDescription = cleanDescription.replace(/^\(Đừng xóa\)\s*/i, '');
      cleanDescription = cleanDescription.replace(/^\(Dừng xóa\)\s*/i, '');

      return {
        id: product.productId,
        prod_id: product.productId,
        title: product.title || '',
        description: cleanDescription,
        price: product.price || 0,
        thumbnail: product.image || '',
        rate: 0, // API không trả về rate, mặc định 0
        Discount: discount > 0 ? discount : undefined, // Chỉ set discount khi > 0
        list_product_variation: list_product_variation
      };
    });
  }, [products, getProductDiscount, groupVariationsByColor]);

  const breadcrumbItems = [
    { label: "Trang chủ", path: "/" },
    { label: "NEW ARRIVALS" }
  ];

  const displayedProducts = useMemo(() => {
    if (showBreadcrumb) return newArrivalsProducts;
    return newArrivalsProducts.slice(0, 6);
  }, [newArrivalsProducts, showBreadcrumb]);

  return (
    <>
      {showBreadcrumb && <Breadcrumb items={breadcrumbItems} />}
    <section className="na-section">
      <h2 className="na-title">New Arrivals</h2>
      <p className="na-desc">Các sản phẩm mới nhất dành cho bạn</p>
      
      {loading ? (
        <div className="na-loading">Đang tải sản phẩm mới...</div>
      ) : error ? (
        <div className="na-error">{error}</div>
      ) : newArrivalsProducts.length === 0 ? (
        <div className="na-empty">Chưa có sản phẩm mới trong 10 ngày qua</div>
      ) : (
        <>
      <div className="na-grid">
            {displayedProducts.map((product) => (
          <ProductCard 
            key={product.id} 
            product={product}
            fromPage="newArrivals"
          />
        ))}
      </div>
          {showViewMoreButton && newArrivalsProducts.length > displayedProducts.length && (
            <div className="na-view-more-container">
              <button 
                className="na-view-more-btn"
                onClick={() => navigate('/newArrivals')}
              >
                Xem thêm
              </button>
            </div>
          )}
        </>
      )}
    </section>
    </>
  );
}