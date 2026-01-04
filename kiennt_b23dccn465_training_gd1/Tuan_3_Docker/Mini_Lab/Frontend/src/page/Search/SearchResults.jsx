import { useState, useEffect, useMemo, useCallback } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { base } from '../../service/Base.jsx';
import ProductCard from '../../components/ProductCard.jsx';
import Breadcrumb from '../../components/Breadcrumb.jsx';
import './SearchResults.css';

export default function SearchResults() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const searchQuery = searchParams.get('q') || '';

  const [products, setProducts] = useState([]);
  const [activeSales, setActiveSales] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch active sales
  useEffect(() => {
    const fetchActiveSales = async () => {
      try {
        const response = await axios.get(`${base}/sales`);
        if (response.status === 200 && response.data?.result) {
          const now = new Date();
          const active = response.data.result.filter(sale => {
            const stDate = new Date(sale.stDate);
            const endDate = new Date(sale.endDate);
            return now >= stDate && now <= endDate;
          });
          setActiveSales(active);
        }
      } catch (err) {
        console.error('Error fetching sales:', err);
      }
    };

    fetchActiveSales();
  }, []);

  // Fetch products
  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const response = await axios.get(`${base}/products`);
        if (response.status === 200 && response.data?.result) {
          setProducts(response.data.result || []);
        }
      } catch (err) {
        console.error('Error fetching products:', err);
        setError('Có lỗi khi tải danh sách sản phẩm');
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // Get discount for product
  const getProductDiscount = useCallback((productId) => {
    for (const sale of activeSales) {
      const productInSale = sale.list_product?.find(p => 
        (p.id === productId || p.productId === productId)
      );
      if (productInSale) {
        return Math.round((productInSale.value || 0) * 100);
      }
    }
    return 0;
  }, [activeSales]);

  // Group variations by color
  const groupVariationsByColor = useCallback((variations) => {
    if (!Array.isArray(variations) || variations.length === 0) return [];

    const colorMap = {};
    variations.forEach(v => {
      const colorKey = (v.color?.trim().toLowerCase() || 'default');
      if (!colorMap[colorKey]) {
        colorMap[colorKey] = {
          key: colorKey,
          color: v.color || 'Mặc định',
          representativeImage: v.image,
          images: [v.image],
          sizes: {},
          totalStock: 0,
          variationIds: []
        };
      } else {
        if (v.image && !colorMap[colorKey].images.includes(v.image)) {
          colorMap[colorKey].images.push(v.image);
        }
      }

      colorMap[colorKey].sizes[v.size] = {
        id: v.id,
        stock: v.stockQuantity || 0
      };
      colorMap[colorKey].totalStock += (v.stockQuantity || 0);
      colorMap[colorKey].variationIds.push(v.id);
    });

    return Object.values(colorMap);
  }, []);

  // Filter and transform products based on search query
  const searchResults = useMemo(() => {
    if (!products || products.length === 0) return [];
    if (!searchQuery.trim()) return [];

    const query = searchQuery.toLowerCase().trim();

    // Filter products by title or description
    const filtered = products.filter(product => {
      const title = (product.title || '').toLowerCase();
      const description = (product.description || '').toLowerCase();
      return title.includes(query) || description.includes(query);
    });

    // Transform to ProductCard format
    return filtered.map(product => {
      const list_product_variation = [];
      
      if (product.variations && Array.isArray(product.variations)) {
        const colorGroups = groupVariationsByColor(product.variations);
        
        colorGroups.forEach(group => {
          if (group.representativeImage) {
            list_product_variation.push({
              id_variation: group.variationIds[0],
              product_id: product.productId,
              thumbnail: group.representativeImage,
              color: group.color,
              colorKey: group.key
            });
          }
        });
      }

      const discount = getProductDiscount(product.productId);

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
        rate: 0,
        Discount: discount > 0 ? discount : undefined,
        list_product_variation: list_product_variation
      };
    });
  }, [products, searchQuery, getProductDiscount, groupVariationsByColor]);

  const breadcrumbItems = [
    { label: "Trang chủ", path: "/" },
    { label: "Kết quả tìm kiếm" }
  ];

  return (
    <>
      <Breadcrumb items={breadcrumbItems} />
      <div className="search-results-container">
        <h1>Kết quả tìm kiếm</h1>
        {searchQuery && (
          <p className="search-summary">
            Tìm kiếm cho: <strong>"{searchQuery}"</strong>
          </p>
        )}

        {loading ? (
          <div className="search-loading">Đang tìm kiếm...</div>
        ) : error ? (
          <div className="search-error">{error}</div>
        ) : searchResults.length === 0 ? (
          <div className="search-empty">
            <p>Không tìm thấy sản phẩm nào với từ khóa "{searchQuery}"</p>
            <button className="back-home-btn" onClick={() => navigate('/')}>
              Về trang chủ
            </button>
          </div>
        ) : (
          <>
            <p className="search-count">Tìm thấy {searchResults.length} sản phẩm</p>
            <div className="search-grid">
              {searchResults.map(product => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          </>
        )}
      </div>
    </>
  );
}

