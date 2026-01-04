import AdminNavbar from './Components/Navbar.jsx'
import './Admin.css'
import { Route, Routes, Outlet } from 'react-router-dom'
import AdminCategory from './AdminCategory/AdminCategory.jsx'
import AdminSale from './AdminSale/AdminSale.jsx'
import GetProduct from './AdminProduct/GetProduct/GetProduct.jsx'

export default function Admin() {
    return (
        <div className="admin-page">
            <AdminNavbar />
            <main className="admin-main">
                <Routes>
                    <Route index element={<div>Dashboard</div>} />
                    <Route path="users" element={<div>Users</div>} />
                    <Route path="products" element={<GetProduct />} />
                    <Route path="sales" element={<AdminSale />} />
                    <Route path="cart" element={<div>Cart</div>} />
                    <Route path="orders" element={<div>Orders</div>} />
                    <Route path="categories" element={<AdminCategory />} />
                    <Route path="feedback" element={<div>Feedback</div>} />
                    <Route path="reviews" element={<div>Reviews</div>} />
                    <Route path="addresses" element={<div>Addresses</div>} />
                    <Route path="payments" element={<div>Payments</div>} />
                    <Route path="settings" element={<div>Settings</div>} />
                </Routes>
            </main>
        </div>
    )
}