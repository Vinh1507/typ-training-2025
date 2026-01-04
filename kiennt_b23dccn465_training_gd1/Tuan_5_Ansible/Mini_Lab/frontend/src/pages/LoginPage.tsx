import { useState } from 'react'
import { useNavigate, Link as RouterLink } from 'react-router-dom'
import {
  Box,
  Container,
  Typography,
  Button,
  Link,
  InputAdornment,
  IconButton,
} from '@mui/material'
import { Visibility, VisibilityOff, Email, Lock } from '@mui/icons-material'
import CustomInput from '../components/CustomInput'
import CustomCard from '../components/CustomCard'
import backgroundImage from '../assets/nen.png'
import vissmartLogo from '../assets/Vissmart.png'
import api from '../api'

const LoginPage = () => {
  const navigate = useNavigate()
  const [showPassword, setShowPassword] = useState(false)
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  })

  // State hiển thị lỗi nếu đăng nhập thất bại
  const [error, setError] = useState('')

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('') // Reset lỗi trước khi gọi API mới

    try {
      const { email, password } = formData

      // 1. Gọi API đăng nhập
      const response = await api.post('/auth/login', {
        email,
        password,
      })

      console.log('Login Response:', response.data)

      // 2. Lưu token (bắt buộc)
      if (response.data.token) {
        localStorage.setItem('token', response.data.token)
      }

      // 2.1. Lưu userId & email để filter dữ liệu đúng người dùng
      if (response.data.userId) {
        localStorage.setItem('userId', String(response.data.userId))
      }
      if (response.data.email) {
        localStorage.setItem('userEmail', response.data.email)
      }

      // 3. LƯU TÊN NGƯỜI DÙNG (Quan trọng để hiện trên Dashboard)
      // Code này tự động kiểm tra xem backend trả về tên field là gì
      const backendName = response.data.fullName || response.data.full_name || response.data.name;

      if (backendName) {
        localStorage.setItem('userFullName', backendName);
      } else {
        // Fallback: Nếu API login không trả về tên, thử xem lúc đăng ký có lưu không
        // Nếu không có gì cả thì Dashboard sẽ hiện "User" mặc định
        console.warn("API Login không trả về field tên (fullName/full_name).");
      }

      // 4. Chuyển hướng
      navigate('/dashboard')

    } catch (err: any) {
      console.error('Lỗi đăng nhập:', err)

      // Xử lý hiển thị thông báo lỗi ra màn hình
      if (err.message === 'Network Error') {
         setError('Không thể kết nối đến máy chủ (Network Error).')
      } else if (err.response) {
        // Lỗi từ Backend trả về (ví dụ: 401 Unauthorized)
        if (err.response.status === 401 || err.response.status === 403) {
          setError('Email hoặc mật khẩu không chính xác.')
        } else if (err.response.data && err.response.data.message) {
          // Nếu backend có trả về message lỗi cụ thể
          setError(err.response.data.message)
        } else {
          setError('Đã có lỗi xảy ra. Vui lòng thử lại.')
        }
      } else {
         setError('Lỗi không xác định. Vui lòng thử lại.')
      }
    }
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundRepeat: 'no-repeat',
        display: 'flex',
        alignItems: 'center',
        py: 4,
      }}
    >
      <Container maxWidth="sm">
        <Box sx={{ textAlign: 'center', mb: 4 }}>
          <Box
            component="img"
            src={vissmartLogo}
            alt="Vissmart"
            sx={{
              height: 60,
              mb: 1,
            }}
          />
          <Typography variant="h5" sx={{ fontWeight: 600, color: 'text.primary' }}>
            Đăng nhập
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Chào mừng bạn quay trở lại!
          </Typography>
        </Box>

        <CustomCard elevation={2}>
          <form onSubmit={handleSubmit}>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>

              <CustomInput
                name="email"
                label="Email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                required
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Email color="action" />
                    </InputAdornment>
                  ),
                }}
              />

              <CustomInput
                name="password"
                label="Mật khẩu"
                type={showPassword ? 'text' : 'password'}
                value={formData.password}
                onChange={handleChange}
                required
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Lock color="action" />
                    </InputAdornment>
                  ),
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        onClick={() => setShowPassword(!showPassword)}
                        edge="end"
                      >
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />

              <Box sx={{ textAlign: 'right' }}>
                <Link
                  component={RouterLink}
                  to="/forgot-password"
                  sx={{
                    color: 'primary.main',
                    textDecoration: 'none',
                    fontSize: '0.9rem',
                    '&:hover': { textDecoration: 'underline' },
                  }}
                >
                  Quên mật khẩu?
                </Link>
              </Box>

              {/* Hiển thị lỗi nếu có */}
              {error && (
                <Typography
                  color="error"
                  variant="body2"
                  textAlign="center"
                  sx={{ backgroundColor: 'rgba(255,0,0,0.1)', p: 1, borderRadius: 1 }}
                >
                  {error}
                </Typography>
              )}

              <Button
                type="submit"
                variant="contained"
                size="large"
                fullWidth
                sx={{
                  py: 1.5,
                  borderRadius: 3,
                  fontSize: '1rem',
                  fontWeight: 600,
                  textTransform: 'none',
                }}
              >
                Đăng nhập
              </Button>

              <Box sx={{ textAlign: 'center', my: 3 }}>
                {/* Khu vực Social Login nếu cần */}
              </Box>

              <Box sx={{ textAlign: 'center', mt: 2 }}>
                <Typography variant="body2" color="text.secondary">
                  Chưa có tài khoản?{' '}
                  <Link
                    component={RouterLink}
                    to="/register"
                    sx={{
                      color: 'primary.main',
                      fontWeight: 600,
                      textDecoration: 'none',
                      '&:hover': { textDecoration: 'underline' },
                    }}
                  >
                    Đăng ký ngay
                  </Link>
                </Typography>
              </Box>
            </Box>
          </form>
        </CustomCard>
      </Container>
    </Box>
  )
}

export default LoginPage
