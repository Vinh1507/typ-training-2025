'use client';

import { Box, Button, Card, CardContent, Container, TextField, Typography, Alert } from "@mui/material";
import Link from "next/link";
import { useState } from "react";
import { api } from "../../../lib/api";
import { useAuth } from "../../../context/AuthContext";

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();

    const handleLogin = async () => {
        try {
            setError('');

            const data = await api('/api/auth/login', {
                method: 'POST',
                body: JSON.stringify({ username, password })
            });

            const responseData = data.data || data;
            const accessToken = responseData.accessToken || responseData.token;
            const refreshToken = responseData.refreshToken;

            if (accessToken && refreshToken) {
                login(accessToken, refreshToken);
                window.location.href = '/';
            } else {
                setError('Không tìm thấy token trong phản hồi');
            }
        } catch (err: any) {
            setError(err.message || 'Đăng nhập thất bại');
        }
    };

    return (
        <Container maxWidth="xs" sx={{ mt: 10 }}>
            <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Typography variant="h5" align="center" fontWeight="bold">Đăng nhập</Typography>

                {error && <Alert severity="error">{error}</Alert>}

                <TextField
                    label="Tên đăng nhập"
                    fullWidth
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <TextField
                    label="Mật khẩu"
                    type="password"
                    fullWidth
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <Button
                    variant="contained"
                    fullWidth
                    onClick={handleLogin}
                >
                    Đăng nhập
                </Button>

                <Typography align="center" variant="body2">
                    Chưa có tài khoản? <Link href="/register" style={{ color: '#1976d2' }}>Đăng ký</Link>
                </Typography>
            </Card>
        </Container>
    );
}
