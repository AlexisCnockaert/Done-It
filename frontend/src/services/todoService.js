import api from './api';

const todoService = {
    getAllTodos: async () => {
        const response = await api.get('/todos');
        return response.data;
    },

    getTodoById: async (id) => {
        const response = await api.get(`/todos/${id}`);
        return response.data;
    },

    createTodo: async (title) => {
        const todoRequest = { title: title.trim() };
        const response = await api.post('/todos', todoRequest);
        return response.data;
    },

    toggleTodo: async (id) => {
        const response = await api.put(`/todos/${id}/toggle`);
        return response.data;
    },

    deleteTodo: async (id) => {
        await api.delete(`/todos/${id}`);
    },
};

export default todoService;