/* eslint-disable no-useless-catch */
import apiClient from "../api/apiClient";
import { toast } from "react-hot-toast";

const categoryService = {
    getAllCategories: async () => {
        try {
            const response = await apiClient.get('/category');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getCategoriesBySport: async (sportId) => {
        try {
            const response = await apiClient.get(`/category/sport/${sportId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getCategoriesByGender: async (genderId) => {
        try {
            const response = await apiClient.get(`/category/gender/${genderId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getCategoriesByLevel: async (levelId) => {
        try {
            const response = await apiClient.get(`/category/level/${levelId}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    searchCategories: async (filters) => {
        try {
            const params = new URLSearchParams();
            
            if (filters.sportId) params.append('sportId', filters.sportId);
            if (filters.genderId) params.append('genderId', filters.genderId);
            
            if (filters.levelId) params.append('levelId', filters.levelId);
            if (filters.minAge !== undefined && filters.minAge !== null) params.append('minAge', filters.minAge);
            if (filters.maxAge !== undefined && filters.maxAge !== null) params.append('maxAge', filters.maxAge);
            if (filters.minWeight !== undefined && filters.minWeight !== null) params.append('minWeight', filters.minWeight);
            if (filters.maxWeight !== undefined && filters.maxWeight !== null) params.append('maxWeight', filters.maxWeight);

            const response = await apiClient.get(`/category/search?${params.toString()}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getCategoryById: async (id) => {
        try {
            const response = await apiClient.get(`/category/${id}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    getCategoryBySportAndName: async (sportId, name) => {
        try {
            const response = await apiClient.get(`/category/sport/${sportId}/name/${name}`);
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    createCategory: async (categoryData) => {
        try {
            const response = await apiClient.post('/category', categoryData);
            toast.success('Category created successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    updateCategory: async (id, categoryData) => {
        try {
            const response = await apiClient.put(`/category/${id}`, categoryData);
            toast.success('Category updated successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    deleteCategory: async (id) => {
        try {
            const response = await apiClient.delete(`/category/${id}`);
            toast.success('Category deleted successfully.');
            return response.data;
        } catch (error) {
            throw error;
        }
    },
};

export default categoryService;