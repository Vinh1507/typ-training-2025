import { Navigate } from 'react-router-dom'
import { ReactNode } from 'react'

interface ProtectedRouteProps {
  children: ReactNode
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  // Kiểm tra xem user đã đăng nhập chưa (có token trong localStorage)
  const token = localStorage.getItem('token')

  // Nếu chưa có token, redirect về trang login
  if (!token) {
    return <Navigate to="/login" replace />
  }

  // Nếu đã có token, cho phép truy cập
  return <>{children}</>
}

export default ProtectedRoute

