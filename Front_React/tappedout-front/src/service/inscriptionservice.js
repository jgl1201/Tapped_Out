/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-hot-toast";

const inscriptionService = {
    getAllInscriptions: async () => {
        try {
            const response = await apiClient.get('/inscription');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionsByCompetitor: async (competitorId) => {
        try {
            const response = await apiClient.get(`/inscription/competitor/${competitorId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionsByEvent: async (eventId) => {
        try {
            const response = await apiClient.get(`/inscription/event/${eventId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionsByCategory: async (categoryId) => {
        try {
            const response = await apiClient.get(`/inscription/category/${categoryId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionsByPaymentStatus: async (status) => {
        try {
            const response = await apiClient.get(`/inscription/status/${status}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getPaidInscriptionsByEvent: async (eventId) => {
        try {
            const response = await apiClient.get(`/inscription/event/${eventId}/paid`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    countPaidInscriptionsByEvent: async (eventId) => {
        try {
            const response = await apiClient.get(`/inscription/event/${eventId}/paid/count`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionById: async (id) => {
        try {
            const response = await apiClient.get(`/inscription/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getInscriptionsByCompetitorAndEvent: async (competitorId, eventId) => {
        try {
            const response = await apiClient.get(`/inscription/competitor/${competitorId}/event/${eventId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createInscription: async (inscriptionData) => {
        try {
            const response = await apiClient.post('/inscription', inscriptionData);
            toast.success('Inscription created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateInscription: async (id, inscriptionData) => {
        try {
            const response = await apiClient.put(`/inscription/${id}`, inscriptionData);
            toast.success('Inscription updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteInscription: async (id) => {
        try {
            const response = await apiClient.delete(`/inscription/${id}`);
            toast.success('Inscription deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default inscriptionService