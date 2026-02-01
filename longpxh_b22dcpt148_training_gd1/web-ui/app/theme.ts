'use client';
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    cssVariables: true,
    palette: {
        primary: {
            main: '#1976d2', // Màu xanh mặc định của MUI, bạn có thể đổi sau
        },
        secondary: {
            main: '#dc004e',
        },
    },
    typography: {
        fontFamily: 'var(--font-roboto)', // Sẽ config font sau
    },
});

export default theme;
