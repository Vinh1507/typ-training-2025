import { Box, Typography, Container } from '@mui/material'

const SimpleDashboard = () => {
  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Dashboard Test
        </Typography>
        <Typography variant="body1">
          Đây là trang dashboard đơn giản để test. Nếu bạn thấy text này thì ứng dụng hoạt động bình thường.
        </Typography>
      </Box>
    </Container>
  )
}

export default SimpleDashboard
