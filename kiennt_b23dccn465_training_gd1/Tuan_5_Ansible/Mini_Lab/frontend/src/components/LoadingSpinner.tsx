import { Box, CircularProgress, Typography } from '@mui/material'

interface LoadingSpinnerProps {
  message?: string
  size?: number
}

const LoadingSpinner = ({ message = 'Đang tải...', size = 40 }: LoadingSpinnerProps) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 2,
        py: 4,
      }}
    >
      <CircularProgress size={size} />
      {message && (
        <Typography variant="body2" color="text.secondary">
          {message}
        </Typography>
      )}
    </Box>
  )
}

export default LoadingSpinner


