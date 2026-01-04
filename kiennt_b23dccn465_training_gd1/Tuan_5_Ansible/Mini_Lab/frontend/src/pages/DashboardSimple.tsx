import { Box, Container, Grid, Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, AppBar, Toolbar, Avatar } from '@mui/material'
import { styled } from '@mui/material/styles'

const StatsCard = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(2.5),
  borderRadius: '16px',
  backgroundColor: theme.palette.primary.main,
  color: 'white',
  textAlign: 'center',
  boxShadow: '0 4px 20px rgba(107, 142, 127, 0.2)',
}))

const ChartCard = styled(Paper)(({ theme }) => ({
  padding: theme.spacing(3),
  borderRadius: '16px',
  backgroundColor: 'white',
  boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
}))

// TODO: File này có vẻ là version đơn giản của Dashboard
// Có thể xóa hoặc giữ lại nếu cần
const DashboardSimple = () => {
  // MOCK DATA - TẠM THỜI (File này có thể không dùng)
  const stats = [
    { title: 'Thu nhập tháng này', value: '100.000.000.000' },
    { title: 'Chi tiêu tháng này', value: '100.000.000.000' },
    { title: 'Số dư hiện tại', value: '100.000.000.000' },
  ]

  const recentTransactions = [
    { time: '4/10/2025', description: 'Ăn tối', amount: '-215.000 vnđ', category: 'Ăn uống' },
    { time: '1/10/2025', description: 'Tiền điện', amount: '-516.000 vnđ', category: 'Sinh hoạt' },
    { time: '29/9/2025', description: 'Đi chợ', amount: '-67.000 vnđ', category: 'Sinh hoạt' },
    { time: '22/9/2025', description: 'Quà sinh nhật', amount: '+1.000.000 vnđ', category: 'Thu nhập' },
  ]

  const PieChartPlaceholder = () => (
    <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'space-around', p: 3 }}>
      <Box sx={{ position: 'relative', width: 200, height: 200 }}>
        <Box
          sx={{
            position: 'absolute',
            width: '100%',
            height: '100%',
            borderRadius: '50%',
            background: 'conic-gradient(#2E5B47 0% 30%, #4A7C59 30% 55%, #6B8E7F 55% 80%, #8BA89D 80% 100%)',
          }}
        />
      </Box>
      <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        {[
          { label: 'Ăn uống', color: '#2E5B47' },
          { label: 'Sinh hoạt', color: '#4A7C59' },
          { label: 'Đi lại', color: '#6B8E7F' },
          { label: 'Giải trí', color: '#8BA89D' },
        ].map((item) => (
          <Box key={item.label} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <Box sx={{ width: 16, height: 16, borderRadius: '50%', bgcolor: item.color }} />
            <Typography variant="body2">{item.label}</Typography>
          </Box>
        ))}
      </Box>
    </Box>
  )

  const BarChartPlaceholder = () => (
    <Box sx={{ height: 300, display: 'flex', alignItems: 'flex-end', justifyContent: 'space-around', p: 3, gap: 0.5 }}>
      {[8, 12, 11, 10, 9, 11, 10, 12, 13, 12, 11, 10].map((height, index) => (
        <Box key={index} sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1, gap: 0.5 }}>
          <Box sx={{ width: '100%', height: `${height * 20}px`, bgcolor: '#8BA89D', borderRadius: '4px 4px 0 0' }} />
          <Box sx={{ width: '100%', height: `${(height + 2) * 20}px`, bgcolor: '#2E5B47', borderRadius: '4px 4px 0 0' }} />
        </Box>
      ))}
    </Box>
  )

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#F5F3EE' }}>
      {/* Simple Header */}
      <AppBar position="static" sx={{ bgcolor: 'white', color: 'text.primary', boxShadow: 1 }}>
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1, color: 'primary.main', fontWeight: 600 }}>
            Vissmart Dashboard
          </Typography>
          <Typography variant="body1" sx={{ mr: 2 }}>
            Xin chào, <strong>Anh Dương!</strong>
          </Typography>
          <Avatar sx={{ bgcolor: 'grey.300', color: 'grey.600' }}>D</Avatar>
        </Toolbar>
      </AppBar>

      {/* Main Content */}
      <Container maxWidth="lg" sx={{ py: 4 }}>
        {/* Stats Cards */}
        <Grid container spacing={3} sx={{ mb: 4 }}>
          {stats.map((stat, index) => (
            <Grid item xs={12} md={4} key={index}>
              <StatsCard>
                <Typography variant="body2" sx={{ opacity: 0.9, mb: 1 }}>
                  {stat.title}
                </Typography>
                <Typography variant="h5" sx={{ fontWeight: 600 }}>
                  {stat.value}
                </Typography>
              </StatsCard>
            </Grid>
          ))}
        </Grid>

        {/* Charts Section */}
        <Grid container spacing={3} sx={{ mb: 4 }}>
          <Grid item xs={12} lg={6}>
            <ChartCard>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Phân bố chi tiêu:
              </Typography>
              <PieChartPlaceholder />
            </ChartCard>
          </Grid>

          <Grid item xs={12} lg={6}>
            <ChartCard>
              <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
                Biểu đồ chi tiêu:
              </Typography>
              <BarChartPlaceholder />
            </ChartCard>
          </Grid>
        </Grid>

        {/* Recent Transactions */}
        <ChartCard>
          <Typography variant="h6" sx={{ fontWeight: 600, mb: 3 }}>
            Giao dịch gần đây:
          </Typography>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell sx={{ fontWeight: 600, borderBottom: '2px dashed #ccc' }}>Thời gian</TableCell>
                  <TableCell sx={{ fontWeight: 600, borderBottom: '2px dashed #ccc' }}>Nội dung</TableCell>
                  <TableCell sx={{ fontWeight: 600, borderBottom: '2px dashed #ccc' }}>Số tiền</TableCell>
                  <TableCell sx={{ fontWeight: 600, borderBottom: '2px dashed #ccc' }}>Danh mục</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {recentTransactions.map((transaction, index) => (
                  <TableRow key={index}>
                    <TableCell sx={{ borderBottom: '1px dashed #ddd' }}>{transaction.time}</TableCell>
                    <TableCell sx={{ borderBottom: '1px dashed #ddd' }}>{transaction.description}</TableCell>
                    <TableCell 
                      sx={{ 
                        borderBottom: '1px dashed #ddd',
                        color: transaction.amount.startsWith('-') ? '#d32f2f' : '#2e7d32',
                        fontWeight: 600,
                      }}
                    >
                      {transaction.amount}
                    </TableCell>
                    <TableCell sx={{ borderBottom: '1px dashed #ddd' }}>{transaction.category}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </ChartCard>
      </Container>
    </Box>
  )
}

export default DashboardSimple
