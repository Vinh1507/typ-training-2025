import { useState } from 'react'
import { useNavigate, useSearchParams, Link as RouterLink } from 'react-router-dom'
import {
  Box,
  Container,
  Typography,
  Button,
  Link,
  InputAdornment,
  IconButton,
} from '@mui/material'
import { Visibility, VisibilityOff, Lock } from '@mui/icons-material'
import CustomInput from '../components/CustomInput'
import CustomCard from '../components/CustomCard'
import backgroundImage from '../assets/nen.png'
import vissmartLogo from '../assets/Vissmart.png'
import api from '../api'

const ResetPasswordPage = () => {
  const navigate = useNavigate()

  // Dùng hook này để đọc query params từ URL
  const [searchParams] = useSearchParams()
  const token = searchParams.get('token') // Lấy token

  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [formData, setFormData] = useState({
    password: '',
    confirmPassword: '',
  })
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setMessage('')

    if (formData.password !== formData.confirmPassword) {
      setError('Mật khẩu xác nhận không khớp!')
      return
    }

    if (!token) {
      setError('Token không hợp lệ hoặc đã hết hạn. Vui lòng thử lại.')
      return
    }

    try {
      // Gọi API đặt lại mật khẩu với token và mật khẩu mới
      const response = await api.post('/auth/reset-password', {
        token: token,
        newPassword: formData.password,
      })

      setMessage(response.data.message) // "Mật khẩu đã được đặt lại thành công."

      // Chờ 2 giây rồi chuyển về trang đăng nhập
      setTimeout(() => {
        navigate('/login')
      }, 2000)

    } catch (err: any) {
      console.error('Lỗi khi đặt lại mật khẩu:', err)
      if (err.response && err.response.data && err.response.data.message) {
        // Hiển thị lỗi từ backend (ví dụ: "Token đã hết hạn")
        setError(err.response.data.message)
      } else {
        setError('Đã có lỗi xảy ra. Vui lòng thử lại.')
      }
    }
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        backgroundImage: `url(${backgroundImage})`,
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
            sx={{ height: 60, mb: 1 }}
          />
          <Typography variant="h5" sx={{ fontWeight: 600, color: 'text.primary' }}>
            Đặt lại mật khẩu
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Nhập mật khẩu mới cho tài khoản của bạn.
          </Typography>
        </Box>

        <CustomCard elevation={2}>
          <form onSubmit={handleSubmit}>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
              <CustomInput
                name="password"
                label="Mật khẩu mới"
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
                      <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                        {showPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />

              <CustomInput
                name="confirmPassword"
                label="Xác nhận mật khẩu mới"
                type={showConfirmPassword ? 'text' : 'password'}
                value={formData.confirmPassword}
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
                      <IconButton onClick={() => setShowConfirmPassword(!showConfirmPassword)} edge="end">
                        {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              />

              {/* Hiển thị thông báo (thành công hoặc lỗi) */}
              {message && (
                <Typography color="green" variant="body2" textAlign="center">
                  {message}
                </Typography>
              )}
              {error && (
                <Typography color="error" variant="body2" textAlign="center">
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
                Lưu mật khẩu mới
              </Button>
            </Box>
          </form>
        </CustomCard>
      </Container>
    </Box>
  )
}

export default ResetPasswordPage