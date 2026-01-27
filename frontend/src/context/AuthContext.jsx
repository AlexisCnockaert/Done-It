import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadUser = () => {
            const savedUser = authService.getUser();
            if (savedUser) {
                setUser(savedUser);
            }
            setLoading(false);
        };

        loadUser();
    }, []);

    const register = async (username, email, password) => {
        const response = await authService.register(username, email, password);
        setUser({
            id: response.userId,
            username: response.username,
            email: response.email
        });
        return response;
    };

    const login = async (usernameOrEmail, password) => {
        const response = await authService.login(usernameOrEmail, password);
        setUser({
            id: response.userId,
            username: response.username,
            email: response.email
        });
        return response;
    };

    const logout = () => {
        authService.logout();
        setUser(null);
    };

    const value = {
        user,
        loading,
        register,
        login,
        logout,
        isAuthenticated: !!user
    };

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within AuthProvider');
    }
    return context;
};
