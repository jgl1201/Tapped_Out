/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-hot-toast";

const sportLevelService = {
    getAllSportLevels: async () => {
        try {
            const response = await apiClient.get('/sport-level');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getSportLevelsBySport: async (sportId) => {
        try {
            const response = await apiClient.get(`/sport-level/sport/${sportId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getSportLevelById: async (id) => {
        try {
            const response = await apiClient.get(`/sport-level/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getSportLevelBySportAndName: async (sportId, name) => {
        try {
            const response = await apiClient.get(`/sport-level/sport/${sportId}/name/${name}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createSportLevel: async (sportLevelData) => {
        try {
            const response = await apiClient.post('/sport-level', sportLevelData);
            toast.success('Sport level created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateSportLevel: async (id, sportLevelData) => {
        try {
            const response = await apiClient.put(`/sport-level/${id}`, sportLevelData);
            toast.success('Sport level updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteSportLevel: async (id) => {
        try {
            const response = await apiClient.delete(`/sport-level/${id}`);
            toast.success('Sport level deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default sportLevelService;