import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

import { CartProvider } from './context/cart/CartProvider'
import { AuthProvider } from './context/auth/AuthProvider' // 👈 NUEVO

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AuthProvider>        
      <CartProvider>
        <App />
      </CartProvider>
    </AuthProvider>
  </StrictMode>,
)