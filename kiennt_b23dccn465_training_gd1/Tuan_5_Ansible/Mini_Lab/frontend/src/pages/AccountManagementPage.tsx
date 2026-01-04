import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Box, Typography, Card, IconButton, Button, Grid } from '@mui/material'
import { Visibility, VisibilityOff, Delete } from '@mui/icons-material'
import DashboardLayout from '../components/DashboardLayout'
import api from '../api'

interface Wallet {
  id: number
  walletName: string
  type: string
  balance: number
  bankLinked?: string
  accountNumber?: string
  accountName?: string
}

const AccountManagementPage = () => {
  const navigate = useNavigate()
  const [showBalance, setShowBalance] = useState(true)
  const [wallets, setWallets] = useState<Wallet[]>([])

  const fetchWallets = async () => {
    try {
      const res = await api.get('/wallets')
      setWallets(res.data)
    } catch (err) {
      console.error('❌ Lỗi tải ví:', err)
    }
  }

  useEffect(() => {
    fetchWallets()
  }, [])

  const handleDelete = async (id: number) => {
    if (window.confirm('Bạn có chắc muốn xóa ví này không?')) {
      try {
        await api.delete(`/wallets/${id}`)
        setWallets((prev) => prev.filter((w) => w.id !== id))
      } catch (err) {
        console.error("Lỗi xóa ví:", err)
      }
    }
  }

  // 🎯 Tách ví thành 3 nhóm
  const cashWallets = wallets.filter((w) => w.type === 'Cash')
  const ewallets = wallets.filter((w) => w.type === 'E-Wallet')
  const bankWallets = wallets.filter((w) => w.type === 'Bank')

  // Tổng số dư
  const totalBalance = wallets
    .reduce((sum, w) => sum + (w.balance || 0), 0)
    .toLocaleString()

  const renderWalletCard = (wallet: Wallet) => (
    <Grid item xs={12} md={6} key={wallet.id}>
      <Card
        sx={{
          p: 3,
          borderRadius: '20px',
          border: '2px solid #6B8E7F',
          backgroundColor: 'white',
          '&:hover': { boxShadow: '0 8px 24px rgba(107, 142, 127, 0.2)' },
        }}
      >
        <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47' }}>
          {wallet.walletName}
        </Typography>

        {/* Hiển thị thông tin phụ cho Ngân hàng/Ví điện tử */}
        {(wallet.type === 'Bank' || wallet.type === 'E-Wallet') && (
          <>
            <Typography variant="body2" sx={{ color: '#6B8E7F', fontStyle: 'italic' }}>
              {wallet.bankLinked}
            </Typography>
            <Typography variant="body2" sx={{ color: '#6B8E7F' }}>
              {wallet.type === 'Bank' ? 'Số TK: ' : 'SĐT: '}{wallet.accountNumber || '-'}
            </Typography>
          </>
        )}

        <Typography variant="h5" sx={{ fontWeight: 700, color: '#2E5B47', mt: 1 }}>
          {showBalance ? `${wallet.balance.toLocaleString()} VND` : '********'}
        </Typography>

        <Box sx={{ textAlign: 'right', mt: 2 }}>
          <IconButton sx={{ color: '#f44336' }} onClick={() => handleDelete(wallet.id)}>
            <Delete />
          </IconButton>
        </Box>
      </Card>
    </Grid>
  )

  return (
    <DashboardLayout>
      <Box>
        {/* Tổng tài sản */}
        <Box sx={{ mb: 4 }}>
          <Typography variant="h5" sx={{ mb: 2, fontWeight: 600, color: '#2E5B47' }}>
            Tổng tài sản:
          </Typography>

          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <Typography variant="h3" sx={{ fontWeight: 700, color: '#2E5B47' }}>
              {showBalance ? `${totalBalance} VND` : '********'}
            </Typography>

            <IconButton onClick={() => setShowBalance(!showBalance)} sx={{ color: '#2E5B47' }}>
              {showBalance ? <Visibility /> : <VisibilityOff />}
            </IconButton>
          </Box>
        </Box>

        {/* 1. Ví tiền mặt */}
        <Typography variant="h5" sx={{ mb: 1, fontWeight: 700, color: '#2E5B47' }}>
          Ví tiền mặt
        </Typography>
        {cashWallets.length === 0 ? (
          <Box sx={{ border: '2px dashed #6B8E7F', p: 2, borderRadius: '12px', color: '#6B8E7F', textAlign: 'center', bgcolor: 'rgba(107,142,127,0.05)', mb: 4 }}>
            Bạn chưa có ví tiền mặt
          </Box>
        ) : (
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {cashWallets.map(renderWalletCard)}
          </Grid>
        )}

        {/* 2. Ví điện tử */}
        <Typography variant="h5" sx={{ mb: 1, fontWeight: 700, color: '#2E5B47' }}>
          Ví điện tử
        </Typography>
        {ewallets.length === 0 ? (
          <Box sx={{ border: '2px dashed #6B8E7F', p: 2, borderRadius: '12px', color: '#6B8E7F', textAlign: 'center', bgcolor: 'rgba(107,142,127,0.05)', mb: 4 }}>
            Bạn chưa có ví điện tử
          </Box>
        ) : (
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {ewallets.map(renderWalletCard)}
          </Grid>
        )}

        {/* 3. Liên kết ngân hàng (ĐÃ SỬA LỖI Ở ĐÂY) */}
        <Typography variant="h5" sx={{ mb: 1, fontWeight: 600, color: '#2E5B47' }}>
          Liên kết ngân hàng
        </Typography>

        {/* Kiểm tra: Nếu không có ngân hàng -> Hiện thông báo. Có -> Hiện danh sách */}
        {bankWallets.length === 0 ? (
          <Box sx={{ border: '2px dashed #6B8E7F', p: 2, borderRadius: '12px', color: '#6B8E7F', textAlign: 'center', bgcolor: 'rgba(107,142,127,0.05)', mb: 4 }}>
            Bạn chưa có tài khoản ngân hàng
          </Box>
        ) : (
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {bankWallets.map(renderWalletCard)}
          </Grid>
        )}

        {/* Nút thêm ví */}
        <Box sx={{ textAlign: 'center', mt: 4 }}>
          <Button
            variant="contained"
            onClick={() => navigate('/accounts/add')}
            sx={{
              backgroundColor: '#6B8E7F',
              color: 'white',
              borderRadius: '50px',
              px: 6,
              py: 2,
              fontSize: '1.1rem',
              fontWeight: 600,
              textTransform: 'none',
              boxShadow: '0 4px 12px rgba(107, 142, 127, 0.3)',
              '&:hover': { backgroundColor: '#5A7A6D' },
            }}
          >
            + Thêm ví mới
          </Button>
        </Box>
      </Box>
    </DashboardLayout>
  )
}

export default AccountManagementPage