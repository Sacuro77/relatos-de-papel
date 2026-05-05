import { useCart } from '../hooks/useCart'
import './Cart.css'

function Cart() {
  const { cart, removeFromCart } = useCart()

  return (
    <aside className="cart-box">
      <h2>Carrito</h2>

      {cart.length === 0 ? (
        <p className="cart-empty">No hay libros en el carrito.</p>
      ) : (
        <ul className="cart-list">
          {cart.map((book, index) => (
            <li className="cart-item" key={index}>
              <p className="cart-title">{book.title}</p>
              <p className="cart-price">${book.price}</p>

              <button
                className="cart-remove"
                onClick={() => removeFromCart(index)}
              >
                Eliminar
              </button>
            </li>
          ))}
        </ul>
      )}
    </aside>
  )
}

export default Cart