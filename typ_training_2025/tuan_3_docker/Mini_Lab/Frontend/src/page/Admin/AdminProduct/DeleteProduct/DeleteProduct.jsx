import { useState } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import { Trash2 } from 'lucide-react'
import './DeleteProduct.css'

export default function DeleteProductModal({ open = false, onClose, onDeleted, product }) {
    const [deleting, setDeleting] = useState(false)
    const { message } = App.useApp()

    const handleCancel = () => {
        if (deleting) return
        if (typeof onClose === 'function') onClose()
    }

    const handleConfirmDelete = async () => {
        if (!product || !product.productId) {
            message.error('Không tìm thấy sản phẩm để xóa')
            if (typeof onClose === 'function') onClose()
            return
        }

        setDeleting(true)
        try {
            // Lấy thông tin sản phẩm chi tiết (để chắc chắn có danh sách biến thể)
            let productWithVariations = product
            if (!Array.isArray(product.variations) || product.variations.length === 0) {
                try {
                    const detailResponse = await axios.get(`${base}/products/${product.productId}`, {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    })

                    if (detailResponse.status === 200 && detailResponse.data?.result) {
                        productWithVariations = {
                            ...product,
                            ...detailResponse.data.result
                        }
                    }
                } catch (detailError) {
                    console.error('❌ Không thể tải chi tiết sản phẩm trước khi xóa:', detailError)
                }
            }

            // Bước 1: Xóa tất cả variations trước (nếu có)
            const variations = Array.isArray(productWithVariations.variations)
                ? productWithVariations.variations.filter(Boolean)
                : []
            
            if (variations.length > 0) {
                console.log(`🗑️ Đang xóa ${variations.length} variations trước...`)
                
                const deleteVariationPromises = variations.map(async (variation) => {
                    try {
                        const variationId = variation.id 
                            || variation.variationId 
                            || variation.productVariationId 
                            || variation.id_variation

                        if (!variationId) {
                            console.error('❌ Không tìm thấy ID biến thể để xóa:', variation)
                            return { success: false, id: null, error: new Error('Thiếu ID biến thể') }
                        }

                        await axios.delete(`${base}/variations/${variationId}`, {
                            headers: {
                                'Authorization': `Bearer ${localStorage.getItem('token')}`
                            }
                        })
                        console.log(`✅ Đã xóa variation ID: ${variationId}`)
                        return { success: true, id: variationId }
                    } catch (err) {
                        console.error(`❌ Lỗi xóa variation ID: ${variation.id}`, err)
                        return { success: false, id: variation.id, error: err }
                    }
                })

                const results = await Promise.all(deleteVariationPromises)
                const failedVariations = results.filter(r => !r.success)
                const failedCount = failedVariations.length
                
                if (failedCount > 0) {
                    const successCount = variations.length - failedCount
                    if (successCount > 0) {
                        message.warning(`Đã xóa ${successCount}/${variations.length} biến thể. Vui lòng thử xóa lại để hoàn tất.`)
                    } else {
                        message.error('Không thể xóa các biến thể của sản phẩm. Vui lòng kiểm tra lại.')
                    }

                    console.error('❌ Các biến thể chưa xóa được:', failedVariations)
                    setDeleting(false)
                    return
                } else {
                    console.log(`✅ Đã xóa thành công ${variations.length} variations`)
                }
            }

            // Bước 2: Xóa product
            console.log(`🗑️ Đang xóa sản phẩm ID: ${product.productId}...`)
            const response = await axios.delete(`${base}/products/${product.productId}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status === 200 || response.status === 204) {
                message.success('Xóa sản phẩm và tất cả biến thể thành công!')
                if (typeof onDeleted === 'function') onDeleted(productWithVariations)
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Xóa sản phẩm thất bại')
        } catch (err) {
            console.error('Error deleting product:', err)
            message.error(err?.response?.data?.message || 'Có lỗi khi xóa sản phẩm')
        } finally {
            setDeleting(false)
        }
    }

    if (!open || !product) return null

    return (
        <div className="modal-overlay delete-product-overlay" onClick={handleCancel} role="dialog" aria-modal="true">
            <div className="modal delete-product-modal" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2 className="modal-title">Xác nhận xóa</h2>
                    <button className="btn-close" onClick={handleCancel} disabled={deleting}>×</button>
                </div>
                <div className="modal-body">
                    <div className="delete-confirm-content">
                        <div className="delete-warning-icon">
                            <Trash2 size={48} color="#dc3545" />
                        </div>
                        <p className="delete-confirm-text">
                            Bạn có chắc chắn muốn xóa sản phẩm <strong>"{product.title}"</strong>?
                        </p>
                        <p className="delete-confirm-subtext">
                            Hành động này không thể hoàn tác. Tất cả <strong>{product.variations?.length || 0} biến thể</strong> và dữ liệu liên quan sẽ bị xóa vĩnh viễn.
                        </p>
                    </div>
                </div>
                <div className="modal-footer">
                    <button 
                        className="btn btn-secondary" 
                        onClick={handleCancel}
                        disabled={deleting}
                    >
                        Hủy
                    </button>
                    <button 
                        className="btn btn-danger" 
                        onClick={handleConfirmDelete}
                        disabled={deleting}
                    >
                        {deleting ? 'Đang xóa...' : 'Xóa sản phẩm'}
                    </button>
                </div>
            </div>
        </div>
    )
}

