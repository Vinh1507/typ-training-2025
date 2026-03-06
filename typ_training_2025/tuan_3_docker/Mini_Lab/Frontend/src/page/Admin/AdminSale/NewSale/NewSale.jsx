import { useEffect, useState } from 'react'
import axios from 'axios'
import { base } from '../../../../service/Base.jsx'
import { App } from 'antd'
import './NewSale.css'

export default function NewSaleModal({ open = false, onClose, onCreated, existingSales = [] }) {
    const [name, setName] = useState('')
    const [description, setDescription] = useState('')
    const [stDate, setStDate] = useState('')
    const [endDate, setEndDate] = useState('')
    const [submitting, setSubmitting] = useState(false)

    const { message } = App.useApp();

    // Hàm kiểm tra trùng lặp thời gian
    const checkTimeOverlap = (newStart, newEnd) => {
        const newStartDate = new Date(newStart)
        const newEndDate = new Date(newEnd)
        
        for (const sale of existingSales) {
            const saleStart = new Date(sale.stDate)
            const saleEnd = new Date(sale.endDate)
            
            // Kiểm tra trùng lặp: hai khoảng thời gian trùng nhau nếu:
            // newStart <= saleEnd && newEnd >= saleStart
            if (newStartDate <= saleEnd && newEndDate >= saleStart) {
                return {
                    overlap: true,
                    conflictSale: sale
                }
            }
        }
        return { overlap: false }
    }

    useEffect(() => {
        if (open) {
            // Reset form when modal opens
            setName('')
            setDescription('')
            setStDate('')
            setEndDate('')
        }
    }, [open])

    const disabled = !name.trim() || !stDate || !endDate || submitting

    const handleCancel = () => {
        if (submitting) return
        if (typeof onClose === 'function') onClose()
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        if (disabled) return
        
        // Validate dates
        const start = new Date(stDate)
        const end = new Date(endDate)
        
        if (start >= end) {
            message.error('Ngày bắt đầu phải nhỏ hơn ngày kết thúc')
            return
        }
        
        // Check time overlap
        const overlapCheck = checkTimeOverlap(stDate, endDate)
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
            // Format dates to ISO string
            const formattedStDate = new Date(stDate).toISOString()
            const formattedEndDate = new Date(endDate).toISOString()

            const payload = {
                name: name.trim(),
                description: description.trim(),
                stDate: formattedStDate,
                endDate: formattedEndDate
            }
            
            console.log('📦 Create Sale Payload:', JSON.stringify(payload, null, 2))

            const response = await axios.post(`${base}/sales`, payload, {
                headers: { 
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })
            
            console.log('✅ Create Sale Response Status:', response.status)
            console.log('✅ Create Sale Response Data:', JSON.stringify(response.data, null, 2))
            
            if (response.data?.result) {
                console.log('📋 Created sale ID:', response.data.result.id)
                console.log('📋 Created sale name:', response.data.result.name)
                console.log('📋 Created sale list_product:', response.data.result.list_product)
                console.log('📋 Number of products in response:', response.data.result.list_product?.length || 0)
                
                if (response.data.result.list_product?.length === 0) {
                    console.warn('⚠️ WARNING: Sale created but list_product is empty!')
                    console.warn('⚠️ Payload sent:', JSON.stringify(payload, null, 2))
                }
            }

            if (response.status === 200 || response.status === 201) {
                message.success('Tạo khuyến mãi thành công')
                // Chờ reload danh sách từ server
                if (typeof onCreated === 'function') await onCreated(response.data?.result)
                if (typeof onClose === 'function') onClose()
                return
            }
            message.error(response.data?.message || 'Tạo khuyến mãi thất bại')
        } catch (err) {
            console.error('❌ Create Sale Error:', err)
            console.error('❌ Error Response:', err?.response?.data)
            console.error('❌ Error Status:', err?.response?.status)
            
            const errorMessage = err?.response?.data?.message || err?.message || 'Có lỗi khi tạo khuyến mãi'
            message.error(errorMessage)
        } finally {
            setSubmitting(false)
        }
    }

    if (!open) return null

    return (
        <div className="modal-overlay" role="dialog" aria-modal="true">
            <div className="modal">
                <div className="modal-header">
                    <h2 className="modal-title">Tạo khuyến mãi mới</h2>
                </div>
                <form className="modal-body" onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Tên khuyến mãi <span className="required">*</span></label>
                        <input
                            id="name"
                            className="form-control"
                            type="text"
                            placeholder="Nhập tên khuyến mãi"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="description">Mô tả</label>
                        <textarea
                            id="description"
                            className="form-control"
                            placeholder="Nhập mô tả khuyến mãi"
                            value={description}
                            onChange={(e) => setDescription(e.target.value)}
                            rows="3"
                        />
                    </div>

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

                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={handleCancel} disabled={submitting}>
                            Hủy
                        </button>
                        <button type="submit" className="btn btn-primary" disabled={disabled}>
                            {submitting ? 'Đang tạo...' : 'Tạo khuyến mãi'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

