/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";

const userTypeService = {
    getAllUserTypes: async () => {
        try {
            const response = await apiClient.get('/user-types');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUserTypeById: async (id) => {
        try {
            const response = await apiClient.get(`/user-types/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUserTypeByName: async (name) => {
        try {
            const response = await apiClient.get(`/user-types/name/${name}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createUserType: async (userTypeData) => {
        try {
            const response = await apiClient.post('/user-types', userTypeData);
            toast.success('User type created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateUserType: async (id, userTypeData) => {
        try {
            const response = await apiClient.put(`/user-types/${id}`, userTypeData);
            toast.success('User type updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteUserType: async (id) => {
        try {
            const response = await apiClient.delete(`/user-types/${id}`);
            toast.success('User type deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default userTypeService;