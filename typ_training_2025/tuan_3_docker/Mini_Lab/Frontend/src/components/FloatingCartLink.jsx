import { Link, useLocation } from 'react-router-dom'
import { ShoppingCart } from 'lucide-react'
import '../assets/style/FloatingCart.css'

function FloatingCartLink() {
  const location = useLocation()
  const currentPath = location.pathname

  const allowedPaths = new Set(['/', '/newArrivals', '/categories'])
  const shouldShow = allowedPaths.has(currentPath)

  if (!shouldShow) return null

  return (
    <Link to="/cart" className="floating-cart" aria-label="Mở giỏ hàng" title="Giỏ hàng">
      <ShoppingCart size={24} strokeWidth={2.5} />
    </Link>
  )
}

export default FloatingCartLink


