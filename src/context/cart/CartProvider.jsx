import { useState } from 'react'
import { CartContext } from './CartContext'

export function CartProvider({ children }) {
  const [cart, setCart] = useState([])

  const addToCart = (book) => {
    setCart([...cart, book])
  }

  const removeFromCart = (indexToRemove) => {
    const newCart = cart.filter((book, index) => index !== indexToRemove)
    setCart(newCart)
  }

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart }}>
      {children}
    </CartContext.Provider>
  )
}