import { useEffect, useMemo, useState } from 'react'
import axios from 'axios'
import { base } from '../../../service/Base.jsx'
import { App } from 'antd'

export default function Modal({ open = false, onClose, parentIdDefault = 0, onCreated, onUpdated, mode = 'create', category = null }) {
    const [categoryName, setCategoryName] = useState('')
    const [parentId, setParentId] = useState(parentIdDefault ?? 0)
    const [imageFile, setImageFile] = useState(null)
    const [previewUrl, setPreviewUrl] = useState('')
    const [submitting, setSubmitting] = useState(false)

    const { message } = App.useApp();

    useEffect(() => {
		setParentId(parentIdDefault ?? 0)
	}, [parentIdDefault])

	useEffect(() => {
		if (!open) return
		if (mode === 'edit' && category) {
			setCategoryName(category.categoryName || '')
			setParentId((category.parentId ?? 0) || 0)
			setImageFile(null)
			setPreviewUrl('')
			return
		}
		if (mode === 'create') {
			setCategoryName('')
			setParentId(parentIdDefault ?? 0)
			setImageFile(null)
			setPreviewUrl('')
		}
	}, [open, mode, category, parentIdDefault])

    useEffect(() => {
        if (!imageFile) {
            setPreviewUrl('')
            return
        }
        const url = URL.createObjectURL(imageFile)
        setPreviewUrl(url)
        return () => URL.revokeObjectURL(url)
    }, [imageFile])

    const existingImageUrl = useMemo(() => {
        if (!(open && mode === 'edit' && category)) return ''
        const raw = category.image || category.imageUrl || category.imagePath || category.avatar || category.thumbnail || ''
        if (!raw) return ''
        if (raw.startsWith('http://') || raw.startsWith('https://')) return raw
        if (raw.startsWith('/')) return `${base}${raw}`
        return `${base}/${raw}`
    }, [open, mode, category])

    const previewToShow = previewUrl || existingImageUrl

    const getFileNameFromUrl = (url) => {
        try {
            const u = new URL(url)
            const parts = u.pathname.split('/')
            return parts[parts.length - 1] || ''
        } catch (_) {
            const parts = (url || '').split('/')
            return parts[parts.length - 1] || ''
        }
    }

    const disabled = useMemo(() => {
        return submitting || !categoryName.trim()
    }, [submitting, categoryName])

    const handleCancel = () => {
        if (submitting) return
        if (typeof onClose === 'function') onClose()
    }

    const handleCreate = async (e) => {
        e.preventDefault()
        if (disabled) return
        setSubmitting(true)
        try {
            const formData = new FormData()
            formData.append('categoryName', categoryName.trim())
            formData.append('perentId', String(parentId || 0))
            if (imageFile) formData.append('image', imageFile)

			const response = await axios.post(`${base}/category`, formData, {
				headers: { 
					'Authorization': `Bearer ${localStorage.getItem('token')}`
				 }
			})

            if (response.status === 200) {
                message.success('Tạo danh mục thành công')
                if (typeof onCreated === 'function') onCreated(response.data?.result)
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Tạo danh mục thất bại')
        } catch (err) {
            message.error(err?.response?.data?.message || 'Có lỗi khi tạo danh mục')
        } finally {
            setSubmitting(false)
        }
    }

	const handleUpdate = async (e) => {
		e.preventDefault()
		if (disabled || !category?.categoryId) return
		setSubmitting(true)
		try {
			const formData = new FormData()
			formData.append('categoryName', categoryName.trim())
			formData.append('parentId', String(parentId || 0))
			if (imageFile) formData.append('image', imageFile)

			const response = await axios.put(`${base}/category/${category.categoryId}`, formData, {
				headers: { 
					'Authorization': `Bearer ${localStorage.getItem('token')}`
				 }
			})

			if (response.status === 200) {
				message.success(response.data?.message || 'Cập nhật danh mục thành công')
				if (typeof onUpdated === 'function') onUpdated(response.data?.result)
				if (typeof onClose === 'function') onClose()
				return
			}
			message.error(response.data?.message || 'Cập nhật danh mục thất bại')
		} catch (err) {
			message.error(err?.response?.data?.message || 'Có lỗi khi cập nhật danh mục')
		} finally {
			setSubmitting(false)
		}
	}

    if (!open) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true">
            <div className="modal">
				<div className="modal-header">
					<h2 className="modal-title">{mode === 'edit' ? 'Sửa danh mục' : 'Tạo danh mục'}</h2>
				</div>
				<form className="modal-body" onSubmit={mode === 'edit' ? handleUpdate : handleCreate}>
                    <div className="form-group">
                        <label htmlFor="categoryName">Tên danh mục</label>
                        <input
                            id="categoryName"
                            className="form-control"
                            type="text"
                            placeholder="Nhập tên danh mục"
                            value={categoryName}
                            onChange={(e) => setCategoryName(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="parentId">parentId</label>
                        <input
                            id="parentId"
                            className="form-control"
                            type="number"
                            min="0"
                            value={parentId}
                            onChange={(e) => setParentId(Number(e.target.value) || 0)}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="image">Ảnh</label>
                        <input
                            id="image"
                            className="form-control"
                            type="file"
                            accept="image/*"
                            onChange={(e) => setImageFile(e.target.files && e.target.files[0] ? e.target.files[0] : null)}
                        />
                        {previewToShow && (
                            <div className="file-preview">
                                <img src={previewToShow} alt="Xem trước" />
                            </div>
                        )}
                    </div>

					<div className="modal-footer">
						<button type="button" className="btn btn-secondary" onClick={handleCancel} disabled={submitting}>Hủy</button>
						<button type="submit" className="btn btn-edit" disabled={disabled}>{mode === 'edit' ? (submitting ? 'Đang lưu...' : 'Lưu') : (submitting ? 'Đang tạo...' : 'Tạo')}</button>
					</div>
                </form>
            </div>
        </div>
    )
}