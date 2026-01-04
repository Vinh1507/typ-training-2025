import { useState } from 'react'
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Switch,
  FormControlLabel,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Grid,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Chip,
} from '@mui/material'
import { Add, Delete } from '@mui/icons-material'
import DashboardLayout from '../components/DashboardLayout'

const SettingsPage = () => {
  // TODO: Module 7 - Configuration & Settings chưa có backend
  // Giữ mock data tạm thời, sẽ thay thế bằng API sau
  // User info (MOCK DATA - TẠM THỜI)
  const [userInfo, setUserInfo] = useState({
    name: 'User',
    email: 'user@example.com',
    phone: '0123456789',
  })

  const [passwordData, setPasswordData] = useState({
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  })

  const [notifications, setNotifications] = useState({
    email: true,
    push: true,
    sms: false,
  })

  const [language, setLanguage] = useState('vi')
  
  // Categories (MOCK DATA - TẠM THỜI)
  // TODO: Sẽ lấy từ API /api/categories sau
  const [categories, setCategories] = useState([
    'Ăn uống',
    'Sinh hoạt',
    'Đi lại',
    'Giải trí',
    'Giáo dục',
    'Y tế',
  ])
  
  const [newCategory, setNewCategory] = useState('')
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false)

  const handleSaveProfile = () => {
    alert('Thông tin cá nhân đã được cập nhật!')
  }

  const handleChangePassword = () => {
    if (passwordData.newPassword !== passwordData.confirmPassword) {
      alert('Mật khẩu mới không khớp!')
      return
    }
    alert('Mật khẩu đã được thay đổi!')
    setPasswordData({ currentPassword: '', newPassword: '', confirmPassword: '' })
  }

  const handleAddCategory = () => {
    if (newCategory.trim()) {
      setCategories([...categories, newCategory.trim()])
      setNewCategory('')
    }
  }

  const handleDeleteCategory = (index: number) => {
    setCategories(categories.filter((_, i) => i !== index))
  }

  const handleBackup = () => {
    alert('Dữ liệu đang được sao lưu...')
  }

  const handleRestore = () => {
    alert('Khôi phục dữ liệu...')
  }

  const handleDeleteAccount = () => {
    setDeleteDialogOpen(false)
    alert('Tài khoản đã được xóa!')
  }

  return (
    <DashboardLayout>
      <Box>
        <Typography variant="h5" sx={{ mb: 3, fontWeight: 600, color: '#2E5B47' }}>
          Cài đặt:
        </Typography>

        <Grid container spacing={3}>
          {/* Thông tin cá nhân */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Thông tin cá nhân
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                  label="Họ và tên"
                  value={userInfo.name}
                  onChange={(e) => setUserInfo({ ...userInfo, name: e.target.value })}
                  fullWidth
                />
                <TextField
                  label="Email"
                  type="email"
                  value={userInfo.email}
                  onChange={(e) => setUserInfo({ ...userInfo, email: e.target.value })}
                  fullWidth
                />
                <TextField
                  label="Số điện thoại"
                  value={userInfo.phone}
                  onChange={(e) => setUserInfo({ ...userInfo, phone: e.target.value })}
                  fullWidth
                />
                <Button
                  variant="contained"
                  onClick={handleSaveProfile}
                  sx={{
                    backgroundColor: '#6B8E7F',
                    '&:hover': { backgroundColor: '#2E5B47' },
                    mt: 1,
                  }}
                >
                  Lưu thay đổi
                </Button>
              </Box>
            </Paper>
          </Grid>

          {/* Thay đổi mật khẩu */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Thay đổi mật khẩu
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <TextField
                  label="Mật khẩu hiện tại"
                  type="password"
                  value={passwordData.currentPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, currentPassword: e.target.value })}
                  fullWidth
                />
                <TextField
                  label="Mật khẩu mới"
                  type="password"
                  value={passwordData.newPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                  fullWidth
                />
                <TextField
                  label="Xác nhận mật khẩu mới"
                  type="password"
                  value={passwordData.confirmPassword}
                  onChange={(e) => setPasswordData({ ...passwordData, confirmPassword: e.target.value })}
                  fullWidth
                />
                <Button
                  variant="contained"
                  onClick={handleChangePassword}
                  sx={{
                    backgroundColor: '#6B8E7F',
                    '&:hover': { backgroundColor: '#2E5B47' },
                    mt: 1,
                  }}
                >
                  Đổi mật khẩu
                </Button>
              </Box>
            </Paper>
          </Grid>

          {/* Cài đặt thông báo */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Cài đặt thông báo
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <FormControlLabel
                  control={
                    <Switch
                      checked={notifications.email}
                      onChange={(e) => setNotifications({ ...notifications, email: e.target.checked })}
                      sx={{
                        '& .MuiSwitch-switchBase.Mui-checked': {
                          color: '#6B8E7F',
                        },
                        '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
                          backgroundColor: '#6B8E7F',
                        },
                      }}
                    />
                  }
                  label="Thông báo qua Email"
                />
                <FormControlLabel
                  control={
                    <Switch
                      checked={notifications.push}
                      onChange={(e) => setNotifications({ ...notifications, push: e.target.checked })}
                      sx={{
                        '& .MuiSwitch-switchBase.Mui-checked': {
                          color: '#6B8E7F',
                        },
                        '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
                          backgroundColor: '#6B8E7F',
                        },
                      }}
                    />
                  }
                  label="Thông báo đẩy (Push)"
                />
                <FormControlLabel
                  control={
                    <Switch
                      checked={notifications.sms}
                      onChange={(e) => setNotifications({ ...notifications, sms: e.target.checked })}
                      sx={{
                        '& .MuiSwitch-switchBase.Mui-checked': {
                          color: '#6B8E7F',
                        },
                        '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
                          backgroundColor: '#6B8E7F',
                        },
                      }}
                    />
                  }
                  label="Thông báo qua SMS"
                />
              </Box>
            </Paper>
          </Grid>

          {/* Cài đặt ngôn ngữ */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Cài đặt ngôn ngữ
              </Typography>
              <FormControl fullWidth>
                <InputLabel>Ngôn ngữ</InputLabel>
                <Select
                  value={language}
                  label="Ngôn ngữ"
                  onChange={(e) => setLanguage(e.target.value)}
                  sx={{
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                      borderColor: '#6B8E7F',
                    },
                  }}
                >
                  <MenuItem value="vi">Tiếng Việt</MenuItem>
                  <MenuItem value="en">English</MenuItem>
                  <MenuItem value="ja">日本語</MenuItem>
                  <MenuItem value="ko">한국어</MenuItem>
                </Select>
              </FormControl>
            </Paper>
          </Grid>

          {/* Quản lý danh mục chi tiêu */}
          <Grid item xs={12}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Quản lý danh mục chi tiêu
              </Typography>
              
              <Box sx={{ display: 'flex', gap: 2, mb: 2, flexWrap: 'wrap' }}>
                <TextField
                  label="Thêm danh mục mới"
                  value={newCategory}
                  onChange={(e) => setNewCategory(e.target.value)}
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      handleAddCategory()
                    }
                  }}
                  sx={{ flex: 1, minWidth: '200px' }}
                />
                <Button
                  variant="contained"
                  startIcon={<Add />}
                  onClick={handleAddCategory}
                  sx={{
                    backgroundColor: '#6B8E7F',
                    '&:hover': { backgroundColor: '#2E5B47' },
                  }}
                >
                  Thêm
                </Button>
              </Box>

              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                {categories.map((category, index) => (
                  <Chip
                    key={index}
                    label={category}
                    onDelete={() => handleDeleteCategory(index)}
                    deleteIcon={<Delete />}
                    sx={{
                      backgroundColor: '#6B8E7F',
                      color: '#fff',
                      '& .MuiChip-deleteIcon': {
                        color: 'rgba(255, 255, 255, 0.7)',
                        '&:hover': {
                          color: '#fff',
                        },
                      },
                    }}
                  />
                ))}
              </Box>
            </Paper>
          </Grid>

          {/* Sao lưu/khôi phục dữ liệu */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#2E5B47', fontWeight: 600 }}>
                Sao lưu & Khôi phục
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Alert severity="info">
                  Sao lưu dữ liệu của bạn để tránh mất mát thông tin quan trọng.
                </Alert>
                <Button
                  variant="contained"
                  onClick={handleBackup}
                  sx={{
                    backgroundColor: '#6B8E7F',
                    '&:hover': { backgroundColor: '#2E5B47' },
                  }}
                >
                  Sao lưu dữ liệu
                </Button>
                <Button
                  variant="outlined"
                  onClick={handleRestore}
                  sx={{
                    borderColor: '#6B8E7F',
                    color: '#6B8E7F',
                    '&:hover': {
                      borderColor: '#2E5B47',
                      backgroundColor: 'rgba(107, 142, 127, 0.04)',
                    },
                  }}
                >
                  Khôi phục từ bản sao lưu
                </Button>
              </Box>
            </Paper>
          </Grid>

          {/* Xóa tài khoản */}
          <Grid item xs={12} md={6}>
            <Paper sx={{ p: 3, borderRadius: 2, boxShadow: '0 4px 16px rgba(0,0,0,0.15)', backgroundColor: 'rgba(255, 255, 255, 0.95)', backdropFilter: 'blur(10px)', border: '1px solid #f44336' }}>
              <Typography variant="h6" sx={{ mb: 2, color: '#f44336', fontWeight: 600 }}>
                Vùng nguy hiểm
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Alert severity="error">
                  Xóa tài khoản sẽ xóa vĩnh viễn tất cả dữ liệu của bạn. Hành động này không thể hoàn tác.
                </Alert>
                <Button
                  variant="contained"
                  color="error"
                  onClick={() => setDeleteDialogOpen(true)}
                >
                  Xóa tài khoản
                </Button>
              </Box>
            </Paper>
          </Grid>
        </Grid>

        {/* Dialog xác nhận xóa tài khoản */}
        <Dialog
          open={deleteDialogOpen}
          onClose={() => setDeleteDialogOpen(false)}
        >
          <DialogTitle sx={{ color: '#f44336' }}>
            Xác nhận xóa tài khoản
          </DialogTitle>
          <DialogContent>
            <DialogContentText>
              Bạn có chắc chắn muốn xóa tài khoản? Tất cả dữ liệu của bạn sẽ bị xóa vĩnh viễn và không thể khôi phục.
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button 
              onClick={() => setDeleteDialogOpen(false)}
              sx={{ color: '#6B8E7F' }}
            >
              Hủy
            </Button>
            <Button 
              onClick={handleDeleteAccount} 
              color="error" 
              variant="contained"
            >
              Xóa tài khoản
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </DashboardLayout>
  )
}

export default SettingsPage

