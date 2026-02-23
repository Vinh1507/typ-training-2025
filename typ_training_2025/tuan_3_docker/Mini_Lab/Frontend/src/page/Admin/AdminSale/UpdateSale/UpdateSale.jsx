import { useEffect, useState } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import { Search, Package, Eye } from 'lucide-react'
import './UpdateSale.css'

export default function UpdateSaleModal({ open = false, onClose, onUpdated, sale, existingSales = [] }) {
    const [stDate, setStDate] = useState('')
    const [endDate, setEndDate] = useState('')
    const [selectedProducts, setSelectedProducts] = useState([])
    const [allProducts, setAllProducts] = useState([])
    const [searchQuery, setSearchQuery] = useState('')
    const [loadingProducts, setLoadingProducts] = useState(false)
    const [submitting, setSubmitting] = useState(false)

    const { message } = App.useApp();

    // Hàm kiểm tra trùng lặp thời gian (trừ sale đang sửa)
    const checkTimeOverlap = (newStart, newEnd, currentSaleId) => {
        const newStartDate = new Date(newStart)
        const newEndDate = new Date(newEnd)
        
        for (const existingSale of existingSales) {
            // Bỏ qua sale đang được sửa
            if (existingSale.id === currentSaleId) continue
            
            const saleStart = new Date(existingSale.stDate)
            const saleEnd = new Date(existingSale.endDate)
            
            // Kiểm tra trùng lặp
            if (newStartDate <= saleEnd && newEndDate >= saleStart) {
                return {
                    overlap: true,
                    conflictSale: existingSale
                }
            }
        }
        return { overlap: false }
    }

    useEffect(() => {
        if (open && sale) {
            // Pre-fill dates
            const stDateFormatted = formatDateForInput(sale.stDate)
            const endDateFormatted = formatDateForInput(sale.endDate)
            setStDate(stDateFormatted)
            setEndDate(endDateFormatted)
            
            // Pre-fill selectedProducts từ sale.list_product
            const currentProducts = sale.list_product || []
            const preselected = currentProducts.map(p => ({
                productId: p.id || p.productId,
                value: p.value || ''
            }))
            setSelectedProducts(preselected)
            
            setSearchQuery('')
            // Fetch all products
            fetchProducts()
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [open, sale])

    // Filter all products based on search
    const filteredProducts = allProducts.filter(product => {
        const productName = (product.title || product.productName || product.name || '').toLowerCase()
        return productName.includes(searchQuery.toLowerCase())
    })

    const formatDateForInput = (dateString) => {
        if (!dateString) return ''
        const date = new Date(dateString)
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        return `${year}-${month}-${day}T${hours}:${minutes}`
    }

    const fetchProducts = async () => {
        setLoadingProducts(true)
        try {
            const response = await axios.get(`${base}/products`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            if (response.status === 200) {
                setAllProducts(response.data.result || [])
            }
        } catch (error) {
            console.log('Error fetching products:', error)
            message.error('Không thể tải danh sách sản phẩm')
        } finally {
            setLoadingProducts(false)
        }
    }

    const handleProductToggle = (productId) => {
        setSelectedProducts(prev => {
            const exists = prev.find(p => p.productId === productId)
            if (exists) {
                // Bỏ tick - xóa khỏi danh sách
                return prev.filter(p => p.productId !== productId)
            } else {
                // Tick - thêm vào danh sách với value rỗng
                return [...prev, { productId, value: '' }]
            }
        })
    }

    const handleValueChange = (productId, value) => {
        setSelectedProducts(prev => 
            prev.map(p => p.productId === productId ? { ...p, value: value } : p)
        )
    }

    const disabled = !stDate || !endDate || submitting

    const handleCancel = () => {
        if (submitting) return
        if (typeof onClose === 'function') onClose()
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        if (disabled || !sale) return
        
        // Validate selectedProducts với value
        if (selectedProducts.length > 0) {
            const invalidProducts = selectedProducts.filter(p => {
                const value = parseFloat(p.value)
                return !p.value || isNaN(value) || value <= 0 || value > 1
            })
            if (invalidProducts.length > 0) {
                message.error('Vui lòng nhập giá trị giảm giá hợp lệ (0.01 - 1.0) cho tất cả sản phẩm')
                return
            }
        }
        
        // Validate dates
        const start = new Date(stDate)
        const end = new Date(endDate)
        
        if (start >= end) {
            message.error('Ngày bắt đầu phải nhỏ hơn ngày kết thúc')
            return
        }
        
        // Check time overlap
        const overlapCheck = checkTimeOverlap(stDate, endDate, sale.id)
        if (overlapCheck.overlap) {
            const conflictSale = overlapCheck.conflictSale
            const conflictStart = new Date(conflictSale.stDate).toLocaleString('vi-VN')
            const conflictEnd = new Date(conflictSale.endDate).toLocaleString('vi-VN')
            message.error(
                `Khoảng thời gian bị trùng với khuyến mãi "${conflictSale.name}" (${conflictStart} - ${conflictEnd}). Chỉ được có một khuyến mãi hoạt động trong cùng thời điểm.`
            )
            return
        }
        
        setSubmitting(true)
        try {
            const formattedStDate = new Date(stDate).toISOString()
            const formattedEndDate = new Date(endDate).toISOString()
            
            // So sánh current products vs selected products
            const currentProducts = sale.list_product || []
            const currentProductIds = currentProducts.map(p => p.id || p.productId)
            const selectedProductIds = selectedProducts.map(p => p.productId)
            
            // Products to remove: có trong current nhưng không có trong selected
            const removeProductIds = currentProductIds.filter(id => !selectedProductIds.includes(id))
            
            // Products to add/update: có trong selected
            const addProducts = selectedProducts.map(p => ({
                productId: p.productId,
                value: parseFloat(p.value)
            }))

            const payload = {
                stDate: formattedStDate,
                endDate: formattedEndDate,
                removeProductIds: removeProductIds,
                addProducts: addProducts
            }
            
            console.log('Update Sale Payload:', payload)
            console.log('Current products:', currentProductIds)
            console.log('Selected products:', selectedProductIds)
            console.log('Remove:', removeProductIds)
            console.log('Add/Update:', addProducts)

            const response = await axios.put(`${base}/sales/${sale.id}`, payload, {
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            
            console.log('Update Sale Response:', response.data)

            if (response.status === 200 || response.status === 201) {
                message.success('Cập nhật khuyến mãi thành công')
                // Chờ reload danh sách từ server
                if (typeof onUpdated === 'function') await onUpdated(response.data?.result || sale)
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Cập nhật khuyến mãi thất bại')
        } catch (err) {
            message.error(err?.response?.data?.message || 'Có lỗi khi cập nhật khuyến mãi')
        } finally {
            setSubmitting(false)
        }
    }

    if (!open || !sale) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true">
            <div className="modal update-modal">
                <div className="modal-header">
                    <h2 className="modal-title">Cập nhật khuyến mãi: {sale.name}</h2>
                </div>
                <form className="modal-body" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="stDate">Ngày bắt đầu <span className="required">*</span></label>
                            <input
                                id="stDate"
                                className="form-control"
                                type="datetime-local"
                                value={stDate}
                                onChange={(e) => setStDate(e.target.value)}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="endDate">Ngày kết thúc <span className="required">*</span></label>
                            <input
                                id="endDate"
                                className="form-control"
                                type="datetime-local"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                required
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label>Chọn sản phẩm cho khuyến mãi ({selectedProducts.length} đã chọn)</label>
                        
                        <div className="search-box">
                            <Search size={18} className="search-icon" />
                            <input
                                type="text"
                                className="search-input"
                                placeholder="Tìm kiếm sản phẩm..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                            />
                        </div>

                        <div className="products-container">
                            {loadingProducts ? (
                                <div className="products-loading">Đang tải sản phẩm...</div>
                            ) : filteredProducts.length === 0 ? (
                                <div className="products-empty">
                                    {searchQuery ? 'Không tìm thấy sản phẩm nào' : 'Không có sản phẩm nào'}
                                </div>
                            ) : (
                                <div className="products-grid-sale">
                                    {filteredProducts.map((product) => {
                                        const selected = selectedProducts.find(p => p.productId === product.productId)
                                        return (
                                            <div key={product.productId} className="product-card-sale">
                                                <div className="product-checkbox-wrapper">
                                                    <input
                                                        type="checkbox"
                                                        checked={!!selected}
                                                        onChange={() => handleProductToggle(product.productId)}
                                                        className="product-checkbox-input"
                                                    />
                                                </div>
                                                <div className="product-image-sale">
                                                    {product.image ? (
                                                        <img src={product.image} alt={product.title} />
                                                    ) : (
                                                        <div className="product-image-placeholder-sale">
                                                            <Package size={32} />
                                                        </div>
                                                    )}
                                                </div>
                                                <div className="product-info-sale">
                                                    <h4 className="product-title-sale">{product.title || product.productName || product.name}</h4>
                                                    <p className="product-price-sale">
                                                        {new Intl.NumberFormat('vi-VN', {
                                                            style: 'currency',
                                                            currency: 'VND'
                                                        }).format(product.price)}
                                                    </p>
                                                    {product.description && (
                                                        <p className="product-desc-sale">{product.description}</p>
                                                    )}
                                                    {selected && (
                                                        <div className="value-input-wrapper">
                                                            <label className="value-label">Giảm giá:</label>
                                                            <input
                                                                type="number"
                                                                className="value-input-inline"
                                                                placeholder="0.0 - 1.0"
                                                                min="0"
                                                                max="1"
                                                                step="0.01"
                                                                value={selected.value}
                                                                onChange={(e) => handleValueChange(product.productId, e.target.value)}
                                                                onClick={(e) => e.stopPropagation()}
                                                            />
                                                        </div>
                                                    )}
                                                </div>
                                                <button
                                                    type="button"
                                                    className="btn-view-product-sale"
                                                    onClick={(e) => {
                                                        e.stopPropagation()
                                                        window.open(`/product/${product.productId}`, '_blank')
                                                    }}
                                                    title="Xem chi tiết"
                                                >
                                                    <Eye size={16} />
                                                </button>
                                            </div>
                                        )
                                    })}
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={handleCancel} disabled={submitting}>
                            Hủy
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={disabled}>
                            {submitting ? 'Đang cập nhật...' : 'Cập nhật'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

