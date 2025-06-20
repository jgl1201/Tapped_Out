/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";

export const authService = {
    login: async (credentials) => {
        try {
            const response = await apiClient.post('/auth/login', credentials);
            const { token, tokenType, userId, email, firstName, lastName, userType } = response.data;
            
            localStorage.setItem('authToken', token);
            localStorage.setItem('tokenType', tokenType);
            localStorage.setItem('userRole', userType);
            localStorage.setItem('userInfo', JSON.stringify({
                userId,
                email,
                firstName,
                lastName,
                userType
            }));
            
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    register: async (credentials) => {
        try {
            const payload = {
                ...credentials,
                userType: credentials.userType || 'COMPETITOR'
            };

            const response = await apiClient.post('/auth/register', payload);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    validateToken: async () => {
        try {
            const response = await apiClient.get('/auth/validate');
            return response.data;
        } catch (error) {
            localStorage.removeItem('authToken');
            localStorage.removeItem('tokenType');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userInfo');

            throw error;
        }
    },

    logout: () => {
        localStorage.removeItem('authToken');
        localStorage.removeItem('tokenType');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userInfo');
    },

    getCurrentUser: () => {
        const userInfo = localStorage.getItem('userInfo');
        return userInfo ? JSON.parse(userInfo) : null;
    },

    getCurrentUserRole: () => {
        return localStorage.getItem('userRole');
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('authToken');
    },

    hasRole: (role) => {
        const userRole = localStorage.getItem('userRole');
        return userRole === role;
    },

    hasAnyRole: (roles) => {
        const userRole = localStorage.getItem('userRole');
        return roles.includes(userRole);
    },

    isAdmin: () => {
        return authService.hasRole('ADMIN');
    },

    isOrganizer: () => {
        return authService.hasRole('ORGANIZER');
    },

    isCompetitor: () => {
        return authService.hasRole('COMPETITOR');
    },
};