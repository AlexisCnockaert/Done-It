import api from './api';

const aiService = {

    generatePlan: async (todoId, todoTitle, userContext = '') => {
        const request = {
            todoId,
            todoTitle,
            userContext: userContext.trim() || null
        };
        
        const response = await api.post('/ai/generate-plan', request);
        return response.data;
    },

    getTodoSteps: async (todoId) => {
        const response = await api.get(`/ai/todos/${todoId}/steps`);
        return response.data;
    },

    toggleStep: async (stepId) => {
        const response = await api.put(`/ai/steps/${stepId}/toggle`);
        return response.data;
    },


    deleteStep: async (stepId) => {
        await api.delete(`/ai/steps/${stepId}`);
    },

    hasSteps: async (todoId) => {
        const response = await api.get(`/ai/todos/${todoId}/has-steps`);
        return response.data;
    }
};

export default aiService;