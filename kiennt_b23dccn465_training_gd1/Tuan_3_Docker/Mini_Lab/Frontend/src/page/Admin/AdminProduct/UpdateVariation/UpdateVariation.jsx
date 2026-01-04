import { useState, useEffect } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import { Upload, X } from 'lucide-react'
import './UpdateVariation.css'

export default function UpdateVariationModal({ variation, onClose, onUpdated }) {
    const [size, setSize] = useState('')
    const [color, setColor] = useState('')
    const [stockQuantity, setStockQuantity] = useState('')
    const [imageFile, setImageFile] = useState(null)
    const [imagePreview, setImagePreview] = useState(null)
    const [removeImage, setRemoveImage] = useState(false)
    const [submitting, setSubmitting] = useState(false)

    const { message } = App.useApp()

    // Pre-fill form when variation changes
    useEffect(() => {
        if (variation) {
            setSize(variation.size || '')
            setColor(variation.color || '')
            setStockQuantity(variation.stockQuantity ? variation.stockQuantity.toString() : '')
            setImagePreview(variation.image || null)
            setImageFile(null)
            setRemoveImage(false)
        }
    }, [variation])

    const handleImageChange = (e) => {
        const file = e.target.files[0]
        if (file) {
            if (!file.type.startsWith('image/')) {
                message.error('Vui lòng chọn file ảnh')
                return
            }
            setImageFile(file)
            setRemoveImage(false)
            const reader = new FileReader()
            reader.onloadend = () => {
                setImagePreview(reader.result)
            }
            reader.readAsDataURL(file)
        }
    }

    const handleRemoveImage = () => {
        setImageFile(null)
        setImagePreview(null)
        setRemoveImage(true)
    }

    const handleCancel = () => {
        if (submitting) return
        resetForm()
        if (typeof onClose === 'function') onClose()
    }

    const resetForm = () => {
        setSize('')
        setColor('')
        setStockQuantity('')
        setImageFile(null)
        setImagePreview(null)
        setRemoveImage(false)
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        
        if (!variation || !variation.id) {
            message.error('Không tìm thấy thông tin biến thể')
            return
        }

        // Build FormData with only changed fields
        const formData = new FormData()
        let hasChanges = false

        // Check if size changed
        if (size.trim() && size.trim() !== variation.size) {
            formData.append('size', size.trim())
            hasChanges = true
        }

        // Check if color changed
        if (color.trim() !== (variation.color || '')) {
            formData.append('color', color.trim())
            hasChanges = true
        }

        // Check if stockQuantity changed
        if (stockQuantity && parseInt(stockQuantity) !== variation.stockQuantity) {
            if (parseInt(stockQuantity) < 0) {
                message.error('Số lượng tồn kho phải lớn hơn hoặc bằng 0')
                return
            }
            formData.append('stockQuantity', parseInt(stockQuantity))
            hasChanges = true
        }

        // Check if image changed (new file selected or removed)
        if (imageFile) {
            formData.append('image', imageFile)
            hasChanges = true
        } else if (removeImage && variation.image) {
            // User wants to remove the existing image
            formData.append('image', '')
            hasChanges = true
        }

        if (!hasChanges) {
            message.warning('Không có thay đổi nào để cập nhật')
            return
        }

        setSubmitting(true)
        try {
            const response = await axios.put(`${base}/variations/${variation.id}`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status === 200 || response.status === 201) {
                message.success('Cập nhật biến thể thành công')
                resetForm()
                if (typeof onUpdated === 'function') onUpdated(response.data?.result)
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Cập nhật biến thể thất bại')
        } catch (err) {
            console.error('Error updating variation:', err)
            message.error(err?.response?.data?.message || 'Có lỗi khi cập nhật biến thể')
        } finally {
            setSubmitting(false)
        }
    }

    if (!variation) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true" style={{ zIndex: 1002 }} onClick={handleCancel}>
            <div className="modal update-variation-modal" onClick={(e) => e.stopPropagation()}>
                <div className="modal-header">
                    <h2 className="modal-title">Cập nhật biến thể</h2>
                    <button className="btn-close" onClick={handleCancel} disabled={submitting}>×</button>
                </div>
                <form className="modal-body" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="size">Size</label>
                        <input
                            id="size"
                            className="form-control"
                            type="text"
                            value={size}
                            onChange={(e) => setSize(e.target.value)}
                            placeholder="Nhập size mới"
                        />
                        {variation?.size && (
                            <span className="current-value">Hiện tại: {variation.size}</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="color">Màu sắc</label>
                        <input
                            id="color"
                            className="form-control"
                            type="text"
                            value={color}
                            onChange={(e) => setColor(e.target.value)}
                            placeholder="Nhập màu mới"
                        />
                        {variation?.color && (
                            <span className="current-value">Hiện tại: {variation.color}</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="stockQuantity">Số lượng tồn kho</label>
                        <input
                            id="stockQuantity"
                            className="form-control"
                            type="number"
                            value={stockQuantity}
                            onChange={(e) => setStockQuantity(e.target.value)}
                            placeholder="Nhập số lượng mới"
                            min="0"
                        />
                        {variation?.stockQuantity !== undefined && (
                            <span className="current-value">
                                Hiện tại: {variation.stockQuantity}
                            </span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="image">Hình ảnh biến thể</label>
                        
                        {!imagePreview ? (
                            <div className="image-upload-area">
                                <input
                                    id="image"
                                    type="file"
                                    accept="image/*"
                                    onChange={handleImageChange}
                                    className="image-input"
                                />
                                <label htmlFor="image" className="image-upload-label">
                                    <Upload size={32} />
                                    <span>{removeImage ? 'Chọn ảnh mới để thay thế' : 'Click để chọn ảnh mới'}</span>
                                    <span className="upload-hint">PNG, JPG, JPEG (Max 5MB)</span>
                                </label>
                                {removeImage && (
                                    <button
                                        type="button"
                                        className="btn-undo-remove-inline"
                                        onClick={() => {
                                            setRemoveImage(false)
                                            setImagePreview(variation?.image || null)
                                        }}
                                    >
                                        Hoàn tác - Giữ ảnh cũ
                                    </button>
                                )}
                            </div>
                        ) : (
                            <div className="image-preview-container">
                                <img src={imagePreview} alt="Preview" className="image-preview" />
                                <button
                                    type="button"
                                    className="btn-remove-image"
                                    onClick={handleRemoveImage}
                                    title="Xóa ảnh"
                                >
                                    <X size={16} />
                                </button>
                                {imageFile && (
                                    <span className="image-changed-badge">Ảnh mới</span>
                                )}
                                {removeImage && (
                                    <span className="image-removed-badge">Ảnh cũ sẽ bị xóa</span>
                                )}
                            </div>
                        )}
                        {variation?.image && !removeImage && !imageFile && imagePreview && (
                            <span className="current-value">Ảnh hiện tại</span>
                        )}
                    </div>

                    <div className="update-note">
                        💡 <strong>Lưu ý:</strong> Chỉ cần điền vào các trường muốn thay đổi
                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={handleCancel} disabled={submitting}>
                            Hủy
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={submitting}>
                            {submitting ? 'Đang cập nhật...' : 'Cập nhật'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

