import { Card, CardContent, CardHeader, CardActions } from '@mui/material'
import { styled } from '@mui/material/styles'
import { ReactNode } from 'react'

const StyledCard = styled(Card)(({ theme }) => ({
  borderRadius: 16,
  boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
  transition: 'all 0.3s ease',
  '&:hover': {
    boxShadow: '0 8px 30px rgba(0, 0, 0, 0.12)',
    transform: 'translateY(-2px)',
  },
}))

interface CustomCardProps {
  title?: string
  subtitle?: string
  children: ReactNode
  actions?: ReactNode
  elevation?: number
}

const CustomCard = ({ title, subtitle, children, actions, elevation = 0 }: CustomCardProps) => {
  return (
    <StyledCard elevation={elevation}>
      {(title || subtitle) && (
        <CardHeader
          title={title}
          subheader={subtitle}
          sx={{
            '& .MuiCardHeader-title': {
              fontWeight: 600,
              fontSize: '1.25rem',
            },
          }}
        />
      )}
      <CardContent>{children}</CardContent>
      {actions && <CardActions sx={{ px: 2, pb: 2 }}>{actions}</CardActions>}
    </StyledCard>
  )
}

export default CustomCard


