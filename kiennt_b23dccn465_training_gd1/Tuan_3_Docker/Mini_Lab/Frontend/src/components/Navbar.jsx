import { Link } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import '../assets/style/Navbar.css'
import Brand from './Brand.jsx'
import { ShoppingCart, User, ChevronDown, MessageCircle, Search, X } from 'lucide-react'
import Category from './CategoryNavbar.jsx'
import { App } from 'antd'
import { useState, useEffect } from 'react'
import { jwtDecode } from "jwt-decode";


function Navbar() {
  const { message } = App.useApp();
  const token = localStorage.getItem('token')

  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isAdmin, setIsAdmin] = useState(false)
  const [showSearch, setShowSearch] = useState(false)
  const [searchQuery, setSearchQuery] = useState('')
  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.clear()
    message.success('Đăng xuất thành công')
    setIsLoggedIn(false)
    setIsAdmin(false)
  }

  const checkLogin = () => {
    if (token) {
      setIsLoggedIn(true)
    } else {
      setIsLoggedIn(false)
      setIsAdmin(false)
    }
  }

  const decodeToken = () => {
    if (token) {
      const decodedToken = jwtDecode(token)
      if (decodedToken.scope.includes('ROLE_ADMIN')) {
        localStorage.setItem('isAdmin', true)
        setIsAdmin(true)
      } else {
        localStorage.setItem('isAdmin', false)
        setIsAdmin(false)
      }
      
    }
  }

  useEffect(() => {
    checkLogin()
    decodeToken()
  }, [])

  const handleSearchToggle = () => {
    setShowSearch(!showSearch)
    if (showSearch) {
      setSearchQuery('')
    }
  }

  const handleSearchSubmit = (e) => {
    e.preventDefault()
    if (searchQuery.trim()) {
      // Navigate to search results page with query
      navigate(`/search?q=${encodeURIComponent(searchQuery.trim())}`)
      setShowSearch(false)
      setSearchQuery('')
    }
  }



  return (
    <header className="navbar-header">
      <nav className="navbar">
        <div className="navbar-brand" onClick={() => navigate('/')}>
          <Brand />
        </div>

        <ul className="navbar-menu">
          <li><Link to="/" className="navbar-link">Trang chủ</Link></li>
          <li><Link to="/newArrivals" className="navbar-link">Hàng mới dề</Link></li>
          <li className="navbar-dropdown">
            <span className="navbar-link">Danh mục</span>
            <ChevronDown className="navbar-dropdown-icon" size={22} strokeWidth={2} />
            <div className="navbar-dropdown-menu">
              <Category />
            </div>
          </li> 

          {isLoggedIn ? (
            <li><Link to="#" onClick={handleLogout} className="navbar-link">Đăng xuất</Link></li>
          ) : (
            <>
              <li><Link to="/login" className="navbar-link">Đăng nhập</Link></li>
              <li><Link to="/register" className="signup-btn">Đăng ký</Link></li>
            </>
          )}

          {isAdmin ? (
            <li><Link to="/admin" className="signup-btn">Quản trị</Link></li>
          ) : (
            <></>
          )}
          <li>
            <button 
              className="navbar-link navbar-icon-btn" 
              onClick={handleSearchToggle}
              aria-label="Tìm kiếm" 
              title="Tìm kiếm"
            >
              <Search size={22} strokeWidth={2} />
            </button>
          </li>
          <li><Link to="/user" className="navbar-link" aria-label="Tài khoản" title="Tài khoản"><User size={22} strokeWidth={2} /></Link></li>
          <li><Link to="/cart" className="navbar-link" aria-label="Giỏ hàng" title="Giỏ hàng"><ShoppingCart size={22} strokeWidth={2} /></Link></li>
          <li><Link to="/chat" className="navbar-link" aria-label="Chat" title="Chat"><MessageCircle size={22} strokeWidth={2} /></Link></li>
        </ul>

      </nav>

      {/* Search Bar Overlay */}
      {showSearch && (
        <div className="search-overlay" onClick={handleSearchToggle}>
          <div className="search-container" onClick={(e) => e.stopPropagation()}>
            <button 
              className="search-close"
              onClick={handleSearchToggle}
              aria-label="Đóng"
            >
              <X size={20} />
            </button>
            <form onSubmit={handleSearchSubmit} className="search-form">
              <Search className="search-icon" size={17} />
              <input
                type="text"
                className="search-input"
                placeholder="Tìm theo tên sản phẩm..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                autoFocus
              />
              {searchQuery && (
                <button
                  type="button"
                  className="search-clear"
                  onClick={() => setSearchQuery('')}
                  aria-label="Xóa"
                >
                  <X size={15} />
                </button>
              )}
              <button type="submit" className="search-submit" aria-label="Tìm kiếm">
                <Search size={18} />
              </button>
            </form>
            <div className="search-suggestions">
              <span onClick={() => { setSearchQuery('Áo thun'); }}>Áo thun</span>
              <span onClick={() => { setSearchQuery('Quần tây'); }}>Quần tây</span>
              <span onClick={() => { setSearchQuery('Áo polo'); }}>Áo polo</span>
            </div>
          </div>
        </div>
      )}
    </header>
  )
}

export default Navbar


