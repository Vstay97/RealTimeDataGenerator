import React from 'react';
import ReactDOM from 'react-dom/client';
import { StyleProvider } from '@ant-design/cssinjs';
import App from './App';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
root.render(
    <React.StrictMode>
        <StyleProvider>
            <App />
        </StyleProvider>
    </React.StrictMode>
);
