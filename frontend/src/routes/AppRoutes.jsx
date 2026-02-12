import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LandingPage from '../pages/LandingPage';
import Home from '../pages/Home';
import TodoPage from '../pages/TodoPage';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import PrivateRoute from '../components/PrivateRoute';

const AppRoutes = () => {
    return (
        <Routes>
            <Route path="/landing" element={<LandingPage />} />
            
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

            <Route path="*" element={<Navigate to="/landing" replace />} />
        </Routes>
    );
};

export default AppRoutes;