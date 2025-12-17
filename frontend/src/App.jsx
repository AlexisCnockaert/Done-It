import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';
import './styles/app.css';
import HomeButton from './components/HomeButton';
function App() {
    return (
        <BrowserRouter>
          <HomeButton />
            <AppRoutes />
        </BrowserRouter>
    );
}

export default App;