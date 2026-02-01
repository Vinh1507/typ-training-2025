'use client';

import { createContext, useContext, useEffect, useState } from 'react';
import { api } from '../lib/api';
import Cookies from 'js-cookie';
import { useRouter } from 'next/navigation';

export interface User {
    id: number;
    username: string;
    email: string;
    fullName: string;
    roles: string[];
}

interface AuthContextType {
    user: User | null;
    isLoading: boolean;
    login: (accessToken: string, refreshToken: string) => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
    user: null,
    isLoading: true,
    login: () => { },
    logout: () => { },
});

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const router = useRouter();

    // Hàm check user khi F5 trang
    useEffect(() => {
        const checkAuth = async () => {
            const token = Cookies.get('accessToken');
            if (!token) {
                setIsLoading(false);
                return;
            }

            try {
                // Using correct path /api/users/my-info
                const userData = await api<User>('/api/users/my-info');
                setUser(userData);
            } catch (error) {
                console.error("Auth check failed:", error);
                // Token lỗi hoặc hết hạn -> Xóa luôn
                Cookies.remove('accessToken');
                Cookies.remove('refreshToken');
                setUser(null);
            } finally {
                setIsLoading(false);
            }
        };

        checkAuth();
    }, []);

    const login = (accessToken: string, refreshToken: string) => {
        Cookies.set('accessToken', accessToken, { expires: 1 }); // 1 ngày
        Cookies.set('refreshToken', refreshToken, { expires: 7 }); // 7 ngày

        // Reload to trigger checkAuth or redirect
        window.location.href = '/problems';
    };

    const logout = () => {
        Cookies.remove('accessToken');
        Cookies.remove('refreshToken');
        setUser(null);
        router.push('/login');
    };

    return (
        <AuthContext.Provider value={{ user, isLoading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

// Hook để dùng nhanh ở các component khác
export const useAuth = () => useContext(AuthContext);
