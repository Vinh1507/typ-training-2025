import { useState, useEffect } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App, Select } from 'antd'
import { Upload, X, Plus, Trash2 } from 'lucide-react'
import './UpdateProduct.css'

export default function UpdateProductModal({ open = false, onClose, onUpdated, product }) {
    const [title, setTitle] = useState('')
    const [description, setDescription] = useState('')
    const [price, setPrice] = useState('')
    const [categoryId, setCategoryId] = useState('')
    const [imageFile, setImageFile] = useState(null)
    const [imagePreview, setImagePreview] = useState(null)
    const [removeImage, setRemoveImage] = useState(false)
    const [submitting, setSubmitting] = useState(false)
    const [categories, setCategories] = useState([])
    const [loadingCategories, setLoadingCategories] = useState(false)
    const [existingVariations, setExistingVariations] = useState([])
    const [newVariations, setNewVariations] = useState([])

    const { message } = App.useApp()

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

    // Pre-fill form when product changes
    useEffect(() => {
        if (product) {
            setTitle(product.title || '')
            setDescription(product.description || '')
            setPrice(product.price ? product.price.toString() : '')
            setCategoryId(product.categoryId || null)
            setImagePreview(product.image || null)
            setImageFile(null)
            setRemoveImage(false)
            setExistingVariations(product.variations || [])
            setNewVariations([])
        }
    }, [product])

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
        setTitle('')
        setDescription('')
        setPrice('')
        setCategoryId('')
        setImageFile(null)
        setImagePreview(null)
        setRemoveImage(false)
        setExistingVariations([])
        setNewVariations([])
    }

    // New variation management
    const handleAddNewVariation = () => {
        setNewVariations([...newVariations, {
            id: Date.now(), // temporary ID
            size: '',
            color: '',
            stockQuantity: '',
            imageFile: null,
            imagePreview: null
        }])
    }

    const handleRemoveNewVariation = (id) => {
        setNewVariations(newVariations.filter(v => v.id !== id))
    }

    const handleNewVariationChange = (id, field, value) => {
        setNewVariations(newVariations.map(v => {
            if (v.id === id) {
                return { ...v, [field]: value }
            }
            return v
        }))
    }

    const handleNewVariationImageChange = (id, e) => {
        const file = e.target.files[0]
        if (file) {
            if (!file.type.startsWith('image/')) {
                message.error('Vui lòng chọn file ảnh')
                return
            }
            const reader = new FileReader()
            reader.onloadend = () => {
                setNewVariations(newVariations.map(v => {
                    if (v.id === id) {
                        return { ...v, imageFile: file, imagePreview: reader.result }
                    }
                    return v
                }))
            }
            reader.readAsDataURL(file)
        }
    }

    const handleRemoveNewVariationImage = (id) => {
        setNewVariations(newVariations.map(v => {
            if (v.id === id) {
                return { ...v, imageFile: null, imagePreview: null }
            }
            return v
        }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        
        if (!product || !product.productId) {
            message.error('Không tìm thấy thông tin sản phẩm')
            return
        }

        // Build FormData - only append fields that have actual changes
        const formData = new FormData()
        let hasProductChanges = false

        // 1. Title - only append if changed
        if (title.trim() && title.trim() !== (product.title || '')) {
            formData.append('title', title.trim())
            hasProductChanges = true
        }

        // 2. Description - only append if changed
        if (description.trim() && description.trim() !== (product.description || '')) {
            formData.append('description', description.trim())
            hasProductChanges = true
        }

        // 3. Price - only append if changed
        if (price && price.trim() !== '') {
            const priceValue = parseFloat(price)
            if (isNaN(priceValue) || priceValue <= 0) {
                message.error('Giá phải là số lớn hơn 0')
                return
            }
            if (priceValue !== product.price) {
                formData.append('price', priceValue)
                hasProductChanges = true
            }
        }

        // 4. CategoryId - only append if changed
        const newCategoryId = categoryId ? parseInt(categoryId) : null
        const oldCategoryId = product.categoryId || null
        if (newCategoryId !== oldCategoryId && newCategoryId) {
            formData.append('categoryId', newCategoryId)
            hasProductChanges = true
        }

        // 5. Image - only append if there's a new file
        if (imageFile) {
            formData.append('image', imageFile)
            hasProductChanges = true
        }

        // Check if there are new variations to add
        const hasNewVariations = newVariations.length > 0 && 
            newVariations.some(v => v.size && v.color && v.stockQuantity)
        
        // Overall changes: either product changes or new variations
        const hasChanges = hasProductChanges || hasNewVariations

        if (!hasChanges) {
            message.warning('Không có thay đổi nào để cập nhật')
            return
        }

        setSubmitting(true)
        try {
            // Only send PUT request if there are changes to product info
            if (hasProductChanges) {
                // Debug: Log FormData contents
                console.log('🔍 FormData contents:')
                for (let [key, value] of formData.entries()) {
                    console.log(`  ${key}:`, value instanceof File ? `File: ${value.name}` : value)
                }

                const response = await axios.put(`${base}/products/${product.productId}`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                })

                if (response.status !== 200 && response.status !== 201) {
                    message.error(response.data?.message || 'Cập nhật sản phẩm thất bại')
                    setSubmitting(false)
                    return
                }
            }

            // Create new variations if any
            if (hasNewVariations && product.productId) {
                try {
                    const validVariations = newVariations.filter(v => v.size && v.color && v.stockQuantity)
                    
                    if (validVariations.length === 0) {
                        message.warning('Vui lòng điền đầy đủ thông tin cho biến thể (Size, Color, Số lượng)')
                        setSubmitting(false)
                        return
                    }

                    console.log('📦 Creating variations:', validVariations.length)
                    
                    const variationPromises = validVariations.map(async (variation, index) => {
                        try {
                            const variationFormData = new FormData()
                            variationFormData.append('productId', product.productId)
                            variationFormData.append('size', variation.size.trim())
                            variationFormData.append('color', variation.color.trim())
                            variationFormData.append('stockQuantity', parseInt(variation.stockQuantity))
                            
                            if (variation.imageFile) {
                                variationFormData.append('image', variation.imageFile)
                            }

                            console.log(`📤 Creating variation ${index + 1}/${validVariations.length}:`, {
                                productId: product.productId,
                                size: variation.size.trim(),
                                color: variation.color.trim(),
                                stockQuantity: parseInt(variation.stockQuantity),
                                hasImage: !!variation.imageFile
                            })

                            const response = await axios.post(`${base}/variations`, variationFormData, {
                                headers: {
                                    'Content-Type': 'multipart/form-data',
                                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                                }
                            })

                            console.log(`✅ Variation ${index + 1} created:`, response.data)
                            return { success: true, variation, response: response.data }
                        } catch (err) {
                            console.error(`❌ Error creating variation ${index + 1}:`, {
                                variation,
                                error: err.response?.data || err.message,
                                status: err.response?.status
                            })
                            return { 
                                success: false, 
                                variation, 
                                error: err.response?.data || { message: err.message } 
                            }
                        }
                    })

                    const results = await Promise.all(variationPromises)
                    const successCount = results.filter(r => r.success).length
                    const failCount = results.filter(r => !r.success).length

                    if (failCount === 0) {
                        if (hasProductChanges) {
                            message.success(`Cập nhật sản phẩm và thêm ${successCount} biến thể thành công`)
                        } else {
                            message.success(`Thêm ${successCount} biến thể thành công`)
                        }
                    } else {
                        // Show detailed error messages
                        results.forEach((result, index) => {
                            if (!result.success) {
                                const errorMsg = result.error?.message || result.error?.result?.message || 'Không xác định'
                                message.error(`Biến thể ${index + 1} (${result.variation.size}/${result.variation.color}): ${errorMsg}`)
                            }
                        })
                        
                        if (successCount > 0) {
                            message.warning(`${successCount} biến thể thành công, ${failCount} biến thể thất bại`)
                        } else {
                            message.error(`Không thể thêm biến thể: ${results[0]?.error?.message || results[0]?.error?.result?.message || 'Vui lòng kiểm tra lại'}`)
                        }
                    }
                } catch (variationError) {
                    console.error('❌ Unexpected error creating variations:', variationError)
                    const errorMsg = variationError?.response?.data?.message || 
                                   variationError?.response?.data?.result?.message ||
                                   variationError?.message || 
                                   'Vui lòng thử lại'
                    message.error(`Có lỗi khi thêm biến thể: ${errorMsg}`)
                }
            } else if (hasProductChanges) {
                message.success('Cập nhật sản phẩm thành công')
            }

            resetForm()
            // Refresh product data - call onUpdated if available
            if (typeof onUpdated === 'function') {
                // If we have response from PUT, use it, otherwise just trigger refresh
                if (hasProductChanges) {
                    // We need to get the updated product, but since we don't have response here,
                    // we'll just trigger the callback to refresh
                    onUpdated(null)
                } else {
                    // Only variations added, still trigger refresh
                    onUpdated(null)
                }
            }
            if (typeof onClose === 'function') onClose()
            return
        } catch (err) {
            console.error('❌ Error updating product:', {
                message: err?.response?.data?.message,
                result: err?.response?.data?.result,
                status: err?.response?.status,
                data: err?.response?.data,
                fullError: err
            })
            
            const errorMessage = err?.response?.data?.message || 
                               err?.response?.data?.result?.message ||
                               err?.message || 
                               'Có lỗi khi cập nhật sản phẩm'
            
            message.error(errorMessage)
        } finally {
            setSubmitting(false)
        }
    }

    if (!open) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true">
            <div className="modal update-product-modal">
                <div className="modal-header">
                    <h2 className="modal-title">Cập nhật sản phẩm</h2>
                    <button className="btn-close" onClick={handleCancel} disabled={submitting}>×</button>
                </div>
                <form className="modal-body" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="title">Tên sản phẩm</label>
                        <input
                            id="title"
                            className="form-control"
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            placeholder="Nhập tên sản phẩm mới"
                        />
                        {product?.title && (
                            <span className="current-value">Hiện tại: {product.title}</span>
                        )}
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Mô tả</label>
                        <textarea
                            id="description"
                            className="form-control"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            placeholder="Nhập mô tả mới"
                            rows="4"
                        />
                        {product?.description && (
                            <span className="current-value">Hiện tại: {product.description}</span>
                        )}
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label htmlFor="price">Giá (VNĐ)</label>
                            <input
                                id="price"
                                className="form-control"
                                type="number"
                                value={price}
                                onChange={(e) => setPrice(e.target.value)}
                                placeholder="Nhập giá mới"
                                min="0"
                                step="1000"
                            />
                            {product?.price && (
                                <span className="current-value">
                                    Hiện tại: {new Intl.NumberFormat('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND'
                                    }).format(product.price)}
                                </span>
                            )}
                        </div>

                        <div className="form-group">
                            <label htmlFor="categoryId">Danh mục</label>
                            <Select
                                id="categoryId"
                                className="category-select"
                                value={categoryId}
                                onChange={(value) => setCategoryId(value)}
                                placeholder="Chọn danh mục mới"
                                showSearch
                                allowClear
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
                            {product?.categoryId && (
                                <span className="current-value">
                                    Hiện tại: {categories.find(c => c.categoryId === product.categoryId)?.categoryName || 
                                              categories.find(c => c.categoryId === product.categoryId)?.name || 
                                              product.categoryId}
                                </span>
                            )}
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="image">Hình ảnh sản phẩm</label>
                        
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
                                            setImagePreview(product?.image || null)
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
                        {product?.image && !removeImage && !imageFile && imagePreview && (
                            <span className="current-value">Ảnh hiện tại</span>
                        )}
                    </div>

                    <div className="update-note">
                        💡 <strong>Lưu ý:</strong> Chỉ cần điền vào các trường muốn thay đổi
                    </div>

                    {/* Variations Section */}
                    <div className="variations-section">
                        {/* Existing Variations */}
                        {existingVariations.length > 0 && (
                            <div className="existing-variations">
                                <div className="variations-header">
                                    <label>Biến thể hiện có ({existingVariations.length})</label>
                                </div>
                                <div className="existing-variations-list">
                                    {existingVariations.map((variation) => (
                                        <div key={variation.id} className="existing-variation-item">
                                            {variation.image && (
                                                <img src={variation.image} alt={`${variation.size} - ${variation.color}`} className="existing-variation-image" />
                                            )}
                                            <div className="existing-variation-details">
                                                <div><strong>Size:</strong> {variation.size}</div>
                                                <div><strong>Màu:</strong> {variation.color}</div>
                                                <div><strong>Tồn kho:</strong> {variation.stockQuantity}</div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}

                        {/* Add New Variations */}
                        <div className="new-variations">
                            <div className="variations-header">
                                <label>Thêm biến thể mới (Tùy chọn)</label>
                                <button
                                    type="button"
                                    className="btn-add-variation"
                                    onClick={handleAddNewVariation}
                                    disabled={submitting}
                                >
                                    <Plus size={16} />
                                    Thêm biến thể
                                </button>
                            </div>
                            
                            {newVariations.length > 0 && (
                                <div className="variations-list">
                                    {newVariations.map((variation, index) => (
                                        <div key={variation.id} className="variation-item">
                                            <div className="variation-header-item">
                                                <h4>Biến thể mới #{index + 1}</h4>
                                                <button
                                                    type="button"
                                                    className="btn-remove-variation"
                                                    onClick={() => handleRemoveNewVariation(variation.id)}
                                                    disabled={submitting}
                                                    title="Xóa biến thể"
                                                >
                                                    <Trash2 size={16} />
                                                </button>
                                            </div>
                                            
                                            <div className="variation-form-grid">
                                                <div className="form-group">
                                                    <label htmlFor={`new-size-${variation.id}`}>Size <span className="required">*</span></label>
                                                    <input
                                                        id={`new-size-${variation.id}`}
                                                        className="form-control"
                                                        type="text"
                                                        value={variation.size}
                                                        onChange={(e) => handleNewVariationChange(variation.id, 'size', e.target.value)}
                                                        placeholder="VD: M, L, XL"
                                                        required
                                                    />
                                                </div>

                                                <div className="form-group">
                                                    <label htmlFor={`new-color-${variation.id}`}>Màu sắc <span className="required">*</span></label>
                                                    <input
                                                        id={`new-color-${variation.id}`}
                                                        className="form-control"
                                                        type="text"
                                                        value={variation.color}
                                                        onChange={(e) => handleNewVariationChange(variation.id, 'color', e.target.value)}
                                                        placeholder="VD: Đen, Trắng"
                                                        required
                                                    />
                                                </div>

                                                <div className="form-group">
                                                    <label htmlFor={`new-stock-${variation.id}`}>Số lượng tồn kho <span className="required">*</span></label>
                                                    <input
                                                        id={`new-stock-${variation.id}`}
                                                        className="form-control"
                                                        type="number"
                                                        value={variation.stockQuantity}
                                                        onChange={(e) => handleNewVariationChange(variation.id, 'stockQuantity', e.target.value)}
                                                        placeholder="Nhập số lượng"
                                                        min="0"
                                                        required
                                                    />
                                                </div>

                                                <div className="form-group">
                                                    <label htmlFor={`new-variation-image-${variation.id}`}>Hình ảnh biến thể</label>
                                                    {!variation.imagePreview ? (
                                                        <div className="variation-image-upload-area">
                                                            <input
                                                                id={`new-variation-image-${variation.id}`}
                                                                type="file"
                                                                accept="image/*"
                                                                onChange={(e) => handleNewVariationImageChange(variation.id, e)}
                                                                className="image-input"
                                                            />
                                                            <label htmlFor={`new-variation-image-${variation.id}`} className="variation-image-upload-label">
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
                                                                onClick={() => handleRemoveNewVariationImage(variation.id)}
                                                                title="Xóa ảnh"
                                                            >
                                                                <X size={14} />
                                                            </button>
                                                        </div>
                                                    )}
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>
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

