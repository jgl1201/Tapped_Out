/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-hot-toast";

const sportService = {
    getAllSports: async () => {
        try {
            const response = await apiClient.get('/sport');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getSportById: async (id) => {
        try {
            const response = await apiClient.get(`/sport/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getSportByName: async (name) => {
        try {
            const response = await apiClient.get(`/sport/name/${name}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createSport: async (sportData) => {
        try {
            const response = await apiClient.post('/sport', sportData);
            toast.success('Sport created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateSport: async (id, sportData) => {
        try {
            const response = await apiClient.put(`/sport/${id}`, sportData);
            toast.success('Sport updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteSport: async (id) => {
        try {
            const response = await apiClient.delete(`/sport/${id}`);
            toast.success('Sport deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default sportService;