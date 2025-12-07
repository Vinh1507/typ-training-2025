'use client';

import { createContext, useContext, useEffect, useState } from 'react';
import { api } from '@/lib/api';
import Cookies from 'js-cookie';
import { useRouter } from 'next/navigation';

interface Role {
    id: number;
    name: string;
    description: string;
}

interface UserRole {
    id: number;
    role: Role;
}

interface User {
    id: string;
    username: string;
    email: string;
    fullName: string;
    userRoles: UserRole[];
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
                // Gọi API lấy thông tin user (Sửa đường dẫn này theo API thật của bạn)
                // Gọi API lấy thông tin user (Sửa đường dẫn này theo API thật của bạn)
                const response = await api<{ data: User }>('/users/my-info');
                setUser(response.data);
            } catch (error) {
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
        // Sau khi set cookie, gọi lại checkAuth hoặc redirect luôn
        // Ở đây mình reload nhẹ hoặc fetch lại user
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