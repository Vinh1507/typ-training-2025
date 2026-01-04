import { Box, Typography, Button } from '@mui/material'
import ReceiptLongOutlined from '@mui/icons-material/ReceiptLongOutlined'

interface EmptyStateProps {
  title: string
  description?: string
  actionText?: string
  onAction?: () => void
}

const EmptyState = ({ title, description, actionText, onAction }: EmptyStateProps) => {
  return (
    <Box
      sx={{
        py: 5,
        px: 2,
        textAlign: 'center',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: 1.5,
      }}
    >
      <ReceiptLongOutlined sx={{ fontSize: 48, color: '#6B8E7F' }} />
      <Typography variant="h6" sx={{ fontWeight: 600, color: '#2E5B47' }}>
        {title}
      </Typography>
      {description && (
        <Typography variant="body2" sx={{ color: '#53655C', maxWidth: 360 }}>
          {description}
        </Typography>
      )}
      {actionText && onAction && (
        <Button
          variant="contained"
          onClick={onAction}
          sx={{
            mt: 1,
            borderRadius: '24px',
            px: 4,
            backgroundColor: '#6B8E7F',
            '&:hover': { backgroundColor: '#2E5B47' },
          }}
        >
          {actionText}
        </Button>
      )}
    </Box>
  )
}

export default EmptyState




