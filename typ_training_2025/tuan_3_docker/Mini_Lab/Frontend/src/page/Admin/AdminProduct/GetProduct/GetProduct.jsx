import { useEffect, useState, useMemo } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import { Eye, Package, Plus, Search, Pencil, Layers, Tag, Upload, Trash2 } from 'lucide-react'
import CreateProductModal from '../CreateProduct/CreateProduct.jsx'
import UpdateProductModal from '../UpdateProduct/UpdateProduct.jsx'
import UpdateVariationModal from '../UpdateVariation/UpdateVariation.jsx'
import DeleteProductModal from '../DeleteProduct/DeleteProduct.jsx'
import DeleteVariationModal from '../DeleteVariation/DeleteVariation.jsx'
import './GetProduct.css'

export default function GetProduct() {
    const [products, setProducts] = useState([])
    const [selectedProduct, setSelectedProduct] = useState(null)
    const [loading, setLoading] = useState(false)
    const [searchQuery, setSearchQuery] = useState('')
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)
    const [isUpdateModalOpen, setIsUpdateModalOpen] = useState(false)
    const [productToUpdate, setProductToUpdate] = useState(null)
    const [activeSales, setActiveSales] = useState([])
    const [selectedVariation, setSelectedVariation] = useState(null)
    const [loadingVariationId, setLoadingVariationId] = useState(null)
    const [isUpdateVariationModalOpen, setIsUpdateVariationModalOpen] = useState(false)
    const [variationToUpdate, setVariationToUpdate] = useState(null)
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)
    const [productToDelete, setProductToDelete] = useState(null)
    const [isDeleteVariationModalOpen, setIsDeleteVariationModalOpen] = useState(false)
    const [variationToDelete, setVariationToDelete] = useState(null)
    const { message } = App.useApp()

    // Filter products based on search query
    const filteredProducts = useMemo(() => {
        if (!searchQuery.trim()) return products
        
        const query = searchQuery.toLowerCase()
        return products.filter(product => {
            const name = (product.title || '').toLowerCase()
            return name.includes(query)
        })
    }, [products, searchQuery])

    useEffect(() => {
        const controller = new AbortController()
        
        const fetchProducts = async () => {
            setLoading(true)
            try {
                const response = await axios.get(`${base}/products`, {
                    signal: controller.signal,
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                })
                if (response.status === 200) {
                    setProducts(response.data.result || [])
                }
            } catch (error) {
                if (error.name === 'CanceledError') {
                    console.log('Request was cancelled')
                    return
                }
                console.log(error)
                message.error('Tải danh sách sản phẩm thất bại')
            } finally {
                setLoading(false)
            }
        }
        
        fetchProducts()
        
        return () => {
            controller.abort()
        }
    }, [message])

    // Fetch active sales
    useEffect(() => {
        const controller = new AbortController()
        
        const fetchActiveSales = async () => {
            try {
                const response = await axios.get(`${base}/sales`, {
                    signal: controller.signal,
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                })
                if (response.status === 200) {
                    const now = new Date()
                    // Filter sales that are currently active (based on time, not backend active field)
                    const active = (response.data.result || []).filter(sale => {
                        const start = new Date(sale.stDate)
                        const end = new Date(sale.endDate)
                        // Sale is active if current time is between start and end date
                        return now >= start && now <= end
                    })
                    console.log('📊 Active sales:', active.length, active.map(s => s.name))
                    setActiveSales(active)
                }
            } catch (error) {
                if (error.name !== 'CanceledError') {
                    console.log('Error fetching sales:', error)
                }
            }
        }
        
        fetchActiveSales()
        
        return () => {
            controller.abort()
        }
    }, [])

    // Helper function to find sale info for a product
    const getProductSaleInfo = (productId) => {
        for (const sale of activeSales) {
            const productInSale = sale.list_product?.find(p => 
                (p.id === productId || p.productId === productId)
            )
            if (productInSale) {
                return {
                    discountPercent: Math.round((productInSale.value || 0) * 100),
                    discountValue: productInSale.value || 0
                }
            }
        }
        return null
    }

    const handleProductClick = (product) => {
        setSelectedProduct(product)
    }

    const handleCloseModal = () => {
        setSelectedProduct(null)
        setSelectedVariation(null)
    }

    const handleVariationImageClick = async (variationId, e) => {
        e.stopPropagation()
        
        if (!variationId) {
            message.warning('Không tìm thấy ID của biến thể')
            return
        }

        setLoadingVariationId(variationId)
        try {
            const response = await axios.get(`${base}/variations/${variationId}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status === 200 && response.data?.result) {
                setSelectedVariation(response.data.result)
                console.log('✅ Variation detail loaded:', response.data.result)
            } else {
                message.error('Không thể tải thông tin chi tiết biến thể')
            }
        } catch (error) {
            console.error('❌ Error fetching variation detail:', error)
            message.error(error?.response?.data?.message || 'Có lỗi khi tải thông tin biến thể')
        } finally {
            setLoadingVariationId(null)
        }
    }

    const handleCloseVariationModal = () => {
        setSelectedVariation(null)
        setVariationToUpdate(null)
        setIsUpdateVariationModalOpen(false)
    }

    const handleEditVariationClick = () => {
        if (selectedVariation) {
            setVariationToUpdate(selectedVariation)
            setIsUpdateVariationModalOpen(true)
        }
    }

    const handleDeleteVariationClick = (e) => {
        e.stopPropagation()
        if (selectedVariation) {
            console.log('🗑️ Opening delete variation modal for:', selectedVariation)
            setVariationToDelete(selectedVariation)
            setIsDeleteVariationModalOpen(true)
        } else {
            console.warn('⚠️ No selected variation to delete')
        }
    }

    const handleVariationDeleted = () => {
        // Close variation detail modal
        setSelectedVariation(null)
        // Refresh product list to reflect changes
        const controller = new AbortController()
        axios.get(`${base}/products`, {
            signal: controller.signal,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (response.status === 200) {
                setProducts(response.data.result || [])
                // Update selectedProduct if it has the same productId
                if (selectedProduct) {
                    const updatedProduct = response.data.result?.find(
                        p => p.productId === selectedProduct.productId
                    )
                    if (updatedProduct) {
                        setSelectedProduct(updatedProduct)
                    }
                }
            }
        })
        .catch(err => {
            if (err.name !== 'AbortError') {
                console.error('Error refreshing products:', err)
            }
        })
    }

    const handleVariationUpdated = (updatedVariation) => {
        // Refresh the selected variation
        if (updatedVariation && selectedVariation?.id === updatedVariation.id) {
            setSelectedVariation(updatedVariation)
        }
        // Refresh product list to reflect changes
        const controller = new AbortController()
        axios.get(`${base}/products`, {
            signal: controller.signal,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
        .then(response => {
            if (response.status === 200) {
                setProducts(response.data.result || [])
                // Update selectedProduct if it has the same productId
                if (selectedProduct) {
                    const updatedProduct = response.data.result?.find(
                        p => p.productId === selectedProduct.productId
                    )
                    if (updatedProduct) {
                        setSelectedProduct(updatedProduct)
                    }
                }
            }
        })
        .catch(error => {
            if (error.name !== 'CanceledError') {
                console.error('Error refreshing products:', error)
            }
        })
    }

    const handleProductCreated = (newProduct) => {
        if (newProduct) {
            setProducts(prev => [newProduct, ...prev])
        } else {
            // Refresh list if no product data returned
            const controller = new AbortController()
            axios.get(`${base}/products`, {
                signal: controller.signal,
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            .then(response => {
                if (response.status === 200) {
                    setProducts(response.data.result || [])
                }
            })
            .catch(error => {
                if (error.name !== 'CanceledError') {
                    console.log(error)
                }
            })
        }
    }

    const handleEditClick = (product, e) => {
        e.stopPropagation()
        setProductToUpdate(product)
        setIsUpdateModalOpen(true)
    }

    const handleDeleteClick = (product, e) => {
        e?.stopPropagation()
        setProductToDelete(product)
        setIsDeleteModalOpen(true)
    }

    const handleProductDeleted = (deletedProduct) => {
        if (deletedProduct) {
            setProducts(prev => prev.filter(p => p.productId !== deletedProduct.productId))
            
            // Close detail modal if the deleted product was being viewed
            if (selectedProduct?.productId === deletedProduct.productId) {
                setSelectedProduct(null)
            }
        }
        setProductToDelete(null)
    }

    const handleProductUpdated = (updatedProduct) => {
        if (updatedProduct) {
            setProducts(prev => prev.map(p => 
                p.productId === updatedProduct.productId ? updatedProduct : p
            ))
        } else {
            // Refresh list if no product data returned
            const controller = new AbortController()
            axios.get(`${base}/products`, {
                signal: controller.signal,
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            .then(response => {
                if (response.status === 200) {
                    setProducts(response.data.result || [])
                }
            })
            .catch(error => {
                if (error.name !== 'CanceledError') {
                    console.log(error)
                }
            })
        }
    }

    const formatPrice = (price) => {
        return new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
        }).format(price)
    }

    if (loading) {
        return (
            <div className="products-container">
                <div className="products-loading">Đang tải sản phẩm...</div>
            </div>
        )
    }

    return (
        <div className="products-container">
            <div className="products-header">
                <h1 className="products-title">Quản lý sản phẩm</h1>
                <div className="products-header-actions">
                    <span className="products-count">Tổng số: {products.length} sản phẩm</span>
                    <button 
                        className="btn-create-product"
                        onClick={() => setIsCreateModalOpen(true)}
                    >
                        <Plus size={18} />
                        <span>Tạo sản phẩm mới</span>
                    </button>
                </div>
            </div>

            {/* Search Box */}
            <div className="products-search-section">
                <div className="search-box-products">
                    <Search size={20} className="search-icon" />
                    <input
                        type="text"
                        className="search-input-products"
                        placeholder="Tìm kiếm sản phẩm theo tên..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                    />
                    {searchQuery && (
                        <button 
                            className="btn-clear-search"
                            onClick={() => setSearchQuery('')}
                        >
                            ×
                        </button>
                    )}
                </div>
                {searchQuery && (
                    <div className="search-results-info">
                        Tìm thấy {filteredProducts.length} sản phẩm
                    </div>
                )}
            </div>

            <div className="products-table-container">
                <table className="products-table">
                    <thead>
                        <tr>
                            <th>Hình ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá</th>
                            <th>Giảm giá</th>
                            <th>Biến thể</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filteredProducts.map((product) => (
                            <tr 
                                key={product.productId} 
                                className="product-row"
                                onClick={() => handleProductClick(product)}
                            >
                                <td className="product-image-cell">
                                    {product.image ? (
                                        <img src={product.image} alt={product.title} className="product-thumbnail" />
                                    ) : (
                                        <div className="product-thumbnail-placeholder">
                                            <Package size={24} />
                                        </div>
                                    )}
                                </td>
                                <td className="product-title">
                                    <div className="product-name-wrapper">
                                        <div className="product-name-text">{product.title}</div>
                                        {product.description && (
                                            <div className="product-description-text">{product.description}</div>
                                        )}
                                    </div>
                                </td>
                                <td className="product-price-cell">
                                    {(() => {
                                        const saleInfo = getProductSaleInfo(product.productId)
                                        if (saleInfo) {
                                            const originalPrice = product.price
                                            const salePrice = originalPrice * (1 - saleInfo.discountValue)
                                            return (
                                                <div className="price-with-sale">
                                                    <span className="price-original">{formatPrice(originalPrice)}</span>
                                                    <span className="price-sale">{formatPrice(salePrice)}</span>
                                                </div>
                                            )
                                        }
                                        return <span className="price-value">{formatPrice(product.price)}</span>
                                    })()}
                                </td>
                                <td className="product-sale-cell">
                                    {(() => {
                                        const saleInfo = getProductSaleInfo(product.productId)
                                        if (saleInfo) {
                                            return <span className="sale-badge">-{saleInfo.discountPercent}%</span>
                                        }
                                        return <span className="no-sale">-</span>
                                    })()}
                                </td>
                                <td className="product-variations-cell">
                                    {product.variations && product.variations.length > 0 ? (
                                        <span className="variations-badge">{product.variations.length}</span>
                                    ) : (
                                        <span className="no-variations">0</span>
                                    )}
                                </td>
                                <td className="product-actions-cell">
                                    <button
                                        className="btn-edit-product"
                                        onClick={(e) => handleEditClick(product, e)}
                                        title="Sửa sản phẩm"
                                    >
                                        <Pencil size={16} />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {filteredProducts.length === 0 && !loading && (
                <div className="products-empty">
                    <Package size={64} />
                    <p>{searchQuery ? `Không tìm thấy sản phẩm nào với từ khóa "${searchQuery}"` : 'Chưa có sản phẩm nào'}</p>
                </div>
            )}

            {/* Variation Detail Modal */}
            {selectedVariation && (
                <div className="modal-overlay" onClick={handleCloseVariationModal} style={{ zIndex: 1001 }}>
                    <div className="modal variation-detail-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2 className="modal-title">Chi tiết biến thể</h2>
                            <button className="btn-close" onClick={handleCloseVariationModal}>×</button>
                        </div>
                        <div className="modal-body">
                            <div className="variation-detail-content">
                                <div className="variation-detail-image">
                                    {selectedVariation.image ? (
                                        <img src={selectedVariation.image} alt={`${selectedVariation.size} - ${selectedVariation.color}`} />
                                    ) : (
                                        <div className="product-image-placeholder-large">
                                            <Package size={120} />
                                        </div>
                                    )}
                                </div>
                                <div className="variation-detail-info">
                                    <div className="detail-row">
                                        <label className="detail-label">ID:</label>
                                        <span className="detail-value">{selectedVariation.id}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Product ID:</label>
                                        <span className="detail-value">{selectedVariation.productId}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Size:</label>
                                        <span className="detail-value">{selectedVariation.size}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Màu sắc:</label>
                                        <span className="detail-value">{selectedVariation.color}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Số lượng tồn kho:</label>
                                        <span className={`detail-value ${selectedVariation.stockQuantity <= 5 ? 'low-stock-text' : ''}`}>
                                            {selectedVariation.stockQuantity}
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button className="btn-secondary" onClick={handleCloseVariationModal}>Đóng</button>
                            <button 
                                className="btn-danger" 
                                onClick={handleDeleteVariationClick}
                                type="button"
                            >
                                <Trash2 size={16} style={{ marginRight: '0.5rem', verticalAlign: 'middle' }} />
                                Xóa biến thể
                            </button>
                            <button className="btn-primary" onClick={handleEditVariationClick}>
                                <Pencil size={16} style={{ marginRight: '0.5rem', verticalAlign: 'middle' }} />
                                Sửa biến thể
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Update Variation Modal */}
            {isUpdateVariationModalOpen && variationToUpdate && (
                <UpdateVariationModal
                    variation={variationToUpdate}
                    onClose={() => {
                        setIsUpdateVariationModalOpen(false)
                        setVariationToUpdate(null)
                    }}
                    onUpdated={handleVariationUpdated}
                />
            )}

            {/* Delete Variation Modal */}
            <DeleteVariationModal
                open={isDeleteVariationModalOpen}
                variation={variationToDelete}
                onClose={() => {
                    setIsDeleteVariationModalOpen(false)
                    setVariationToDelete(null)
                }}
                onDeleted={handleVariationDeleted}
            />

            {/* Product Detail Modal */}
            {selectedProduct && (
                <div className="modal-overlay" onClick={handleCloseModal}>
                    <div className="modal product-detail-modal" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2 className="modal-title">Chi tiết sản phẩm</h2>
                            <button className="btn-close" onClick={handleCloseModal}>×</button>
                        </div>
                        <div className="modal-body">
                            <div className="product-detail-content">
                                <div className="product-detail-image">
                                    {selectedProduct.image ? (
                                        <img src={selectedProduct.image} alt={selectedProduct.title} />
                                    ) : (
                                        <div className="product-image-placeholder-large">
                                            <Package size={120} />
                                        </div>
                                    )}
                                </div>
                                <div className="product-detail-info">
                                    <div className="detail-row">
                                        <label className="detail-label">ID:</label>
                                        <span className="detail-value">{selectedProduct.productId}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Tên sản phẩm:</label>
                                        <span className="detail-value">{selectedProduct.title}</span>
                                    </div>
                                    <div className="detail-row">
                                        <label className="detail-label">Giá gốc:</label>
                                        <span className="detail-value price">{formatPrice(selectedProduct.price)}</span>
                                    </div>
                                    {selectedProduct.saleValue && (
                                        <>
                                            <div className="detail-row">
                                                <label className="detail-label">Giảm giá:</label>
                                                <span className="detail-value sale-badge">
                                                    <Tag size={14} />
                                                    {Math.round(selectedProduct.saleValue * 100)}%
                                                </span>
                                            </div>
                                            <div className="detail-row">
                                                <label className="detail-label">Giá sau giảm:</label>
                                                <span className="detail-value price-sale">
                                                    {formatPrice(selectedProduct.price * (1 - selectedProduct.saleValue))}
                                                </span>
                                            </div>
                                        </>
                                    )}
                                    {selectedProduct.categoryId && (
                                        <div className="detail-row">
                                            <label className="detail-label">Danh mục ID:</label>
                                            <span className="detail-value">{selectedProduct.categoryId}</span>
                                        </div>
                                    )}
                                    {selectedProduct.description && (
                                        <div className="detail-row full-width">
                                            <label className="detail-label">Mô tả:</label>
                                            <p className="detail-description">{selectedProduct.description}</p>
                                        </div>
                                    )}
                                    {selectedProduct.variations && selectedProduct.variations.length > 0 && (
                                        <div className="detail-row full-width">
                                            <label className="detail-label">
                                                <Layers size={16} />
                                                Biến thể ({selectedProduct.variations.length}):
                                            </label>
                                            <div className="variations-grid">
                                                {selectedProduct.variations.map((variation) => (
                                                    <div key={variation.id} className="variation-card">
                                                        {variation.image && (
                                                            <div 
                                                                className="variation-image clickable" 
                                                                onClick={(e) => handleVariationImageClick(variation.id, e)}
                                                                title="Click để xem chi tiết"
                                                            >
                                                                <img src={variation.image} alt={`${variation.size} - ${variation.color}`} />
                                                                {loadingVariationId === variation.id && (
                                                                    <div className="variation-image-loading">
                                                                        <span>Đang tải...</span>
                                                                    </div>
                                                                )}
                                                            </div>
                                                        )}
                                                        <div className="variation-details">
                                                            <div className="variation-attr">
                                                                <span className="attr-label">Size:</span>
                                                                <span className="attr-value">{variation.size}</span>
                                                            </div>
                                                            <div className="variation-attr">
                                                                <span className="attr-label">Màu:</span>
                                                                <span className="attr-value">{variation.color}</span>
                                                            </div>
                                                            <div className="variation-attr">
                                                                <span className="attr-label">Tồn kho:</span>
                                                                <span className={`attr-value stock ${variation.stockQuantity <= 5 ? 'low-stock' : ''}`}>
                                                                    {variation.stockQuantity}
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    )}
                                    <div className="detail-row">
                                        <label className="detail-label">Ngày tạo:</label>
                                        <span className="detail-value">
                                            {selectedProduct.createdAt 
                                                ? new Date(selectedProduct.createdAt).toLocaleString('vi-VN')
                                                : <span style={{ color: '#999', fontStyle: 'italic' }}>Chưa có</span>
                                            }
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button className="btn-secondary" onClick={handleCloseModal}>Đóng</button>
                            <button 
                                className="btn-danger" 
                                onClick={(e) => handleDeleteClick(selectedProduct, e)}
                            >
                                <Trash2 size={16} style={{ marginRight: '0.5rem', verticalAlign: 'middle' }} />
                                Xóa sản phẩm
                            </button>
                            <button 
                                className="btn-primary" 
                                onClick={(e) => {
                                    e.stopPropagation()
                                    handleEditClick(selectedProduct, e)
                                }}
                            >
                                <Pencil size={16} style={{ marginRight: '0.5rem', verticalAlign: 'middle' }} />
                                Cập nhật sản phẩm
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <CreateProductModal 
                open={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                onCreated={handleProductCreated}
            />

            <UpdateProductModal 
                open={isUpdateModalOpen}
                onClose={() => {
                    setIsUpdateModalOpen(false)
                    setProductToUpdate(null)
                }}
                onUpdated={handleProductUpdated}
                product={productToUpdate}
            />

            <DeleteProductModal 
                open={isDeleteModalOpen}
                onClose={() => {
                    setIsDeleteModalOpen(false)
                    setProductToDelete(null)
                }}
                onDeleted={handleProductDeleted}
                product={productToDelete}
            />
        </div>
    )
}

