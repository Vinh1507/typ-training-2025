import { useState } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import { Trash2 } from 'lucide-react'
import './DeleteVariation.css'

export default function DeleteVariationModal({ open = false, onClose, onDeleted, variation }) {
    const [deleting, setDeleting] = useState(false)
    const { message } = App.useApp()

    const handleCancel = () => {
        if (deleting) return
        if (typeof onClose === 'function') onClose()
    }

    const handleConfirmDelete = async () => {
        if (!variation || !variation.id) {
            console.error('❌ No variation or variation.id found')
            message.error('Không tìm thấy biến thể để xóa')
            if (typeof onClose === 'function') onClose()
            return
        }

        console.log('🗑️ Deleting variation:', variation.id)
        setDeleting(true)
        try {
            const response = await axios.delete(`${base}/variations/${variation.id}`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            console.log('✅ Delete response:', response.status, response.data)

            if (response.status === 200 || response.status === 204) {
                message.success('Xóa biến thể thành công')
                if (typeof onDeleted === 'function') {
                    onDeleted(variation)
                }
                if (typeof onClose === 'function') {
                    onClose()
                }
                return
            }
            message.error(response.data?.message || 'Xóa biến thể thất bại')
        } catch (err) {
            console.error('❌ Error deleting variation:', err)
            console.error('❌ Error response:', err?.response?.data)
            message.error(err?.response?.data?.message || 'Có lỗi khi xóa biến thể')
        } finally {
            setDeleting(false)
        }
    }

    if (!open || !variation) {
        console.log('⚠️ DeleteVariationModal not rendering:', { open, variation: !!variation })
        return null
    }

    console.log('✅ DeleteVariationModal rendering for variation:', variation.id)

    return (
        <div className="modal-overlay delete-variation-overlay" onClick={handleCancel} role="dialog" aria-modal="true">
            <div className="modal delete-variation-modal" onClick={(e) => e.stopPropagation()}>
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
                            Bạn có chắc chắn muốn xóa biến thể này?
                        </p>
                        <p className="delete-confirm-subtext">
                            Size: <strong>{variation.size}</strong> | Màu: <strong>{variation.color}</strong>
                        </p>
                        <p className="delete-confirm-subtext">
                            Hành động này không thể hoàn tác. Biến thể sẽ bị xóa vĩnh viễn.
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
                        {deleting ? 'Đang xóa...' : 'Xóa biến thể'}
                    </button>
                </div>
            </div>
        </div>
    )
}

