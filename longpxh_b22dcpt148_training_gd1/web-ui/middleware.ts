import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

const protectedRoutes = ['/problems', '/profile', '/submissions', '/rankings'];
const authRoutes = ['/login', '/register'];

export function middleware(request: NextRequest) {
    const token = request.cookies.get('accessToken')?.value;
    const { pathname } = request.nextUrl;

    // 1. Nếu chưa login mà vào trang bảo vệ -> Đá về Login
    if (!token && protectedRoutes.some((route) => pathname.startsWith(route))) {
        const loginUrl = new URL('/login', request.url);
        loginUrl.searchParams.set('from', pathname);
        return NextResponse.redirect(loginUrl);
    }

    // 2. Nếu đã login mà vào trang Login/Register -> Đá về trang chủ (hoặc problems)
    if (token && authRoutes.some((route) => pathname.startsWith(route))) {
        return NextResponse.redirect(new URL('/problems', request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};