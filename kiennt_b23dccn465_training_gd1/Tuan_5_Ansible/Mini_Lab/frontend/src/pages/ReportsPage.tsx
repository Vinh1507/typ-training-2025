import { 
  Box, 
  Typography, 
  Card, 
  Grid,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper
} from '@mui/material'
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, PointElement, LineElement } from 'chart.js'
import { Pie, Line } from 'react-chartjs-2'
import DashboardLayout from '../components/DashboardLayout'

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, PointElement, LineElement)

const ReportsPage = () => {
  // TODO: Module 5 - Analytics & Reporting chưa có backend
  // Giữ mock data tạm thời, sẽ thay thế bằng API sau
  // Dữ liệu biểu đồ tròn (MOCK DATA - TẠM THỜI)
  const pieData = {
    labels: ['Ăn uống', 'Sinh hoạt', 'Đi lại', 'Giải trí', 'Giáo dục', 'Y tế'],
    datasets: [
      {
        data: [30, 25, 20, 15, 8, 2],
        backgroundColor: [
          '#A8C5B8',
          '#6B8E7F',
          '#4A7260',
          '#5A8372',
          '#2E5B47',
          '#7FA89B',
        ],
        borderWidth: 0,
      },
    ],
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

  // Line Chart Data - Xu hướng chi tiêu theo thời gian (MOCK DATA - TẠM THỜI)
  const lineData = {
    labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
    datasets: [
      {
        label: 'Chi tiêu',
        data: [8.5, 9.2, 8.8, 10.1, 9.5, 11.2, 10.8, 9.9, 12.1, 10.5, 9.8, 11.5],
        borderColor: '#d32f2f',
        backgroundColor: 'rgba(211, 47, 47, 0.1)',
        tension: 0.4,
        fill: true,
      },
      {
        label: 'Thu nhập',
        data: [15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15, 15],
        borderColor: '#2e7d32',
        backgroundColor: 'rgba(46, 125, 50, 0.1)',
        tension: 0.4,
        fill: true,
      },
    ],
  }

  const lineOptions = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: true,
        position: 'top' as const,
        labels: {
          usePointStyle: true,
          padding: 15,
          font: {
            size: 12,
          }
        },
      },
      tooltip: {
        callbacks: {
          label: function(context: any) {
            return context.dataset.label + ': ' + context.parsed.y + ' triệu VNĐ'
          }
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        max: 18,
        ticks: {
          stepSize: 3,
          callback: function(value: any) {
            return value + ' tr'
          }
        },
        grid: {
          color: 'rgba(0, 0, 0, 0.05)',
        }
      },
      x: {
        grid: {
          display: false,
        }
      }
    },
  }

  // Dữ liệu bảng thống kê
  const categoryStats = [
    { category: 'Sinh hoạt', amount: '3.124.000 vnd', change: '0%', color: '#6B8E7F' },
    { category: 'Ăn uống', amount: '2.345.000 vnd', change: '- 30%', color: '#A8C5B8' },
    { category: 'Giáo dục', amount: '1.322.000 vnd', change: '- 0%', color: '#2E5B47' },
    { category: 'Giải trí', amount: '1.116.000 vnd', change: '- 0%', color: '#5A8372' },
    { category: 'Y tế', amount: '1.005.000 vnd', change: '+ 10%', color: '#7FA89B' },
    { category: 'Đi lại', amount: '997.000 vnd', change: '+ 20%', color: '#4A7260' },
  ]

  return (
    <DashboardLayout>
      <Box>
        <Typography variant="h5" sx={{ mb: 4, fontWeight: 600, color: '#2E5B47' }}>
          Báo cáo:
        </Typography>

        <Grid container spacing={4}>
          {/* Line Chart - Xu hướng */}
          <Grid item xs={12}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
              }}
            >
              <Typography variant="h6" sx={{ mb: 3, fontWeight: 600, color: '#2E5B47' }}>
                Xu hướng thu chi năm 2025:
              </Typography>
              <Box sx={{ height: 300 }}>
                <Line data={lineData} options={lineOptions} />
              </Box>
            </Card>
          </Grid>

          {/* Hai biểu đồ tròn */}
          <Grid item xs={12} md={6}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
                textAlign: 'center',
              }}
            >
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600, color: '#2E5B47' }}>
                Tháng 9
              </Typography>
              <Box sx={{ maxWidth: 300, mx: 'auto' }}>
                <Pie data={pieData} options={chartOptions} />
              </Box>
            </Card>
          </Grid>

          <Grid item xs={12} md={6}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
                textAlign: 'center',
              }}
            >
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600, color: '#2E5B47' }}>
                Tháng 10
              </Typography>
              <Box sx={{ maxWidth: 300, mx: 'auto' }}>
                <Pie data={pieData} options={chartOptions} />
              </Box>
            </Card>
          </Grid>

          {/* Bảng thống kê */}
          <Grid item xs={12} md={7}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
              }}
            >
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600, color: '#2E5B47' }}>
                Thống kê:
              </Typography>

              <TableContainer component={Paper} elevation={0} sx={{ borderRadius: '12px' }}>
                <Table>
                  <TableHead sx={{ backgroundColor: '#6B8E7F' }}>
                    <TableRow>
                      <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                        Danh mục
                      </TableCell>
                      <TableCell sx={{ color: 'white', fontWeight: 600, fontSize: '1rem' }}>
                        Đã chi tiêu
                      </TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {categoryStats.map((row, index) => (
                      <TableRow
                        key={index}
                        sx={{
                          '&:nth-of-type(odd)': { backgroundColor: '#F5F3EE' },
                          '&:last-child td': { border: 0 },
                        }}
                      >
                        <TableCell sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                          <Box
                            sx={{
                              width: 12,
                              height: 12,
                              backgroundColor: row.color,
                              borderRadius: '50%',
                            }}
                          />
                          <Typography sx={{ fontWeight: 500, color: '#2E5B47' }}>
                            {row.category}
                          </Typography>
                          <Typography 
                            sx={{ 
                              ml: 1, 
                              fontSize: '0.9rem',
                              color: row.change.includes('+') ? '#d32f2f' : 
                                     row.change.includes('-') && !row.change.includes('0%') ? '#2e7d32' : '#666'
                            }}
                          >
                            {row.change}
                          </Typography>
                        </TableCell>
                        <TableCell sx={{ fontWeight: 600, color: '#2E5B47' }}>
                          {row.amount}
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            </Card>
          </Grid>

          {/* Thống kê Thu nhập/Chi tiêu */}
          <Grid item xs={12} md={5}>
            <Card
              sx={{
                p: 3,
                borderRadius: '20px',
                backgroundColor: 'white',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
                height: '100%',
              }}
            >
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47', mb: 1 }}>
                    Thu nhập:
                  </Typography>
                  <Typography variant="h4" sx={{ fontWeight: 700, color: '#2e7d32' }}>
                    15.000.000 vnd
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47', mb: 1 }}>
                    Chi tiêu:
                  </Typography>
                  <Typography variant="h4" sx={{ fontWeight: 700, color: '#d4a574' }}>
                    9.909.000 vnd
                  </Typography>
                </Box>

                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47', mb: 1 }}>
                    Còn lại:
                  </Typography>
                  <Typography variant="h4" sx={{ fontWeight: 700, color: '#d32f2f' }}>
                    5.091.000 vnd
                  </Typography>
                </Box>
              </Box>

              {/* Legend */}
              <Box sx={{ mt: 4 }}>
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2 }}>
                  {pieData.labels.map((label, index) => (
                    <Box key={index} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <Box
                        sx={{
                          width: 12,
                          height: 12,
                          backgroundColor: pieData.datasets[0].backgroundColor[index],
                          borderRadius: '50%',
                        }}
                      />
                      <Typography variant="body2" sx={{ color: '#2E5B47', fontWeight: 500 }}>
                        {label}
                      </Typography>
                    </Box>
                  ))}
                </Box>
              </Box>
            </Card>
          </Grid>
        </Grid>
      </Box>
    </DashboardLayout>
  )
}

export default ReportsPage

