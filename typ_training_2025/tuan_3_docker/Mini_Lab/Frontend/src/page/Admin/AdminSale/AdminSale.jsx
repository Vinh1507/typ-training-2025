import axios from 'axios'
import { base } from '../../../service/Base.jsx'
import { useEffect, useState } from 'react'
import { App } from 'antd'
import { Plus, Pencil, ChevronRight, ChevronDown, Trash2 } from 'lucide-react'
import './AdminSale.css'
import NewSaleModal from './NewSale/NewSale.jsx'
import UpdateSaleModal from './UpdateSale/UpdateSale.jsx'
import ConfirmDialog from '../Components/ConfirmDialog.jsx'


export default function AdminSale() {
    const [_sales, setSales] = useState([]);
    const [expandedSaleId, setExpandedSaleId] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false);
    const [selectedSale, setSelectedSale] = useState(null);
    const [isConfirmOpen, setIsConfirmOpen] = useState(false);
    const [saleToDelete, setSaleToDelete] = useState(null);
    const [productsDetails, setProductsDetails] = useState({});
    const [loadingProducts, setLoadingProducts] = useState({});
    const { message } = App.useApp();
    
    const fetchSales = async () => {
        try {
            const response = await axios.get(`${base}/sales`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            if (response.status === 200) {
                console.log('📋 Fetched all sales:', response.data.result);
                
                // Debug: Kiểm tra list_product của từng sale
                response.data.result.forEach(sale => {
                    console.log(`Sale "${sale.name}" has ${sale.list_product?.length || 0} products:`, sale.list_product);
                });
                
                setSales(response.data.result);
            }
        } catch (error) {
            console.log(error)
            message.error('Tải danh sách khuyến mãi thất bại');
        }
    }

    useEffect(() => {
        const controller = new AbortController();
        
        const fetchSalesWithAbort = async () => {
            try {
                const response = await axios.get(`${base}/sales`, {
                    signal: controller.signal,
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                })
                if (response.status === 200) {
                    setSales(response.data.result);
                    console.log(response.data.result);
                }
            } catch (error) {
                if (error.name === 'CanceledError') {
                    console.log('Request was cancelled');
                    return;
                }
                console.log(error)
                message.error('Tải danh sách khuyến mãi thất bại');
            }
        }
        
        fetchSalesWithAbort()
        
        return () => {
            controller.abort();
        }
    }, [message])

    const fetchProductDetails = async (productIds, saleId) => {
        setLoadingProducts(prev => ({ ...prev, [saleId]: true }));
        
        try {
            const promises = productIds.map(id => 
                axios.get(`${base}/products/${id}`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }).catch(err => {
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
            
            setProductsDetails(prev => ({ ...prev, ...details }));
        } catch (error) {
            console.log('Error fetching product details:', error);
        } finally {
            setLoadingProducts(prev => ({ ...prev, [saleId]: false }));
        }
    };

    const toggleExpand = (saleId) => {
        const sale = _sales.find(s => s.id === saleId);
        const isExpanding = expandedSaleId !== saleId;
        
        setExpandedSaleId(isExpanding ? saleId : null);
        
        // Fetch product details khi expand
        if (isExpanding && sale && sale.list_product && sale.list_product.length > 0) {
            const productIds = sale.list_product.map(p => p.id);
            const needsFetch = productIds.some(id => !productsDetails[id]);
            
            if (needsFetch) {
                fetchProductDetails(productIds, saleId);
            }
        }
    }

    const handleSaleCreated = async () => {
        // Refresh toàn bộ danh sách để lấy list_product đầy đủ từ server
        await fetchSales();
    }

    const handleEditClick = (sale) => {
        setSelectedSale(sale);
        setIsUpdateModalOpen(true);
    }

    const handleSaleUpdated = async () => {
        // Refresh toàn bộ danh sách để lấy list_product đầy đủ từ server
        await fetchSales();
    }

    const handleDeleteClick = (sale) => {
        setSaleToDelete(sale)
        setIsConfirmOpen(true)
    }

    const handleConfirmDelete = async () => {
        if (!saleToDelete) return

        setIsConfirmOpen(false)

        try {
            const response = await axios.delete(`${base}/sales/${saleToDelete.id}`, {
                headers: { 
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status === 200 || response.status === 204) {
                message.success('Xóa khuyến mãi thành công')
                setSales(_sales.filter(s => s.id !== saleToDelete.id))
                setSaleToDelete(null)
                return
            }
            message.error(response.data?.message || 'Xóa khuyến mãi thất bại')
        } catch (err) {
            message.error(err?.response?.data?.message || 'Có lỗi khi xóa khuyến mãi')
        }
    }

    const handleCancelDelete = () => {
        setIsConfirmOpen(false)
        setSaleToDelete(null)
    }

    return (
        <div className="admin-sale-container">
            <div className="admin-sale-header">
                <h1>Quản Lý Khuyến Mãi</h1>
                <button className="btn-create-sale" onClick={() => setIsModalOpen(true)}>
                    <Plus size={18} />
                    <span>Tạo khuyến mãi mới</span>
                </button>
            </div>
            
            <div className="sales-grid">
                {_sales.map((sale) => {
                    // Determine if sale is active based on current time
                    const now = new Date()
                    const start = new Date(sale.stDate)
                    const end = new Date(sale.endDate)
                    const isActive = now >= start && now <= end
                    
                    return (
                    <div key={sale.id} className={`sale-card ${isActive ? 'active' : 'inactive'}`}>
                        <div className="sale-card-header">
                            <h2 className="sale-name">{sale.name}</h2>
                            <span className={`sale-status ${isActive ? 'status-active' : 'status-inactive'}`}>
                                {isActive ? 'Hoạt động' : 'Không hoạt động'}
                            </span>
                        </div>
                        
                        <div className="sale-card-body">
                            <p className="sale-description">{sale.description || 'Không có mô tả'}</p>
                            
                            <div className="sale-dates">
                                <div className="date-item">
                                    <span className="date-label">Ngày bắt đầu:</span>
                                    <span className="date-value">
                                        {new Date(sale.stDate).toLocaleDateString('vi-VN')} {new Date(sale.stDate).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                                    </span>
                                </div>
                                <div className="date-item">
                                    <span className="date-label">Ngày kết thúc:</span>
                                    <span className="date-value">
                                        {new Date(sale.endDate).toLocaleDateString('vi-VN')} {new Date(sale.endDate).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })}
                                    </span>
                                </div>
                            </div>

                            <div className="sale-actions">
                                <button 
                                    className="btn-edit-sale"
                                    onClick={() => handleEditClick(sale)}
                                    title="Sửa"
                                >
                                    <Pencil size={16} />
                                </button>
                                <button 
                                    className="btn-delete-sale"
                                    onClick={() => handleDeleteClick(sale)}
                                    title="Xóa"
                                >
                                    <Trash2 size={16} />
                                </button>
                                <button 
                                    className="btn-view-products"
                                    onClick={() => toggleExpand(sale.id)}
                                >
                                    {expandedSaleId === sale.id ? (
                                        <>
                                            <ChevronDown size={16} />
                                            <span>Ẩn ({sale.list_product?.length || 0})</span>
                                        </>
                                    ) : (
                                        <>
                                            <ChevronRight size={16} />
                                            <span>Xem ({sale.list_product?.length || 0})</span>
                                        </>
                                    )}
                                </button>
                            </div>

                            {expandedSaleId === sale.id && (
                                <div className="products-list">
                                    {loadingProducts[sale.id] ? (
                                        <p className="loading-products">Đang tải thông tin sản phẩm...</p>
                                    ) : sale.list_product && sale.list_product.length > 0 ? (
                                        <div className="products-grid-expanded">
                                            {sale.list_product.map((product, index) => {
                                                const productDetail = productsDetails[product.id];

    return (
                                                    <div key={index} className="product-card-expanded">
                                                        <div className="product-image-expanded">
                                                            {product.image ? (
                                                                <img src={product.image} alt={productDetail?.title || 'Product'} />
                                                            ) : (
                                                                <div className="product-image-placeholder">
                                                                    <Package size={32} />
                                                                </div>
                                                            )}
                                                        </div>
                                                        <div className="product-details-expanded">
                                                            <h4 className="product-name-expanded">
                                                                {productDetail?.title || `Product ${product.id}`}
                                                            </h4>
                                                            {productDetail?.price && (
                                                                <div className="product-prices">
                                                                    <span className="product-original-price">
                                                                        {new Intl.NumberFormat('vi-VN', {
                                                                            style: 'currency',
                                                                            currency: 'VND'
                                                                        }).format(productDetail.price)}
                                                                    </span>
                                                                    <span className="product-sale-price">
                                                                        {new Intl.NumberFormat('vi-VN', {
                                                                            style: 'currency',
                                                                            currency: 'VND'
                                                                        }).format(productDetail.price * (1 - product.value))}
                                                                    </span>
                                                                </div>
                                                            )}
                                                            <span className="product-discount-badge">
                                                                -{(product.value * 100).toFixed(0)}%
                                                            </span>
                                                        </div>
                                                    </div>
                                                )
                                            })}
                                        </div>
                                    ) : (
                                        <p className="no-products">Chưa có sản phẩm nào trong khuyến mãi này</p>
                                    )}
                                </div>
                            )}
                        </div>
                    </div>
                    )
                })}
            </div>
            
            <NewSaleModal 
                open={isModalOpen}
                onClose={() => setIsModalOpen(false)}
                onCreated={handleSaleCreated}
                existingSales={_sales}
            />

            <UpdateSaleModal 
                open={isUpdateModalOpen}
                onClose={() => setIsUpdateModalOpen(false)}
                onUpdated={handleSaleUpdated}
                sale={selectedSale}
                existingSales={_sales}
            />

            <ConfirmDialog
                open={isConfirmOpen}
                title="Xác nhận xóa"
                message={`Bạn có chắc chắn muốn xóa khuyến mãi "${saleToDelete?.name}"?`}
                onConfirm={handleConfirmDelete}
                onCancel={handleCancelDelete}
            />
        </div>
    )
}