import { useState, useEffect } from 'react'
import { 
  Box, 
  Typography, 
  Card, 
  Grid,
  FormControl,
  Select,
  MenuItem,
  SelectChangeEvent
} from '@mui/material'
import { Chart as ChartJS, ArcElement, CategoryScale, LinearScale, BarElement, Tooltip, Legend } from 'chart.js'
import { Pie, Bar } from 'react-chartjs-2'
import DashboardLayout from '../components/DashboardLayout'
import api from '../api'

ChartJS.register(ArcElement, CategoryScale, LinearScale, BarElement, Tooltip, Legend)

interface Transaction {
  transactionDate: string
  amount: number
  isIncome: boolean
  category: string
}

const ExpenseAnalysisPage = () => {
  const currentUserId = Number(localStorage.getItem('userId')) || null
  const [selectedCategory, setSelectedCategory] = useState('')
  const [pieData, setPieData] = useState({
    labels: [] as string[],
    datasets: [{
      data: [] as number[],
        backgroundColor: [
          '#A8C5B8',
          '#6B8E7F',
          '#4A7260',
          '#5A8372',
          '#2E5B47',
          '#7FA89B',
        ],
        borderWidth: 0,
    }],
  })
  const [barData, setBarData] = useState({
    labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
    datasets: [
      {
        label: 'Chi tiêu',
        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        backgroundColor: '#2E5B47',
      },
      {
        label: 'Thu nhập',
        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        backgroundColor: '#A8C5B8',
      },
    ],
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchAnalysisData()
  }, [])

  const fetchAnalysisData = async () => {
    if (!currentUserId) {
      setLoading(false)
      return
    }
    try {
      setLoading(true)
      const currentYear = new Date().getFullYear()

      // Fetch all transactions
      const res = await api.get('/transactions', {
        params: {
          userId: currentUserId,
          page: 0,
          limit: 1000,
        }
      })
      const transactions: Transaction[] = res.data.content || []

      // Tính toán pie chart (phân bố chi tiêu theo category)
      const expenseByCategory: { [key: string]: number } = {}
      transactions
        .filter(t => !t.isIncome && t.category)
        .forEach(t => {
          expenseByCategory[t.category] = (expenseByCategory[t.category] || 0) + (t.amount || 0)
        })

      const categories = Object.keys(expenseByCategory)
      const amounts = Object.values(expenseByCategory)
      const total = amounts.reduce((sum, a) => sum + a, 0)
      const percentages = amounts.map(a => total > 0 ? Math.round((a / total) * 100) : 0)

      if (categories.length > 0) {
        setSelectedCategory(categories[0])
      }

      setPieData({
        labels: categories,
        datasets: [{
          data: percentages,
          backgroundColor: [
            '#A8C5B8',
            '#6B8E7F',
            '#4A7260',
            '#5A8372',
            '#2E5B47',
            '#7FA89B',
          ],
          borderWidth: 0,
        }],
      })

      // Tính toán bar chart (theo tháng)
      const monthlyData: { [key: number]: { income: number, expense: number } } = {}
      for (let i = 0; i < 12; i++) {
        monthlyData[i] = { income: 0, expense: 0 }
      }

      transactions.forEach(t => {
        if (t.transactionDate) {
          const date = new Date(t.transactionDate)
          if (date.getFullYear() === currentYear) {
            const month = date.getMonth()
            if (t.isIncome) {
              monthlyData[month].income += (t.amount || 0) / 1000000 // Convert to triệu
            } else {
              monthlyData[month].expense += (t.amount || 0) / 1000000
            }
          }
        }
      })

      setBarData({
        labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        datasets: [
          {
            label: 'Chi tiêu',
            data: Object.values(monthlyData).map(d => Math.round(d.expense)),
            backgroundColor: '#2E5B47',
          },
          {
            label: 'Thu nhập',
            data: Object.values(monthlyData).map(d => Math.round(d.income)),
            backgroundColor: '#A8C5B8',
          },
        ],
      })

    } catch (error) {
      console.error('Error fetching analysis data:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleCategoryChange = (event: SelectChangeEvent) => {
    setSelectedCategory(event.target.value)
  }

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: false,
      },
    },
  }

  const barOptions = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: true,
        position: 'top' as const,
        labels: {
          usePointStyle: true,
          padding: 15,
        },
      },
    },
    scales: {
      y: {
        beginAtZero: true,
        max: 12,
        ticks: {
          stepSize: 2,
        },
      },
    },
  }

  if (!currentUserId) {
    return (
      <DashboardLayout>
        <Box>
          <Typography variant="h6" color="error">Không tìm thấy thông tin người dùng</Typography>
          <Typography>Vui lòng đăng nhập lại để xem phân tích chi tiêu.</Typography>
        </Box>
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <Box>
        <Typography variant="h5" sx={{ mb: 4, fontWeight: 600, color: '#2E5B47' }}>
          Phân tích chi tiêu:
        </Typography>

        <Grid container spacing={4}>
          {/* Biểu đồ tròn */}
          <Grid item xs={12} md={6}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
                height: '100%',
              }}
            >
              {loading ? (
                <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Typography>Đang tải dữ liệu...</Typography>
                </Box>
              ) : pieData.labels.length === 0 ? (
                <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Typography>Chưa có dữ liệu chi tiêu</Typography>
                </Box>
              ) : (
                <>
              <Box sx={{ maxWidth: 400, mx: 'auto' }}>
                <Pie data={pieData} options={chartOptions} />
              </Box>

              {/* Legend */}
              <Box sx={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center', gap: 2, mt: 3 }}>
                {pieData.labels.map((label, index) => (
                  <Box key={index} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Box
                      sx={{
                        width: 16,
                        height: 16,
                            backgroundColor: pieData.datasets[0].backgroundColor[index % pieData.datasets[0].backgroundColor.length],
                        borderRadius: '4px',
                      }}
                    />
                    <Typography variant="body2" sx={{ color: '#2E5B47', fontWeight: 500 }}>
                          {label} ({pieData.datasets[0].data[index]}%)
                    </Typography>
                  </Box>
                ))}
              </Box>
                </>
              )}
            </Card>
          </Grid>

          {/* Biểu đồ cột với dropdown */}
          <Grid item xs={12} md={6}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
                height: '100%',
              }}
            >
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600, color: '#2E5B47' }}>
                Thống kê chi tiết:
              </Typography>

              {pieData.labels.length > 0 && (
              <FormControl fullWidth sx={{ mb: 3 }}>
                <Select
                  value={selectedCategory}
                  onChange={handleCategoryChange}
                  sx={{
                    borderRadius: '12px',
                    backgroundColor: '#6B8E7F',
                    color: 'white',
                    '& .MuiOutlinedInput-notchedOutline': {
                      border: 'none',
                    },
                    '& .MuiSvgIcon-root': {
                      color: 'white',
                    },
                  }}
                >
                  {pieData.labels.map((label) => (
                    <MenuItem key={label} value={label}>
                      {label}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              )}

              {loading ? (
                <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Typography>Đang tải dữ liệu...</Typography>
                </Box>
              ) : (
              <Box>
                <Bar data={barData} options={barOptions} />
              </Box>
              )}
            </Card>
          </Grid>
        </Grid>
      </Box>
    </DashboardLayout>
  )
}

export default ExpenseAnalysisPage

