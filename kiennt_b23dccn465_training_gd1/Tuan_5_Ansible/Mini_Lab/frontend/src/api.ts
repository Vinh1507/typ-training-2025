import axios from "axios";

const apiBaseURL =
    import.meta.env.VITE_API_BASE_URL ??
    (import.meta.env.DEV ? "http://localhost:8080/api" : "/api");

const api = axios.create({
    baseURL: apiBaseURL,
    headers: {
        "Content-Type": "application/json",
    },
});

// ==========================
// INTERCEPTOR CHUẨN
// ==========================
api.interceptors.request.use(
    (config) => {
        // Không gửi token với các API auth
        if (!config.url?.includes("/auth/")) {
            const token = localStorage.getItem("token");
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }

        return config;
    },
    (error) => Promise.reject(error)
);

export default api;
