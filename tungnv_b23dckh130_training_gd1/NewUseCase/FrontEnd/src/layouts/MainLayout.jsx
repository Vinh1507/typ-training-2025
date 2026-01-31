import Navbar from "../components/Navbar.jsx";
import { Outlet, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

function MainLayout() {
    const location = useLocation();
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        // 1. Mỗi khi đổi đường dẫn -> Bật chế độ Loading
        setIsLoading(true);
        window.scrollTo({ top: 0, behavior: 'smooth' }); // Cuộn lên đầu

        const timer = setTimeout(() => {
            setIsLoading(false);
        }, 800);

        return () => clearTimeout(timer);
    }, [location.pathname]);
    return (
        <div className="min-h-screen bg-[#F5F7FA] font-sans">
            <Navbar />

            <main className="relative min-h-[calc(100vh-80px)]">
                {isLoading ? (
                    <div className="absolute inset-0 flex flex-col items-center justify-center bg-white/80 backdrop-blur-sm z-50">
                        <div className="relative">
                            <div className="h-16 w-16 border-4 border-blue-200 rounded-full animate-spin"></div>
                            <div className="absolute top-0 left-0 h-16 w-16 border-4 border-blue-600 rounded-full animate-spin border-t-transparent"></div>
                        </div>
                        <p className="mt-4 text-blue-800 font-bold animate-pulse text-sm">
                            Đang tải dữ liệu...
                        </p>
                    </div>
                ) : (
                    <div>
                        <Outlet />
                    </div>
                )}
            </main>
        </div>
    )
}

export default MainLayout;