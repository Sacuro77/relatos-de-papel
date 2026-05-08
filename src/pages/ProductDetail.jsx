import { Link, useParams } from 'react-router-dom'
import { booksData } from '../data/books'
import { useCart } from '../hooks/useCart'
import './ProductDetail.css'

function ProductDetail() {
  const { id } = useParams()
  const { addToCart } = useCart()

  const book = booksData.find((item) => item.id === Number(id))

  if (!book) {
    return (
      <section className="product-detail-page">
        <div className="product-detail-card">
          <h1>Libro no encontrado</h1>
          <p>No existe un libro con el identificador solicitado.</p>

          <Link className="back-link" to="/products">
            Volver al catálogo
          </Link>
        </div>
      </section>
    )
  }

  return (
    <section className="product-detail-page">
      <div className="product-detail-card">
        <img
          className="product-detail-image"
          src={book.image}
          alt={book.title}
        />

        <h1>{book.title}</h1>

        <p className="product-author">
          <strong>Autor:</strong> {book.author}
        </p>

        <p className="product-description">
          Este libro forma parte del catálogo simulado de Relatos de Papel.
          La información se presenta como dato mock para representar la futura
          integración con el backend.
        </p>

        <p className="product-price">${book.price}</p>

        <button
          className="add-cart-button"
          onClick={() => addToCart(book)}
        >
          Agregar al carrito
        </button>

        <Link className="back-link" to="/products">
          Volver al catálogo
        </Link>
      </div>
    </section>
  )
}

export default ProductDetail