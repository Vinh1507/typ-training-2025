import { useNavigate } from 'react-router-dom'
import { Box, Button } from '@mui/material'
import { styled } from '@mui/material/styles'
import logoImage from '../assets/logo.png'
import vissmartImage from '../assets/Vissmart.png'
import backgroundImage from '../assets/nen.png'
import walletImage from '../assets/wallet.png'
import bagImage from '../assets/Bag.png'

// Styled components
const LandingContainer = styled(Box)({
  minHeight: '100vh',
  backgroundImage: `url(${backgroundImage})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
  backgroundRepeat: 'no-repeat',
  position: 'relative',
  overflow: 'hidden',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  padding: '2rem',
})

const LogoImage = styled('img')({
  width: '100px',
  height: 'auto',
  marginBottom: '2rem',
  position: 'relative',
  zIndex: 10,
  filter: 'drop-shadow(0 4px 8px rgba(0, 0, 0, 0.15))',
})

const VissmartImage = styled('img')({
  maxWidth: '600px',
  width: '80%',
  height: 'auto',
  marginBottom: '3rem',
  position: 'relative',
  zIndex: 10,
  filter: 'drop-shadow(0 4px 8px rgba(0, 0, 0, 0.15))',
  '@media (max-width: 600px)': {
    width: '90%',
  },
})

const ActionButton = styled(Button)({
  minWidth: 300,
  padding: '16px 48px',
  fontSize: '1.1rem',
  fontWeight: 600,
  borderRadius: '50px',
  textTransform: 'uppercase' as const,
  letterSpacing: '2px',
  boxShadow: '0 8px 24px rgba(0, 0, 0, 0.2)',
  transition: 'all 0.3s ease',
  position: 'relative',
  zIndex: 10,
  '@media (max-width: 600px)': {
    minWidth: 250,
    padding: '14px 40px',
  },
  '&:hover': {
    transform: 'translateY(-2px)',
    boxShadow: '0 12px 32px rgba(0, 0, 0, 0.25)',
  },
})

const DecorativeImage = styled('img')({
  position: 'absolute',
  maxWidth: '250px',
  opacity: 0.6,
  animation: 'float 3s ease-in-out infinite',
  zIndex: 1,
  '@media (max-width: 768px)': {
    maxWidth: '150px',
    opacity: 0.4,
  },
  '@keyframes float': {
    '0%, 100%': {
      transform: 'translateY(0px)',
    },
    '50%': {
      transform: 'translateY(-20px)',
    },
  },
})

const WalletIcon = styled(DecorativeImage)({
  bottom: '10%',
  left: '5%',
  '@media (max-width: 768px)': {
    left: '2%',
    bottom: '5%',
  },
})

const BagIcon = styled(DecorativeImage)({
  top: '15%',
  right: '5%',
  animationDelay: '1.5s',
  '@media (max-width: 768px)': {
    right: '2%',
    top: '10%',
  },
})

const LandingPage = () => {
  const navigate = useNavigate()

  const handleLogin = () => {
    navigate('/login')
  }

  const handleRegister = () => {
    navigate('/register')
  }

  return (
    <LandingContainer>
      {/* Logo */}
      <LogoImage src={logoImage} alt="Logo" />

      {/* Vissmart Title */}
      <VissmartImage src={vissmartImage} alt="Vissmart" />

      {/* Buttons */}
      <Box sx={{ 
        display: 'flex', 
        flexDirection: 'column', 
        gap: 2, 
        alignItems: 'center', 
        position: 'relative',
        zIndex: 10,
      }}>
        <ActionButton
          variant="contained"
          color="primary"
          onClick={handleLogin}
        >
          Đăng Nhập
        </ActionButton>

        <ActionButton
          variant="outlined"
          color="primary"
          onClick={handleRegister}
          sx={{
            backgroundColor: 'rgba(255, 255, 255, 0.9)',
            '&:hover': {
              backgroundColor: 'rgba(255, 255, 255, 1)',
            },
          }}
        >
          Đăng Ký
        </ActionButton>
      </Box>

      {/* Decorative images */}
      <WalletIcon src={walletImage} alt="Wallet" />
      <BagIcon src={bagImage} alt="Bag" />
    </LandingContainer>
  )
}

export default LandingPage
