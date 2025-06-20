import axios from 'axios';
import { toast } from 'react-hot-toast';

const API_BASE_URL = 'http://localhost:9000';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000,
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('authToken');
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

apiClient.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response.status === 400)
            toast.error(error.response.data.message || 'Validation error.');

        if (error.response.status === 401) {
            localStorage.removeItem('authToken');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userInfo');
            toast.error(error.response.data.message || 'Session expired. Please login again.');

            if (window.location.pathname !== '/login')
                window.location.href = '/login';
        }

        if (error.response.status === 400)
            toast.error(error.response.data.message || 'Validation error.');

        if (error.response.status === 403)
            toast.error(error.response.data.message || 'You are not authorized to perform this action.');

        if (error.response.status === 404)
            toast.error(error.response.data.message || 'Resource not found.');

        if (error.response.status === 409)
            toast.error(error.response.data.message || 'Resource already exists.');

        if (error.response.status === 500)
            toast.error(error.response.data.message || 'Something went wrong. Please try again.');

        return Promise.reject(error);
    }
);

export default apiClient;