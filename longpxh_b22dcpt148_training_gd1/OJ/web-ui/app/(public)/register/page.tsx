import {Button, Card, Container, TextField, Typography} from "@mui/material";
import Link from "next/link";

export default function Register(){
    return (
        <Container maxWidth="xs" sx={{ mt: 10 }}>
            <Card sx={{ p: 4, display: 'flex', flexDirection: 'column', gap: 2 }}>
                <Typography variant="h5" align="center" fontWeight="bold">Đăng ký</Typography>

                <TextField label="Họ và tên"  fullWidth />
                <TextField label="Tên đăng nhập" fullWidth />
                <TextField label="Mật khẩu" type="password" fullWidth />
                <TextField label="Email" type="email" fullWidth />


                <Button variant="contained" fullWidth>Đăng ký</Button>
                <Typography align="center" variant="body2">
                    Đã có tài khoản <Link href="/login" style={{color: '#1976d2'}}>Đăng nhập</Link>
                </Typography>

            </Card>
        </Container>
    );
}