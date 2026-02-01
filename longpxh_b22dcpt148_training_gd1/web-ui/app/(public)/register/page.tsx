"use client";

import {Button, Card, Container, TextField, Typography, Alert} from "@mui/material";
import Link from "next/link";
import { useState } from "react";
import { api } from "../../../lib/api";

export default function Register(){
    const [formData, setFormData] = useState({
        fullName: '',
        username: '',
        password: '',
        email: ''
    });
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleRegister = async () => {
        setError(null);
        setLoading(true);
        try {
            // Update to use /api/auth/register to match backend controller
            await api('/api/auth/register', {
                method: 'POST',
                body: JSON.stringify(formData)
            });
            // Success
            alert("Đăng ký thành công! Vui lòng đăng nhập.");
            window.location.href = '/login';
        } catch (err: any) {
            console.error(err);
            setError(err.message || "Đăng ký thất bại. Vui lòng thử lại.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container maxWidth="xs" sx={{ mt: 10 }}>
            <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Typography variant="h5" align="center" fontWeight="bold">Đăng ký</Typography>

                {error && <Alert severity="error">{error}</Alert>}

                <TextField 
                    label="Họ và tên" 
                    name="fullName"
                    value={formData.fullName}
                    onChange={handleChange}
                    fullWidth 
                />
                <TextField 
                    label="Tên đăng nhập" 
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    fullWidth 
                />
                <TextField 
                    label="Mật khẩu" 
                    type="password" 
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    fullWidth 
                />
                <TextField 
                    label="Email" 
                    type="email" 
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    fullWidth 
                />

                <Button 
                    variant="contained" 
                    fullWidth 
                    onClick={handleRegister}
                    disabled={loading}
                >
                    {loading ? "Đang xử lý..." : "Đăng ký"}
                </Button>

                <Typography align="center" variant="body2">
                    Đã có tài khoản <Link href="/login" style={{color: '#1976d2'}}>Đăng nhập</Link>
                </Typography>

            </Card>
        </Container>
    );
}
