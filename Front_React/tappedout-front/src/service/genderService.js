/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";

const genderService = {
    getAllGenders: async () => {
        try {
            const response = await apiClient.get('/gender');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getGenderById: async (id) => {
        try {
            const response = await apiClient.get(`/gender/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getGenderByName: async (name) => {
        try {
            const response = await apiClient.get(`/gender/name/${name}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createGender: async(genderData) => {
        try {
            const response = await apiClient.post('/gender', genderData);
            toast.success('Gender created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateGender: async(id, genderData) => {
        try {
            const response = await apiClient.put(`/gender/${id}`, genderData);
            toast.success('Gender updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteGender: async(id) => {
        try {
            const response = await apiClient.delete(`/gender/${id}`);
            toast.success('Gender deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default genderService;