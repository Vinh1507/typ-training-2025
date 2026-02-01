import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    transpilePackages: [
        '@mui/material',
        '@mui/system',
        '@mui/icons-material',
        '@toolpad/core'
    ],
    async rewrites() {
        return [
            {
                source: '/api/:path*',
                destination: 'http://127.0.0.1:18080/api/:path*',
            },
        ];
    },
};

export default nextConfig;
