/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-hot-toast";

const resultService = {
    getAllResults: async () => {
        try {
            const response = await apiClient.get('/result');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultsByEvent: async (eventId) => {
        try {
            const response = await apiClient.get(`/result/event/${eventId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultsByEventAndCategory: async (eventId, categoryId) => {
        try {
            const response = await apiClient.get(`/result/event/${eventId}/category/${categoryId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultsByCompetitorAndEvent: async (competitorId, eventId) => {
        try {
            const response = await apiClient.get(`/result/competitor/${competitorId}/event/${eventId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultsByCompetitor: async (competitorId) => {
        try {
            const response = await apiClient.get(`/result/competitor/${competitorId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultsByEventAndPosition: async (eventId, position) => {
        try {
            const response = await apiClient.get(`/result/event/${eventId}/position/${position}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getResultById: async (id) => {
        try {
            const response = await apiClient.get(`/result/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getWinnerByEventAndCategory: async (eventId, categoryId) => {
        try {
            const response = await apiClient.get(`/result/event/${eventId}/category/${categoryId}/winners`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createResult: async (resultData) => {
        try {
            const response = await apiClient.post('/result', resultData);
            toast.success('Result created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateResult: async (id, resultData) => {
        try {
            const response = await apiClient.put(`/result/${id}`, resultData);
            toast.success('Result updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteResult: async (id) => {
        try {
            const response = await apiClient.delete(`/result/${id}`);
            toast.success('Result deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default resultService;