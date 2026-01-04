import { useState } from 'react'
import { Link as RouterLink } from 'react-router-dom'
import {
  Box,
  Container,
  Typography,
  Button,
  Link,
  InputAdornment,
} from '@mui/material'
import { Email, ArrowBack } from '@mui/icons-material'
import CustomInput from '../components/CustomInput'
import CustomCard from '../components/CustomCard'
import backgroundImage from '../assets/nen.png' // Lấy lại ảnh nền
import vissmartLogo from '../assets/Vissmart.png' // Lấy lại logo
import api from '../api' // Import file axios

const ForgotPasswordPage = () => {
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState('') // Để hiển thị thông báo
  const [error, setError] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setMessage('')

    try {
      // Gọi API mà chúng ta đã tạo
      const response = await api.post('/auth/forgot-password', { email })

      // Hiển thị thông báo thành công từ backend
      setMessage(response.data.message)

    } catch (err: any) {
      console.error('Lỗi khi gửi yêu cầu:', err)
      setError('Đã có lỗi xảy ra. Vui lòng thử lại.')
    }
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: 'cover',
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
            Quên mật khẩu
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Nhập email của bạn để nhận hướng dẫn.
          </Typography>
        </Box>

        <CustomCard elevation={2}>
          <form onSubmit={handleSubmit}>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
              <CustomInput
                name="email"
                label="Email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Email color="action" />
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
                Gửi hướng dẫn
              </Button>

              <Box sx={{ textAlign: 'center', mt: 2 }}>
                <Link
                  component={RouterLink}
                  to="/login"
                  sx={{
                    color: 'text.secondary',
                    textDecoration: 'none',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: 0.5,
                    '&:hover': { color: 'primary.main' },
                  }}
                >
                  <ArrowBack sx={{ fontSize: '1rem' }} /> Quay lại Đăng nhập
                </Link>
              </Box>
            </Box>
          </form>
        </CustomCard>
      </Container>
    </Box>
  )
}

export default ForgotPasswordPage