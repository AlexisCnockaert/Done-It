import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';
import { AuthProvider } from './context/AuthContext';
import './styles/base.css';
import './styles/typography.css';
import './styles/components.css';
import './styles/icons.css';
import './styles/ai.css';
import './styles/auth.css';

import HomeButton from './components/HomeButton';
function App() {
    return (
        <BrowserRouter>
            <AuthProvider>
                <HomeButton />
                <AppRoutes />
            </AuthProvider>
        </BrowserRouter>
    );
}

export default App;