import { BrowserRouter, Routes, Route } from 'react-router-dom'
import './App.css'

import Menu from './components/Menu'
import Footer from './components/Footer'
import Cart from './components/Cart'

import Home from './pages/Home'
import Login from './pages/Login'
import Products from './pages/Products'
import ProductDetail from './pages/ProductDetail'
import Profile from './pages/Profile'
import Checkout from './pages/Checkout'

import PrivateRoute from './components/PrivateRoute'

function App() {
  return (
    <BrowserRouter>
      <div className="app">

        <Menu />

        <main className="app-main">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/products" element={<Products />} />
            <Route path="/product/:id" element={<ProductDetail />} />

            <Route
              path="/profile"
              element={
                <PrivateRoute>
                  <Profile />
                </PrivateRoute>
              }
            />

            <Route
              path="/checkout"
              element={
                <PrivateRoute>
                  <Checkout />
                </PrivateRoute>
              }
            />
          </Routes>
        </main>

        <Cart />

        <Footer />
      </div>
    </BrowserRouter>
  )
}

export default App