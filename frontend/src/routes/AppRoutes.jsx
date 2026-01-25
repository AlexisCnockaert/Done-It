import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Home from '../pages/Home';
import TodoPage from '../pages/TodoPage'; 

const AppRoutes = () => {
    return (
        <Routes>
            
            <Route path="/" element={<Home />} />
            <Route path="*" element={<Navigate to="/" replace />} />
            <Route path="/todos/:id" element={<TodoPage />} />
        </Routes>
    );
};

export default AppRoutes;