import Cookies from 'js-cookie';

const BASE_URL = process.env.NEXT_PUBLIC_API_URL || '';

type RequestOptions = RequestInit & {
    headers?: Record<string, string>;
};

// Biến để track trạng thái refresh token
let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (error: any, token: string | null = null) => {
    failedQueue.forEach(prom => {
        if (error) {
            prom.reject(error);
        } else {
            prom.resolve(token);
        }
    });

    failedQueue = [];
};

export const api = async <T = any>(path: string, options: RequestOptions = {}): Promise<T> => {
    // 1. Lấy token từ Cookie
    let token = Cookies.get('accessToken');

    // 2. Chuẩn bị Header
    // 2. Chuẩn bị Header
    const headers: Record<string, string> = {
        ...(options.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
        ...(options.headers as Record<string, string>),
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    // 3. Gọi API
    console.log(`[API] Calling ${path}, Token: ${token ? 'Yes' : 'No'}`);
    const response = await fetch(`${BASE_URL}${path}`, {
        ...options,
        headers,
    });

    console.log(`[API] ${path} returned ${response.status}`);

    // 4. Xử lý lỗi 401 (Hết hạn token)
    if (response.status === 401) {
        console.log('[API] 401 Detected. Checking Refresh Token...');
        const refreshToken = Cookies.get('refreshToken');

        if (!refreshToken) {
            console.log('[API] No Refresh Token found. Logging out.');
            // Không có refresh token -> Logout luôn
            Cookies.remove('accessToken');
            Cookies.remove('refreshToken');
            if (typeof window !== 'undefined') {
                window.location.href = '/login';
            }
            throw new Error('Unauthorized');
        }

        if (isRefreshing) {
            console.log('[API] Refresh already in progress. Queuing request.');
            // Nếu đang refresh thì add vào queue đợi
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject });
            }).then(() => {
                // Gọi lại request cũ sau khi refresh thành công
                return api(path, options);
            }).catch(err => {
                return Promise.reject(err);
            });
        }

        isRefreshing = true;
        console.log('[API] Starting Refresh Token flow...');

        try {
            // Gọi API refresh token
            const refreshResponse = await fetch(`${BASE_URL}/auth/refresh`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ refreshToken }),
            });

            console.log(`[API] Refresh endpoint returned ${refreshResponse.status}`);

            if (!refreshResponse.ok) {
                throw new Error('Refresh failed');
            }

            const refreshData = await refreshResponse.json();
            console.log('[API] Refresh successful. New tokens received.');

            // Backend trả về: { success: true, data: { accessToken: "...", refreshToken: "..." } }
            const newAccessToken = refreshData.data?.accessToken || refreshData.accessToken;
            const newRefreshToken = refreshData.data?.refreshToken || refreshData.refreshToken;

            if (!newAccessToken) {
                throw new Error('No access token returned');
            }

            Cookies.set('accessToken', newAccessToken, { expires: 1 });
            if (newRefreshToken) {
                Cookies.set('refreshToken', newRefreshToken, { expires: 7 });
            }

            processQueue(null, newAccessToken);

            // Gọi lại request ban đầu
            return api(path, options);

        } catch (error) {
            console.error('[API] Refresh failed:', error);
            processQueue(error, null);
            // Logout
            Cookies.remove('accessToken');
            Cookies.remove('refreshToken');
            if (typeof window !== 'undefined') {
                window.location.href = '/login';
            }
            throw new Error('Session expired');
        } finally {
            isRefreshing = false;
        }
    }

    // 5. Trả về dữ liệu
    if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || 'API Error');
    }

    return response.json();
};
