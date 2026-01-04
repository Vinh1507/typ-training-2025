import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box, Typography, TextField, Button, Card, Grid,
  MenuItem, Select, FormControl, InputLabel
} from '@mui/material'
import { ArrowBack } from '@mui/icons-material'
import DashboardLayout from '../components/DashboardLayout'
import api from '../api'

const AddAccountPage = () => {
  const currentUserId = Number(localStorage.getItem('userId')) || null
  const navigate = useNavigate()

  // State quản lý dữ liệu chung
  const [walletType, setWalletType] = useState('Bank')
  const [walletName, setWalletName] = useState('') // Tên hiển thị của ví
  const [initialBalance, setInitialBalance] = useState('')

  // State riêng cho Ngân hàng
  const [selectedBank, setSelectedBank] = useState('')
  const [accountNumber, setAccountNumber] = useState('') // Dùng chung cho STK hoặc SĐT ví
  const [bankAccountName, setBankAccountName] = useState('') // Tên chủ tài khoản ngân hàng

  // State riêng cho Ví điện tử
  const [selectedEWallet, setSelectedEWallet] = useState('')

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!currentUserId) {
      alert('Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.')
      navigate('/login')
      return
    }

    // Tạo payload cơ bản
    let newWallet: any = {
      userId: currentUserId,
      type: walletType,
      walletName: walletName, // Luôn lấy từ ô "Tên ví hiển thị"
      balance: Number(initialBalance),
      bankLinked: null,
      accountNumber: null,
      accountName: null
    }

    // --- 1. XỬ LÝ NGÂN HÀNG ---
    if (walletType === "Bank") {
      if (!selectedBank || !accountNumber || !bankAccountName) {
        alert("Vui lòng điền đầy đủ thông tin ngân hàng!");
        return;
      }
      newWallet.bankLinked = selectedBank;
      newWallet.accountNumber = accountNumber;
      newWallet.accountName = bankAccountName; // Tên chủ thẻ
    }

    // --- 2. XỬ LÝ VÍ ĐIỆN TỬ ---
    else if (walletType === "E-Wallet") {
       if (!selectedEWallet) {
          alert("Vui lòng chọn loại ví điện tử!");
          return;
       }
       newWallet.bankLinked = selectedEWallet; // Momo, ZaloPay...
       newWallet.accountNumber = accountNumber; // Số điện thoại
       newWallet.accountName = walletName; // Tên chủ ví lấy theo tên hiển thị
    }

    // --- 3. XỬ LÝ TIỀN MẶT ---
    else {
      newWallet.bankLinked = "Cash";
    }

    try {
      await api.post('/wallets', newWallet)
      navigate('/accounts')
    } catch (err) {
      console.error('❌ Lỗi khi tạo ví:', err)
      alert("Có lỗi xảy ra khi tạo ví!")
    }
  }

  if (!currentUserId) {
    return (
      <DashboardLayout>
        <Box>
          <Typography variant="h6" color="error">Không tìm thấy thông tin người dùng</Typography>
          <Typography>Vui lòng đăng nhập lại để thêm ví mới.</Typography>
        </Box>
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <Box>
        <Box sx={{ mb: 4, display: 'flex', alignItems: 'center', gap: 2 }}>
          <Button startIcon={<ArrowBack />} onClick={() => navigate('/accounts')} sx={{ color: '#6B8E7F' }}>
            Quay lại
          </Button>
          <Typography variant="h4" sx={{ fontWeight: 600, color: '#2E5B47' }}>
            Thêm ví mới
          </Typography>
        </Box>

        <Card sx={{ maxWidth: 600, mx: 'auto', p: 4, borderRadius: '20px', boxShadow: '0 4px 20px rgba(0,0,0,0.1)' }}>
          <form onSubmit={handleSubmit}>
            <Grid container spacing={3}>

              {/* 1. Loại tài khoản */}
              <Grid item xs={12}>
                <FormControl fullWidth required>
                  <InputLabel>Loại tài khoản</InputLabel>
                  <Select
                    value={walletType}
                    label="Loại tài khoản"
                    onChange={(e) => {
                      setWalletType(e.target.value);
                      // Reset form khi đổi loại
                      setSelectedBank('');
                      setSelectedEWallet('');
                      setAccountNumber('');
                      setBankAccountName('');
                    }}
                  >
                    <MenuItem value="Bank">Tài khoản Ngân hàng</MenuItem>
                    <MenuItem value="E-Wallet">Ví điện tử</MenuItem>
                    <MenuItem value="Cash">Tiền mặt</MenuItem>
                  </Select>
                </FormControl>
              </Grid>

              {/* 2. Tên ví & Số dư (Luôn hiện) */}
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Tên ví hiển thị (VD: Ví chính, Momo cá nhân)"
                  value={walletName}
                  onChange={(e) => setWalletName(e.target.value)}
                  required
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Số dư ban đầu"
                  type="number"
                  value={initialBalance}
                  onChange={(e) => setInitialBalance(e.target.value)}
                  required
                  InputProps={{
                    endAdornment: <Typography sx={{ color: '#777', ml: 1 }}>VND</Typography>
                  }}
                />
              </Grid>

              {/* --- FORM RIÊNG: NGÂN HÀNG --- */}
              {walletType === "Bank" && (
                <>
                  <Grid item xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>Chọn ngân hàng</InputLabel>
                      <Select value={selectedBank} label="Chọn ngân hàng" onChange={(e) => setSelectedBank(e.target.value)}>
                        <MenuItem value="Vietcombank">Vietcombank</MenuItem>
                        <MenuItem value="MB Bank">MB Bank</MenuItem>
                        <MenuItem value="Techcombank">Techcombank</MenuItem>
                        <MenuItem value="VPBank">VPBank</MenuItem>
                        <MenuItem value="VietinBank">VietinBank</MenuItem>
                        <MenuItem value="BIDV">BIDV</MenuItem>
                        <MenuItem value="TPBank">TPBank</MenuItem>
                        <MenuItem value="ACB">ACB</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>

                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Số tài khoản"
                      value={accountNumber}
                      onChange={(e) => setAccountNumber(e.target.value)}
                      required
                    />
                  </Grid>

                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Tên chủ thẻ (In hoa)"
                      value={bankAccountName}
                      onChange={(e) => setBankAccountName(e.target.value.toUpperCase())}
                      required
                      placeholder="NGUYEN VAN A"
                    />
                  </Grid>
                </>
              )}

              {/* --- FORM RIÊNG: VÍ ĐIỆN TỬ --- */}
              {walletType === "E-Wallet" && (
                <>
                  <Grid item xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>Chọn Ví điện tử</InputLabel>
                      <Select value={selectedEWallet} label="Chọn Ví điện tử" onChange={(e) => setSelectedEWallet(e.target.value)}>
                        <MenuItem value="Momo">Momo</MenuItem>
                        <MenuItem value="ZaloPay">ZaloPay</MenuItem>
                        <MenuItem value="ShopeePay">ShopeePay</MenuItem>
                        <MenuItem value="Viettel Money">Viettel Money</MenuItem>
                        <MenuItem value="VNPay">VNPay</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>

                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Số điện thoại đăng ký ví"
                      value={accountNumber}
                      onChange={(e) => setAccountNumber(e.target.value)}
                      required
                      type="number"
                    />
                  </Grid>
                </>
              )}

              <Grid item xs={12} sx={{ textAlign: 'center', mt: 2 }}>
                <Button
                  variant="outlined"
                  onClick={() => navigate('/accounts')}
                  sx={{ mr: 2, borderColor: '#6B8E7F', color: '#6B8E7F' }}
                >
                  Hủy
                </Button>
                <Button
                  variant="contained"
                  type="submit"
                  sx={{ backgroundColor: '#6B8E7F', '&:hover': { backgroundColor: '#2E5B47' } }}
                >
                  Lưu Ví
                </Button>
              </Grid>
            </Grid>
          </form>
        </Card>
      </Box>
    </DashboardLayout>
  )
}

export default AddAccountPage