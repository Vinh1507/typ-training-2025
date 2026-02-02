import { AlertTriangle } from 'lucide-react'
import './ConfirmDialog.css'

export default function ConfirmDialog({ open, title, message, onConfirm, onCancel }) {
    if (!open) return null

    return (
        <div className="confirm-overlay" onClick={onCancel}>
            <div className="confirm-dialog" onClick={(e) => e.stopPropagation()}>
                <div className="confirm-icon">
                    <AlertTriangle size={48} color="#dc3545" />
                </div>
                <h2 className="confirm-title">{title}</h2>
                <p className="confirm-message">{message}</p>
                <div className="confirm-actions">
                    <button className="btn-confirm-cancel" onClick={onCancel}>
                        Hủy
                    </button>
                    <button className="btn-confirm-ok" onClick={onConfirm}>
                        OK
                    </button>
                </div>
            </div>
        </div>
    )
}

