/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-toastify";

const eventService = {
    getAllEvents: async () => {
        try {
            const response = await apiClient.get('/event');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getUpcomingEvents: async () => {
        try {
            const response = await apiClient.get('/event/upcoming');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventsBySport: async (sportId) => {
        try {
            const response = await apiClient.get(`/event/sport/${sportId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventsByOrganizer: async (organizerId) => {
        try {
            const response = await apiClient.get(`/event/organizer/${organizerId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventsByStatus: async (status) => {
        try {
            const response = await apiClient.get(`/event/status/${status}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventsByLocation: async (country, city) => {
        try {
            const response = await apiClient.get('/event/location', {
                params: {country, city}
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getPastEvents: async () => {
        try {
            const response = await apiClient.get('/event/past');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    searchEvents: async ({sportId, country, city, query}) => {
        try {
            const response = await apiClient.get('/event/search', {
                params: {sportId, country, city, query}
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventById: async (id) => {
        try {
            const response = await apiClient.get(`/event/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getEventCategories: async (eventId) => {
        try {
            const response = await apiClient.get(`/event/${eventId}/categories`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    addCategoryToEvent: async (eventId, categoryId) => {
        try {
            await apiClient.post(`/event/${eventId}/category/${categoryId}`);
            toast.success('Category added to event successfully.');
        } catch (error) {
            throw error;
        }
    },

    removeCategoryFromEvent: async (eventId, categoryId) => {
        try {
            await apiClient.delete(`/event/${eventId}/category/${categoryId}`);
            toast.success('Category removed from event successfully.');
        } catch (error) {
            throw error;
        }
    },

    createEvent: async (eventData) => {
        try {
            const response = await apiClient.post('/event', eventData);
            toast.success('Event created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateEvent: async (eventId, eventData) => {
        try {
            const response = await apiClient.put(`/event/${eventId}`, eventData);
            toast.success('Event updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteEvent: async (eventId) => {
        try {
            await apiClient.delete(`/event/${eventId}`);
            toast.success('Event deleted successfully.');
        } catch (error) {
            throw error;
        }
    },
};

export default eventService;