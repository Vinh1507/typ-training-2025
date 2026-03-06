import { useState, useEffect } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App, Select } from 'antd'
import { Upload, X, Plus, Trash2 } from 'lucide-react'
import './CreateProduct.css'

export default function CreateProductModal({ open = false, onClose, onCreated }) {
    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [price, setPrice] = useState('')
    const [categoryId, setCategoryId] = useState(null)
    const [imageFile, setImageFile] = useState(null)
    const [imagePreview, setImagePreview] = useState(null)
    const [submitting, setSubmitting] = useState(false)
    const [categories, setCategories] = useState([])
    const [loadingCategories, setLoadingCategories] = useState(false)
    const [variations, setVariations] = useState([])

    const { message } = App.useApp()

    const createEmptySizeRow = () => ({
        id: `size-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
        size: '',
        stockQuantity: ''
    })

    // Fetch categories when modal opens
    useEffect(() => {
        const fetchCategories = async () => {
            setLoadingCategories(true)
            try {
                const response = await axios.get(`${base}/category`, {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                })
                if (response.status === 200) {
                    setCategories(response.data.result || [])
                }
            } catch (error) {
                console.error('Error fetching categories:', error)
                message.error('Không thể tải danh sách danh mục')
            } finally {
                setLoadingCategories(false)
            }
        }

        if (open) {
            fetchCategories()
        }
    }, [open, message])

    const handleImageChange = (e) => {
        const file = e.target.files[0]
        if (file) {
            if (!file.type.startsWith('image/')) {
                message.error('Vui lòng chọn file ảnh')
                return
            }
            setImageFile(file)
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
    }

    const handleCancel = () => {
        if (submitting) return
        resetForm()
        if (typeof onClose === 'function') onClose()
    }

    const resetForm = () => {
        setTitle('')
        setDescription('')
        setPrice('')
        setCategoryId(null)
        setImageFile(null)
        setImagePreview(null)
        setVariations([])
    }

    // Variation management
    const handleAddVariation = () => {
        setVariations([...variations, {
            id: Date.now(), // temporary ID
            color: '',
            imageFile: null,
            imagePreview: null,
            sizes: [createEmptySizeRow()]
        }])
    }

    const handleRemoveVariation = (id) => {
        setVariations(variations.filter(v => v.id !== id))
    }

    const handleVariationChange = (id, field, value) => {
        setVariations(variations.map(v => {
            if (v.id === id) {
                return { ...v, [field]: value }
            }
            return v
        }))
    }

    const handleAddSizeRow = (variationId) => {
        setVariations(variations.map(v => {
            if (v.id === variationId) {
                const currentSizes = Array.isArray(v.sizes) ? v.sizes : []
                return { ...v, sizes: [...currentSizes, createEmptySizeRow()] }
            }
            return v
        }))
    }

    const handleRemoveSizeRow = (variationId, sizeId) => {
        setVariations(variations.map(v => {
            if (v.id === variationId) {
                const currentSizes = Array.isArray(v.sizes) ? v.sizes : []
                if (currentSizes.length <= 1) return { ...v, sizes: currentSizes }
                return { ...v, sizes: currentSizes.filter(size => size.id !== sizeId) }
            }
            return v
        }))
    }

    const handleSizeFieldChange = (variationId, sizeId, field, value) => {
        setVariations(variations.map(v => {
            if (v.id === variationId) {
                const currentSizes = Array.isArray(v.sizes) ? v.sizes : []
                const normalizedValue = field === 'size' ? value.toUpperCase() : value
                return {
                    ...v,
                    sizes: currentSizes.map(size => {
                        if (size.id === sizeId) {
                            return { ...size, [field]: normalizedValue }
                        }
                        return size
                    })
                }
            }
            return v
        }))
    }

    const handleVariationImageChange = (id, e) => {
        const file = e.target.files[0]
        if (file) {
            if (!file.type.startsWith('image/')) {
                message.error('Vui lòng chọn file ảnh')
                return
            }
            const reader = new FileReader()
            reader.onloadend = () => {
                setVariations(variations.map(v => {
                    if (v.id === id) {
                        return { ...v, imageFile: file, imagePreview: reader.result }
                    }
                    return v
                }))
            }
            reader.readAsDataURL(file)
        }
    }

    const handleRemoveVariationImage = (id) => {
        setVariations(variations.map(v => {
            if (v.id === id) {
                return { ...v, imageFile: null, imagePreview: null }
            }
            return v
        }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        
        // Validation cho tất cả các trường bắt buộc
        if (!title.trim()) {
            message.error('Vui lòng nhập tên sản phẩm')
            return
        }

        if (!description.trim()) {
            message.error('Vui lòng nhập mô tả sản phẩm')
            return
        }

        if (!price || parseFloat(price) <= 0) {
            message.error('Vui lòng nhập giá hợp lệ (lớn hơn 0)')
            return
        }

        if (!categoryId) {
            message.error('Vui lòng chọn danh mục sản phẩm')
            return
        }

        if (!imageFile) {
            message.error('Vui lòng chọn hình ảnh sản phẩm')
            return
        }

        setSubmitting(true)
        try {
            const formData = new FormData()
            formData.append('title', title.trim())
            formData.append('description', description.trim())
            formData.append('price', parseFloat(price))
            formData.append('categoryId', parseInt(categoryId))
            formData.append('image', imageFile)

            const response = await axios.post(`${base}/products`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status === 200 || response.status === 201) {
                const createdProduct = response.data?.result
                const productId = createdProduct?.productId || createdProduct?.id

                // Create variations if any
                if (variations.length > 0 && productId) {
                    try {
                        for (let i = 0; i < variations.length; i++) {
                            const variation = variations[i]
                            const sizeRows = Array.isArray(variation.sizes) ? variation.sizes : []

                            if (!variation.color.trim()) {
                                message.error(`Vui lòng nhập màu sắc cho biến thể #${i + 1}`)
                                return
                            }

                            if (sizeRows.length === 0) {
                                message.error(`Vui lòng thêm ít nhất một size cho biến thể #${i + 1}`)
                                return
                            }

                            for (let j = 0; j < sizeRows.length; j++) {
                                const sizeRow = sizeRows[j]
                                const stockValue = parseInt(sizeRow.stockQuantity, 10)

                                if (!sizeRow.size.trim()) {
                                    message.error(`Vui lòng nhập tên size cho biến thể #${i + 1}, dòng #${j + 1}`)
                                    return
                                }

                                if (sizeRow.stockQuantity === '' || Number.isNaN(stockValue) || stockValue < 0) {
                                    message.error(`Số lượng tồn kho phải là số không âm cho biến thể #${i + 1}, size ${sizeRow.size}`)
                                    return
                                }
                            }
                        }

                        const preparedVariations = variations.flatMap((variation, variationIndex) =>
                            (Array.isArray(variation.sizes) ? variation.sizes : []).map((sizeRow, sizeIndex) => ({
                                variationIndex,
                                sizeIndex,
                                color: variation.color.trim(),
                                size: sizeRow.size.trim(),
                                stockQuantity: parseInt(sizeRow.stockQuantity, 10),
                                imageFile: variation.imageFile
                            }))
                        )

                        if (preparedVariations.length === 0) {
                            message.warning('Vui lòng điền đầy đủ thông tin cho biến thể (Màu sắc, Size, Số lượng)')
                            return
                        }

                        console.log('📦 Creating variations:', preparedVariations.length)

                        const variationPromises = preparedVariations.map(async (payload, index) => {
                            try {
                                const variationFormData = new FormData()
                                variationFormData.append('productId', productId)
                                variationFormData.append('size', payload.size)
                                variationFormData.append('color', payload.color)
                                variationFormData.append('stockQuantity', payload.stockQuantity)

                                if (payload.imageFile) {
                                    variationFormData.append('image', payload.imageFile)
                                }

                                console.log(`📤 Creating variation ${index + 1}/${preparedVariations.length}:`, {
                                    productId,
                                    size: payload.size,
                                    color: payload.color,
                                    stockQuantity: payload.stockQuantity,
                                    hasImage: !!payload.imageFile
                                })

                                const response = await axios.post(`${base}/variations`, variationFormData, {
                                    headers: {
                                        'Content-Type': 'multipart/form-data',
                                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                                    }
                                })

                                console.log(`✅ Variation ${index + 1} created:`, response.data)
                                return { success: true, payload, response: response.data }
                            } catch (err) {
                                console.error(`❌ Error creating variation ${index + 1}:`, {
                                    payload,
                                    error: err.response?.data || err.message,
                                    status: err.response?.status
                                })
                                return {
                                    success: false,
                                    payload,
                                    error: err.response?.data || { message: err.message }
                                }
                            }
                        })

                        const results = await Promise.all(variationPromises)
                        const successCount = results.filter(r => r.success).length
                        const failCount = results.filter(r => !r.success).length

                        if (failCount === 0) {
                            message.success(`Tạo sản phẩm và ${successCount} biến thể thành công`)
                        } else {
                            results.forEach((result, index) => {
                                if (!result.success) {
                                    const errorMsg = result.error?.message || result.error?.result?.message || 'Không xác định'
                                    message.error(`Biến thể ${index + 1} (${result.payload.size}/${result.payload.color}): ${errorMsg}`)
                                }
                            })

                            if (successCount > 0) {
                                message.warning(`Tạo sản phẩm thành công. ${successCount} biến thể thành công, ${failCount} biến thể thất bại`)
                            } else {
                                message.error(`Tạo sản phẩm thành công nhưng không thể tạo biến thể: ${results[0]?.error?.message || results[0]?.error?.result?.message || 'Vui lòng kiểm tra lại'}`)
                            }
                        }
                    } catch (variationError) {
                        console.error('❌ Unexpected error creating variations:', variationError)
                        const errorMsg = variationError?.response?.data?.message || 
                                       variationError?.response?.data?.result?.message ||
                                       variationError?.message || 
                                       'Vui lòng thử lại'
                        message.warning(`Tạo sản phẩm thành công nhưng có lỗi khi tạo biến thể: ${errorMsg}`)
                    }
                } else {
                    message.success('Tạo sản phẩm thành công')
                }

                resetForm()
                // Gọi onCreated() không có tham số để force refresh toàn bộ danh sách
                // (vì variations vừa tạo chưa có trong createdProduct)
                if (typeof onCreated === 'function') onCreated()
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Tạo sản phẩm thất bại')
        } catch (err) {
            console.error('Error creating product:', err)
            message.error(err?.response?.data?.message || 'Có lỗi khi tạo sản phẩm')
        } finally {
            setSubmitting(false)
        }
    }

    if (!open) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true">
            <div className="modal create-product-modal">
                <div className="modal-header">
                    <h2 className="modal-title">Tạo sản phẩm mới</h2>
                    <button className="btn-close" onClick={handleCancel} disabled={submitting}>×</button>
                </div>
                <form className="modal-body" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="title">Tên sản phẩm <span className="required">*</span></label>
                        <input
                            id="title"
                            className="form-control"
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            placeholder="Nhập tên sản phẩm"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Mô tả <span className="required">*</span></label>
                        <textarea
                            id="description"
                            className="form-control"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Nhập mô tả sản phẩm"
                            rows="4"
                            required
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="price">Giá (VNĐ) <span className="required">*</span></label>
                            <input
                                id="price"
                                className="form-control"
                                type="number"
                                value={price}
                                onChange={(e) => setPrice(e.target.value)}
                                placeholder="Nhập giá"
                                min="0"
                                step="1000"
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="categoryId">Danh mục <span className="required">*</span></label>
                            <Select
                                id="categoryId"
                                className="category-select"
                                value={categoryId}
                                onChange={(value) => setCategoryId(value)}
                                placeholder="Chọn danh mục"
                                showSearch
                                allowClear={false}
                                loading={loadingCategories}
                                filterOption={(input, option) =>
                                    (option?.label ?? '').toLowerCase().includes(input.toLowerCase())
                                }
                                options={(() => {
                                    // Hiển thị tất cả category, nhưng disable category cha (không cho chọn)
                                    const parents = categories.filter(cat => !cat.parentId || cat.parentId === 0)
                                    const children = categories.filter(cat => cat.parentId && cat.parentId !== 0)
                                    
                                    const sorted = []
                                    
                                    // Add orphan children first (with warning)
                                    const orphans = children.filter(child => 
                                        !parents.some(p => p.categoryId === (child.parentId || child.perentId))
                                    )
                                    orphans.forEach(orphan => {
                                        sorted.push({
                                            value: orphan.categoryId,
                                            label: `⚠️ ${orphan.categoryName || orphan.name} (Lỗi dữ liệu)`,
                                            parentId: orphan.parentId || orphan.perentId,
                                            isParent: false,
                                            isOrphan: true
                                        })
                                    })
                                    
                                    parents.forEach(parent => {
                                        // Add parent category (disabled - không cho chọn)
                                        sorted.push({
                                            value: parent.categoryId,
                                            label: parent.categoryName || parent.name,
                                            parentId: 0,
                                            isParent: true,
                                            disabled: true // Không cho chọn category cha
                                        })
                                        
                                        // Add children of this parent
                                        const parentChildren = children.filter(child => 
                                            (child.parentId || child.perentId) === parent.categoryId
                                        )
                                        parentChildren.forEach(child => {
                                            sorted.push({
                                                value: child.categoryId,
                                                label: child.categoryName || child.name,
                                                parentId: child.parentId || child.perentId,
                                                isParent: false
                                            })
                                        })
                                    })
                                    
                                    return sorted
                                })()}
                                optionRender={(option) => {
                                    const isChild = !option.data.isParent
                                    const isOrphan = option.data.isOrphan
                                    const isDisabled = option.data.disabled
                                    return (
                                        <div style={{ 
                                            paddingLeft: isChild ? '20px' : '0',
                                            color: isOrphan ? '#d32f2f' : (isDisabled ? '#999' : 'inherit'),
                                            fontWeight: isOrphan ? '600' : (isDisabled ? 'bold' : 'normal'),
                                            cursor: isDisabled ? 'not-allowed' : 'pointer'
                                        }}>
                                            {isChild && !isOrphan && '└─ '}
                                            {option.data.label}
                                        </div>
                                    )
                                }}
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="image">Hình ảnh sản phẩm <span className="required">*</span></label>
                        
                        {!imagePreview ? (
                            <div className="image-upload-area">
                                <input
                                    id="image"
                                    type="file"
                                    accept="image/*"
                                    onChange={handleImageChange}
                                    className="image-input"
                                    required
                                />
                                <label htmlFor="image" className="image-upload-label">
                                    <Upload size={32} />
                                    <span>Click để chọn ảnh</span>
                                    <span className="upload-hint">PNG, JPG, JPEG (Max 5MB)</span>
                                </label>
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
                            </div>
                        )}
                    </div>

                    {/* Variations Section */}
                    <div className="variations-section">
                        <div className="variations-header">
                            <label>Biến thể sản phẩm (Tùy chọn)</label>
                        </div>
                        
                        {variations.length > 0 && (
                            <div className="variations-list">
                                {variations.map((variation, index) => (
                                    <div key={variation.id} className="variation-item">
                                        <div className="variation-header-item">
                                            <h4>Biến thể #{index + 1}</h4>
                                            <button
                                                type="button"
                                                className="btn-remove-variation"
                                                onClick={() => handleRemoveVariation(variation.id)}
                                                disabled={submitting}
                                                title="Xóa biến thể"
                                            >
                                                <Trash2 size={16} />
                                            </button>
                                        </div>
                                        
                                        <div className="variation-form-grid">
                                            <div className="form-group">
                                                <label htmlFor={`color-${variation.id}`}>Màu sắc <span className="required">*</span></label>
                                                <input
                                                    id={`color-${variation.id}`}
                                                    className="form-control"
                                                    type="text"
                                                    value={variation.color}
                                                    onChange={(e) => handleVariationChange(variation.id, 'color', e.target.value)}
                                                    placeholder="VD: Đen, Trắng"
                                                    required
                                                />
                                            </div>

                                            <div className="form-group">
                                                <label htmlFor={`variation-image-${variation.id}`}>Hình ảnh biến thể</label>
                                                {!variation.imagePreview ? (
                                                    <div className="variation-image-upload-area">
                                                        <input
                                                            id={`variation-image-${variation.id}`}
                                                            type="file"
                                                            accept="image/*"
                                                            onChange={(e) => handleVariationImageChange(variation.id, e)}
                                                            className="image-input"
                                                        />
                                                        <label htmlFor={`variation-image-${variation.id}`} className="variation-image-upload-label">
                                                            <Upload size={20} />
                                                            <span>Chọn ảnh</span>
                                                        </label>
                                                    </div>
                                                ) : (
                                                    <div className="variation-image-preview-container">
                                                        <img src={variation.imagePreview} alt="Variation preview" className="variation-image-preview" />
                                                        <button
                                                            type="button"
                                                            className="btn-remove-image"
                                                            onClick={() => handleRemoveVariationImage(variation.id)}
                                                            title="Xóa ảnh"
                                                        >
                                                            <X size={14} />
                                                        </button>
                                                    </div>
                                                )}
                                            </div>
                                        </div>

                                        <div className="variation-sizes-section">
                                            <div className="variation-sizes-header">
                                                <label>Danh sách size & tồn kho <span className="required">*</span></label>
                                                <button
                                                    type="button"
                                                    className="btn-add-size"
                                                    onClick={() => handleAddSizeRow(variation.id)}
                                                    disabled={submitting}
                                                >
                                                    <Plus size={14} />
                                                    Thêm size
                                                </button>
                                            </div>

                                            <div className="variation-size-list">
                                                {(variation.sizes || []).map((sizeRow, sizeIndex) => (
                                                    <div key={sizeRow.id} className="variation-size-row">
                                                        <div className="form-group">
                                                            <label htmlFor={`variation-${variation.id}-size-${sizeRow.id}`}>
                                                                Size #{sizeIndex + 1} <span className="required">*</span>
                                                            </label>
                                                            <input
                                                                id={`variation-${variation.id}-size-${sizeRow.id}`}
                                                                className="form-control"
                                                                type="text"
                                                                value={sizeRow.size}
                                                                onChange={(e) => handleSizeFieldChange(variation.id, sizeRow.id, 'size', e.target.value)}
                                                                placeholder="VD: S, M, L, XL"
                                                                required
                                                            />
                                                        </div>

                                                        <div className="form-group">
                                                            <label htmlFor={`variation-${variation.id}-stock-${sizeRow.id}`}>
                                                                Tồn kho <span className="required">*</span>
                                                            </label>
                                                            <input
                                                                id={`variation-${variation.id}-stock-${sizeRow.id}`}
                                                                className="form-control"
                                                                type="number"
                                                                value={sizeRow.stockQuantity}
                                                                onChange={(e) => handleSizeFieldChange(variation.id, sizeRow.id, 'stockQuantity', e.target.value)}
                                                                placeholder="Nhập số lượng"
                                                                min="0"
                                                                required
                                                            />
                                                        </div>

                                                        {((variation.sizes || []).length > 1) && (
                                                            <button
                                                                type="button"
                                                                className="btn-remove-size"
                                                                onClick={() => handleRemoveSizeRow(variation.id, sizeRow.id)}
                                                                disabled={submitting}
                                                                title="Xóa size này"
                                                            >
                                                                <Trash2 size={16} />
                                                            </button>
                                                        )}
                                                    </div>
                                                ))}
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}

                        <div className="variations-footer">
                            <button
                                type="button"
                                className="btn-add-variation"
                                onClick={handleAddVariation}
                                disabled={submitting}
                            >
                                <Plus size={16} />
                                Thêm biến thể
                            </button>
                        </div>
                    </div>

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={handleCancel} disabled={submitting}>
                            Hủy
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={submitting}>
                            {submitting ? 'Đang tạo...' : 'Tạo sản phẩm'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

