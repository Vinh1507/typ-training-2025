'use client'

import { useState } from 'react';
import { AppBar, Box, Button, Toolbar, Typography, IconButton, Menu, MenuItem, Container, Divider } from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useAuth } from "@/context/AuthContext";

const MENU = [
    { label: "Danh sách bài tập", href: "/problems" },
    { label: "Xếp hạng", href: "/rankings" },
    { label: "Hướng dẫn", href: "/tutorial" }
];

export default function Header() {
    const pathname = usePathname();
    const { user, logout } = useAuth();

    // State cho Mobile Menu
    const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null);
    // State cho User Menu
    const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null);

    // Xử lý Mobile Menu
    const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
        setAnchorElNav(null);
    };

    // Xử lý User Menu
    const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorElUser(event.currentTarget);
    };

    const handleCloseUserMenu = () => {
        setAnchorElUser(null);
    };

    const handleLogout = () => {
        handleCloseUserMenu();
        logout();
    };

    return (
        <AppBar position="sticky" className="mb-3">
            <Container maxWidth="xl">
                <Toolbar disableGutters>

                    {/*  LOGO */}
                    <Typography
                        variant="h6"
                        noWrap
                        component={Link}
                        href="/"
                        sx={{
                            mr: 2,
                            display: { xs: 'none', md: 'flex' }, // Ẩn trên Mobile
                            fontWeight: 700,
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        OJ CPP
                    </Typography>

                    {/* MENU LINKS (Desktop) */}
                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, gap: 2 }}>
                        {MENU.map((item) => {
                            const isActive = pathname === item.href;
                            return (
                                <Button
                                    key={item.href}
                                    component={Link}
                                    href={item.href}
                                    onClick={handleCloseNavMenu}
                                    sx={{
                                        my: 2,
                                        color: 'white',
                                        display: 'block',
                                        backgroundColor: isActive ? 'rgba(255, 255, 255, 0.2)' : 'transparent',
                                        fontWeight: isActive ? 'bold' : 'normal'
                                    }}
                                >
                                    {item.label}
                                </Button>
                            );
                        })}
                    </Box>

                    {/*  HAMBURGER MENU */}
                    <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
                        <IconButton
                            size="large"
                            aria-label="menu"
                            aria-controls="menu-appbar"
                            aria-haspopup="true"
                            onClick={handleOpenNavMenu}
                            color="inherit"
                        >
                            <MenuIcon />
                        </IconButton>
                        <Menu
                            id="menu-appbar"
                            anchorEl={anchorElNav}
                            anchorOrigin={{
                                vertical: 'bottom',
                                horizontal: 'left',
                            }}
                            keepMounted
                            transformOrigin={{
                                vertical: 'top',
                                horizontal: 'left',
                            }}
                            open={Boolean(anchorElNav)}
                            onClose={handleCloseNavMenu}
                            sx={{
                                display: { xs: 'block', md: 'none' },
                            }}
                        >
                            {MENU.map((item) => (
                                <MenuItem key={item.href} onClick={handleCloseNavMenu} component={Link} href={item.href}>
                                    <Typography textAlign="center">{item.label}</Typography>
                                </MenuItem>
                            ))}
                        </Menu>
                    </Box>

                    {/* LOGO (Mobile) */}
                    <Typography
                        variant="h6"
                        noWrap
                        component={Link}
                        href="/"
                        sx={{
                            mr: 2,
                            display: { xs: 'flex', md: 'none' }, // Chỉ hiện trên Mobile
                            flexGrow: 1,
                            fontWeight: 700,
                            color: 'inherit',
                            textDecoration: 'none',
                        }}
                    >
                        OJ CPP
                    </Typography>

                    {/* USER MENU / LOGIN BUTTON */}
                    <Box sx={{ flexGrow: 0 }}>
                        {user ? (
                            <>
                                <Button
                                    onClick={handleOpenUserMenu}
                                    sx={{ color: 'white', textTransform: 'none' }}
                                >
                                    <Typography>
                                        Xin chào, {user.fullName || user.username}
                                    </Typography>
                                </Button>
                                <Menu
                                    sx={{ mt: '45px' }}
                                    id="menu-appbar"
                                    anchorEl={anchorElUser}
                                    anchorOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    keepMounted
                                    transformOrigin={{
                                        vertical: 'top',
                                        horizontal: 'right',
                                    }}
                                    open={Boolean(anchorElUser)}
                                    onClose={handleCloseUserMenu}
                                >
                                    <MenuItem component={Link} href="/profile" onClick={handleCloseUserMenu}>
                                        <Typography textAlign="center">Hồ sơ cá nhân</Typography>
                                    </MenuItem>

                                    {/* Admin Menu Item */}
                                    {user.userRoles?.some(ur => ur.role.name === 'ROLE_ADMIN') && (
                                        <MenuItem component={Link} href="/admin" onClick={handleCloseUserMenu}>
                                            <Typography textAlign="center">Quản lý hệ thống</Typography>
                                        </MenuItem>
                                    )}

                                    <Divider />

                                    <MenuItem onClick={handleLogout}>
                                        <Typography textAlign="center" color="error">Đăng xuất</Typography>
                                    </MenuItem>
                                </Menu>
                            </>
                        ) : (
                            <Button
                                color="inherit"
                                component={Link}
                                sx={{
                                    backgroundColor: pathname === '/login' ? 'rgba(255, 255, 255, 0.2)' : 'transparent',
                                    fontWeight: pathname === '/login' ? 'bold' : 'normal'
                                }}
                                href="/login"
                            >
                                Đăng nhập
                            </Button>
                        )}
                    </Box>
                </Toolbar>
            </Container>
        </AppBar>
    );
}
