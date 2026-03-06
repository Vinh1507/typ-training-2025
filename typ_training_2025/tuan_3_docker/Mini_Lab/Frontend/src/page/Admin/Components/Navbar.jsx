import { NavLink } from 'react-router-dom'

export default function AdminNavbar() {
    return (
        <aside className="admin-sidebar">
            <nav className="admin-nav">
                <ul>
                    <li><NavLink to="/admin/users" className={({ isActive }) => isActive ? 'active' : ''}>Users</NavLink></li>
                    <li><NavLink to="/admin/products" className={({ isActive }) => isActive ? 'active' : ''}>Products</NavLink></li>
                    <li><NavLink to="/admin/sales" className={({ isActive }) => isActive ? 'active' : ''}>Sales</NavLink></li>
                    <li><NavLink to="/admin/categories" className={({ isActive }) => isActive ? 'active' : ''}>Categories</NavLink></li>
                    <li><NavLink to="/admin/feedback" className={({ isActive }) => isActive ? 'active' : ''}>Feedback</NavLink></li>
                    <li><NavLink to="/admin/reviews" className={({ isActive }) => isActive ? 'active' : ''}>Reviews</NavLink></li>
                    <li><NavLink to="/admin/cart" className={({ isActive }) => isActive ? 'active' : ''}>Cart</NavLink></li>
                    <li><NavLink to="/admin/orders" className={({ isActive }) => isActive ? 'active' : ''}>Orders</NavLink></li>
                    <li><NavLink to="/admin/addresses" className={({ isActive }) => isActive ? 'active' : ''}>Addresses</NavLink></li>
                    <li><NavLink to="/admin/payments" className={({ isActive }) => isActive ? 'active' : ''}>Payments</NavLink></li>
                    <li><NavLink to="/admin/settings" className={({ isActive }) => isActive ? 'active' : ''}>Settings</NavLink></li>
                    <li><NavLink to="/admin" end className={({ isActive }) => isActive ? 'active' : ''}>Dashboard</NavLink></li>
                </ul>
            </nav>
        </aside>
    )
}