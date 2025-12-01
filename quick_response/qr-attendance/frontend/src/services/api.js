import axios from 'axios';
const API_BASE_URL = process.env.REACT_APP_API_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: false
});

export const qrAPI = {
    getQRCode: async () => {
        try {
        const response = await api.get('/qr');
        return response.data;
        } catch (error) {
        console.error('Error fetching QR code:', error);
        throw error;
        }
    },

    generateQRCode: async () => {
        try {
        const response = await api.post('/qr/generate');
        return response.data;
        } catch (error) {
        console.error('Error generating QR code:', error);
        throw error;
        }
    },

    getLogs: async (limit = 10) => {
        try {
        const response = await api.get('/logs', {
            params: { limit }
        });
        return response.data;
        } catch (error) {
        console.error('Error fetching logs:', error);
        throw error;
        }
    },

    checkIn: async (token) => {
        try {
        const response = await api.post('/checkin', { token });
        return response.data;
        } catch (error) {
        console.error('Error checking in:', error);
        throw error;
        }
    },

    getStats: async () => {
        try {
        const response = await api.get('/stats');
        return response.data;
        } catch (error) {
            console.error('Error fetching stats:', error);
            return {
                total: 0,
                today: 0,
                qrValid: false
            };
        }
    }
};

api.interceptors.request.use(
    (config) => {
        // I can add auth token here if needed
        // config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
        console.error('API Error:', error.response.status, error.response.data);
        
        switch (error.response.status) {
            case 400:
            console.error('Bad Request:', error.response.data);
            break;
            case 401:
            console.error('Unauthorized');
            break;
            case 404:
            console.error('Not Found');
            break;
            case 500:
            console.error('Server Error');
            break;
            default:
            console.error('Unknown Error');
        }
        } else if (error.request) {
        console.error('Network Error - No response received');
        } else {
        console.error('Error:', error.message);
        }
        return Promise.reject(error);
    }
);

export default api;