import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Home from '../pages/Home';
import TodoPage from '../pages/TodoPage';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import PrivateRoute from '../components/PrivateRoute';

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            <Route 
                path="/" 
                element={
                    <PrivateRoute>
                        <Home />
                    </PrivateRoute>
                } 
            />
            <Route 
                path="/todos/:id" 
                element={
                    <PrivateRoute>
                        <TodoPage />
                    </PrivateRoute>
                } 
            />

            <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
    );
};

export default AppRoutes;