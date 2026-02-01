import type { Metadata } from "next";
import "./global.css";
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";
import MuiProvider from "./MuiProvider";
import { AuthProvider } from "../context/AuthContext";

export const metadata: Metadata = {
    title: "Online judge C++",
    description: "Mô tả app",
};

export default function RootLayout({ children, }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body className="flex min-h-screen flex-col">
                <MuiProvider>
                    <AuthProvider>
                        <Header />
                        <main className="flex-grow">
                            {children}
                        </main>
                        <Footer />
                    </AuthProvider>
                </MuiProvider>
            </body>
        </html>
    );
}