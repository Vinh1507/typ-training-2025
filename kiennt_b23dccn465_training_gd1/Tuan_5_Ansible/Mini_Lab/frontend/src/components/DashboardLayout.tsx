import { ReactNode, useState, useEffect } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import {
  Box,
  Drawer,
  AppBar,
  Toolbar,
  List,
  Typography,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Avatar,
  IconButton,
  useMediaQuery,
  useTheme,
} from '@mui/material'
import {
  Home,
  AccountBalance,
  SwapHoriz,
  Savings,
  Analytics,
  Assessment,
  Settings,
  ExitToApp,
  Menu as MenuIcon,
} from '@mui/icons-material'
import { styled } from '@mui/material/styles'
import logoImage from '../assets/logo.png'
import vissmartImage from '../assets/Vissmart2.png'
import backgroundImage from '../assets/nen.png'
import ChatWidget from './ChatWidget'

const drawerWidth = 280

interface DashboardLayoutProps {
  children: ReactNode
}

const StyledDrawer = styled(Drawer)(({ theme }) => ({
  '& .MuiDrawer-paper': {
    width: drawerWidth,
    boxSizing: 'border-box',
    backgroundColor: theme.palette.primary.main,
    color: 'white',
    border: 'none',
  },
}))

const LogoContainer = styled(Box)(({ theme }) => ({
  padding: theme.spacing(3),
  display: 'flex',
  alignItems: 'center',
  gap: theme.spacing(2),
  borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
  marginBottom: theme.spacing(1),
}))

const LogoBox = styled(Box)(({ theme }) => ({
  width: 40,
  height: 40,
  backgroundColor: 'rgba(255, 255, 255, 0.2)',
  borderRadius: '8px',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  fontSize: '12px',
  fontWeight: 'bold',
}))

const StyledListItemButton = styled(ListItemButton)(({ theme }) => ({
  borderRadius: '8px',
  margin: '4px 16px',
  color: 'rgba(255, 255, 255, 0.8)',
  '&:hover': {
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    color: 'white',
  },
  '&.Mui-selected': {
    backgroundColor: 'rgba(255, 255, 255, 0.15)',
    color: 'white',
    '&:hover': {
      backgroundColor: 'rgba(255, 255, 255, 0.2)',
    },
  },
}))

const UserSection = styled(Box)(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  gap: theme.spacing(2),
  color: 'white',
}))

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))
  const navigate = useNavigate()
  const location = useLocation()
  const [mobileOpen, setMobileOpen] = useState(false)

  // 1. Thêm State để lưu trữ tên người dùng
  const [userName, setUserName] = useState("User"); // Giá trị mặc định là "User"

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen)
  }

  const handleListItemClick = (path: string) => {
    navigate(path)
    if (isMobile) {
      setMobileOpen(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('userFullName')
    localStorage.removeItem('userEmail')
    navigate('/login')
  }

  // 2. CẬP NHẬT LOGIC: Dùng useEffect để đọc tên người dùng từ localStorage
  useEffect(() => {
    // Đọc tên người dùng từ localStorage (được lưu sau khi đăng ký/đăng nhập)
    const storedFullName = localStorage.getItem('userFullName');

    if (storedFullName) {
        // Cập nhật state nếu tìm thấy tên
        setUserName(storedFullName);
    }
    // Nếu không tìm thấy, nó sẽ giữ giá trị mặc định là "User"
  }, []);


  const menuItems = [
    { text: 'Trang chủ', icon: <Home />, path: '/dashboard' },
    { text: 'Quản lý tài khoản', icon: <AccountBalance />, path: '/accounts' },
    { text: 'Quản lý giao dịch', icon: <SwapHoriz />, path: '/transactions' },
    { text: 'Quản lý ngân sách', icon: <Savings />, path: '/budgets' },
    { text: 'Phân tích chi tiêu', icon: <Analytics />, path: '/analysis' },
    { text: 'Báo cáo', icon: <Assessment />, path: '/reports' },
    { text: 'Cài đặt', icon: <Settings />, path: '/settings' },
  ]

  const drawer = (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      {/* Logo Section */}
      <LogoContainer>
        <Box
          component="img"
          src={logoImage}
          alt="Logo"
          sx={{
            width: 50,
            height: 50,
            objectFit: 'contain',
          }}
        />
        <Box
          component="img"
          src={vissmartImage}
          alt="Vissmart"
          sx={{
            height: 70,
            objectFit: 'contain',
          }}
        />
      </LogoContainer>

      {/* Navigation Menu */}
      <List sx={{ flex: 1, px: 1 }}>
        {menuItems.map((item) => (
          <ListItem key={item.text} disablePadding>
            <StyledListItemButton
              selected={location.pathname === item.path}
              onClick={() => handleListItemClick(item.path)}
            >
              <ListItemIcon sx={{ color: 'inherit', minWidth: 40 }}>
                {item.icon}
              </ListItemIcon>
              <ListItemText
                primary={item.text}
                primaryTypographyProps={{ fontSize: '0.95rem' }}
              />
            </StyledListItemButton>
          </ListItem>
        ))}
      </List>

      {/* Logout Button */}
      <Box sx={{ p: 2 }}>
        <StyledListItemButton onClick={handleLogout}>
          <ListItemIcon sx={{ color: 'inherit', minWidth: 40 }}>
            <ExitToApp />
          </ListItemIcon>
          <ListItemText primary="Đăng xuất" />
        </StyledListItemButton>
      </Box>
    </Box>
  )

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh' }}>
      {/* App Bar */}
      <AppBar
        position="fixed"
        sx={{
          width: { md: `calc(100% - ${drawerWidth}px)` },
          ml: { md: `${drawerWidth}px` },
          backgroundColor: '#6B8E7F',
          color: 'white',
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
        }}
      >
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2, display: { md: 'none' } }}
          >
            <MenuIcon />
          </IconButton>

          <Box sx={{ flex: 1 }} />

          <UserSection>
            {/* 3. Hiển thị userName từ state */}
            <Typography variant="body1" sx={{ color: 'white', fontStyle: 'italic' }}>
              Xin chào, <strong>{userName}    </strong>
            </Typography>
            <Avatar
              sx={{
                bgcolor: 'rgba(255, 255, 255, 0.3)',
                color: 'white',
                width: 40,
                height: 40,
              }}
            >
              {/* Lấy chữ cái đầu tiên của userName */}
              {userName.charAt(0).toUpperCase()}
            </Avatar>
          </UserSection>
        </Toolbar>
      </AppBar>

      {/* Sidebar */}
      <Box
        component="nav"
        sx={{ width: { md: drawerWidth }, flexShrink: { md: 0 } }}
      >
        <StyledDrawer
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{
            keepMounted: true,
          }}
          sx={{
            display: { xs: 'block', md: 'none' },
          }}
        >
          {drawer}
        </StyledDrawer>
        <StyledDrawer
          variant="permanent"
          sx={{
            display: { xs: 'none', md: 'block' },
          }}
          open
        >
          {drawer}
        </StyledDrawer>
      </Box>

      {/* Main content */}
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          width: { md: `calc(100% - ${drawerWidth}px)` },
          backgroundImage: `url(${backgroundImage})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          backgroundRepeat: 'no-repeat',
          backgroundAttachment: 'fixed',
          minHeight: '100vh',
        }}
      >
        <Toolbar />
        <Box sx={{ p: { xs: 2, md: 3 } }}>
          {children}
        </Box>
      </Box>

      {/* Floating Chat Widget */}
      <ChatWidget />
    </Box>
  )
}

export default DashboardLayout
