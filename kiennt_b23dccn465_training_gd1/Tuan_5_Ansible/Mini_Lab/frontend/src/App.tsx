import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import { ThemeProvider, createTheme } from '@mui/material/styles'
import CssBaseline from '@mui/material/CssBaseline'
import LandingPage from './pages/LandingPage'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'
import AccountManagementPage from './pages/AccountManagementPage'
import AddAccountPage from './pages/AddAccountPage'
import TransactionManagementPage from './pages/TransactionManagementPage'
import BudgetManagementPage from './pages/BudgetManagementPage'
import ExpenseAnalysisPage from './pages/ExpenseAnalysisPage'
import ReportsPage from './pages/ReportsPage'
import SettingsPage from './pages/SettingsPage'
import ChatPage from './pages/ChatPage'
import ChatWidget from './components/ChatWidget'
import ProtectedRoute from './components/ProtectedRoute'

// ===================================
// BẮT ĐẦU PHẦN THÊM MỚI
// ===================================
import ForgotPasswordPage from './pages/ForgotPasswordPage'
import ResetPasswordPage from './pages/ResetPasswordPage'
// ===================================
// KẾT THÚC PHẦN THÊM MỚI
// ===================================


// Tạo theme tùy chỉnh
const theme = createTheme({
  palette: {
    primary: {
      main: '#6B8E7F', // Màu xanh lá như trong mockup
      light: '#8BA89D',
      dark: '#567165',
    },
    secondary: {
      main: '#D4A574', // Màu vàng nâu
    },
    background: {
      default: '#F5F3EE', // Màu nền be nhạt
      paper: '#FFFFFF',
    },
  },
  typography: {
    fontFamily: '"Segoe UI", "Roboto", "Arial", sans-serif',
  },
})

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <Routes>
          {/* Public Routes - Không cần đăng nhập */}
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/reset-password" element={<ResetPasswordPage />} />

          {/* Protected Routes - Cần đăng nhập */}
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <DashboardPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/accounts"
            element={
              <ProtectedRoute>
                <AccountManagementPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/accounts/add"
            element={
              <ProtectedRoute>
                <AddAccountPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/transactions"
            element={
              <ProtectedRoute>
                <TransactionManagementPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/budgets"
            element={
              <ProtectedRoute>
                <BudgetManagementPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/analysis"
            element={
              <ProtectedRoute>
                <ExpenseAnalysisPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/reports"
            element={
              <ProtectedRoute>
                <ReportsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/settings"
            element={
              <ProtectedRoute>
                <SettingsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/chat"
            element={
              <ProtectedRoute>
                <ChatPage />
              </ProtectedRoute>
            }
          />
        </Routes>
        {/* ChatWidget hiển thị trên mọi trang (sau khi đăng nhập) */}
        <ChatWidget />
      </Router>
    </ThemeProvider>
  )
}

export default App