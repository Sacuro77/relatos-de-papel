import { useCart } from '../hooks/useCart'
import { useNavigate } from 'react-router-dom'
import './Checkout.css'

function Checkout() {
  const { cart, clearCart } = useCart()
  const navigate = useNavigate()

  const total = cart.reduce((acc, book) => acc + Number(book.price), 0)

  const handleCheckout = () => {
    if (cart.length === 0) {
      alert('El carrito está vacío')
      return
    }

    alert('Compra realizada con éxito 🎉')

    clearCart()

    navigate('/')
  }

  return (
    <section className="checkout-page">
      <div className="checkout-card">
        <h1>Checkout</h1>
        <p>Revisa tu compra antes de confirmar.</p>

        {cart.length === 0 ? (
          <div className="checkout-empty">
            <p>No hay productos en el carrito.</p>
          </div>
        ) : (
          <>
            <div className="checkout-list">
              {cart.map((book, index) => (
                <article className="checkout-item" key={index}>
                  <div>
                    <h3>{book.title}</h3>
                    <p>{book.author}</p>
                  </div>

                  <span>${book.price}</span>
                </article>
              ))}
            </div>

            <div className="checkout-total">
              <h2>Total: ${total.toFixed(2)}</h2>
            </div>

            <button
              className="checkout-button"
              onClick={handleCheckout}
            >
              Confirmar compra
            </button>
          </>
        )}
      </div>
    </section>
  )
}

export default Checkout