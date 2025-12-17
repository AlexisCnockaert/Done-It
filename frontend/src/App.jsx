import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';
import './styles/base.css';
import './styles/typography.css';
import './styles/components.css';
import './styles/icons.css';
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