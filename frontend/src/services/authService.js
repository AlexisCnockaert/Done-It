import api from './api';

const authService = {

    register: async (username, email, password) => {
        console.log(process.env.REACT_APP_API_URL);

        const response = await api.post('/auth/register', {
            username,
            email,
            password
        });
        

        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify({
                id: response.data.userId,
                username: response.data.username,
                email: response.data.email
            }));
        }
        
        return response.data;
    },


    login: async (usernameOrEmail, password) => {
        const response = await api.post('/auth/login', {
            usernameOrEmail,
            password
        });
        
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('user', JSON.stringify({
                id: response.data.userId,
                username: response.data.username,
                email: response.data.email
            }));
        }
        
        return response.data;
    },

    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },

    getCurrentUser: async () => {
        const response = await api.get('/auth/me');
        return response.data;
    },

    getToken: () => {
        return localStorage.getItem('token');
    },

    getUser: () => {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    },

    isAuthenticated: () => {
        return !!localStorage.getItem('token');
    }
};

export default authService;