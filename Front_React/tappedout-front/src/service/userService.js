/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-toastify";

const userService = {
    getAllUsers: async () => {
        try {
            const response = await apiClient.get('/user');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUsersByType: async (userTypeId) => {
        try {
            const response = await apiClient.get(`/user/type/${userTypeId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUsersByGender: async (genderId) => {
        try {
            const response = await apiClient.get(`/user/gender/${genderId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUsersByLocation: async (country, city) => {
        try {
            const response = await apiClient.get('/user/location/', {
                params: {country, city}
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    searchUsers: async (query) => {
        try {
            const response = await apiClient.get('/user/search', {
                params: {query}
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUserById: async (id) => {
        try {
            const response = await apiClient.get(`/user/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUserByDni: async (dni) => {
        try {
            const response = await apiClient.get(`/user/dni/${dni}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUserByEmail: async (email) => {
        try {
            const response = await apiClient.get(`/user/email/${email}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createUser: async (userData) => {
        try {
            const response = await apiClient.post('/user', userData);
            toast.success('User created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateUser: async (id, userData) => {
        try {
            const response = await apiClient.put(`/user/${id}`, userData);
            toast.success('User updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateUserSecurity: async (id, userSecurityData) => {
        try {
            const response = await apiClient.patch(`/user/${id}/security`, userSecurityData);
            toast.success('User security information updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteUser: async (id) => {
        try {
            console.log('ID TO DELETE:', id);
            const response = await apiClient.delete(`/user/${id}`);
            toast.success('User deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default userService;