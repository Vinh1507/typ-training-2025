import { useLocation, useParams } from 'react-router-dom'
import { useState, useEffect, useMemo, useCallback } from 'react'
import axios from 'axios'
import { base } from '../../service/Base.jsx'
import ProductCard from '../../components/ProductCard.jsx'
import Breadcrumb from '../../components/Breadcrumb.jsx'
import './CategoryProduct.css'

export default function CategoryProduct() {
    const location = useLocation()
    const { state } = location
    const { id } = useParams()

    const [products, setProducts] = useState([])
    const [activeSales, setActiveSales] = useState([])
    const [category, setCategory] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    // Fetch category info
    useEffect(() => {
        const fetchCategory = async () => {
            if (!id) return;
            
            try {
                const response = await axios.get(`${base}/category`);
                if (response.status === 200 && response.data?.result) {
                    const categories = response.data.result;
                    const currentCategory = categories.find(
                        cat => cat.categoryId == id
                    );
                    setCategory(currentCategory || null);
                }
            } catch (err) {
                console.error('Error fetching category:', err);
            }
        };

        fetchCategory();
    }, [id]);

    // Fetch active sales để tính discount
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

    // Fetch products from API - Không cần authentication
    useEffect(() => {
        const fetchProducts = async () => {
            setLoading(true);
            setError(null);
            
            try {
                const response = await axios.get(`${base}/products`);

                if (response.status === 200 && response.data?.result) {
                    // Filter products by categoryId
                    const allProducts = response.data.result || [];
                    const categoryProducts = id 
                        ? allProducts.filter(p => p.categoryId == id) 
                        : allProducts;
                    setProducts(categoryProducts);
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
    }, [id]);

    // Helper function để tìm discount cho sản phẩm
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

    // Helper function: Group variations theo COLOR
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

    // Transform products để phù hợp với ProductCard
    const transformedProducts = useMemo(() => {
        return products.map(product => {
            // Transform variations: Group theo COLOR, mỗi màu chỉ 1 ảnh đại diện
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

            // Get discount từ active sales
            const discount = getProductDiscount(product.productId);

            // Clean description
            let cleanDescription = (product.description || '').trim();
            cleanDescription = cleanDescription.replace(/\(Đừng xóa\)/gi, '').trim();

            return {
                id: product.productId,
                title: product.title || '',
                price: product.price || 0,
                thumbnail: product.image || '',
                description: cleanDescription,
                rate: 0,
                Discount: discount > 0 ? discount : undefined, // Chỉ set discount khi > 0
                list_product_variation: list_product_variation
            };
        });
    }, [products, getProductDiscount, groupVariationsByColor]);

    // Get category name (ưu tiên từ API, fallback sang state)
    const categoryName = category?.categoryName || category?.name || state?.name || 'Sản phẩm';

    // Breadcrumb items
    const breadcrumbItems = [
        { label: "Trang chủ", path: "/" },
        { label: "Danh mục", path: "/" },
        { label: categoryName }
    ];

    return (
        <>
            <Breadcrumb items={breadcrumbItems} />
            
            {/* Category banner */}
            {category && category.image && (
                <div className="category-banner">
                    <img src={category.image} alt={categoryName} />
                </div>
            )}
            
            <div className="category-container">
                <h1>{categoryName}</h1>
                
                {loading ? (
                    <div className="category-loading">Đang tải sản phẩm...</div>
                ) : error ? (
                    <div className="category-error">{error}</div>
                ) : transformedProducts.length === 0 ? (
                    <div className="category-empty">Không có sản phẩm nào trong danh mục này</div>
                ) : (
                    <div className="na-grid">
                        {transformedProducts.map((product) => (
                            <ProductCard 
                                key={product.id} 
                                product={product}
                                fromPage="category"
                                categoryName={categoryName}
                            />
                        ))}
                    </div>
                )}
            </div>
        </>
    )
}