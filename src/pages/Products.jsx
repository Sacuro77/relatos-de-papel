import { useEffect, useState } from 'react'
import SearchBar from '../components/SearchBar'
import { booksData } from '../data/books'
import { useCart } from '../hooks/useCart'
import './Products.css'

function Products() {
  const [search, setSearch] = useState('')
  const [books] = useState(booksData)

  const { addToCart } = useCart()

  useEffect(() => {
    console.log('La página de productos se cargó correctamente')
  }, [])

  const filteredBooks = books.filter((book) =>
    book.title.toLowerCase().includes(search.toLowerCase())
  )

  return (
    <section className="products-page">
      <div className="products-header">
        <h1>Libros</h1>
        <p>Explora el catálogo disponible de Relatos de Papel.</p>
      </div>

      <SearchBar search={search} onSearchChange={setSearch} />

      <p className="products-search-text">Buscando: {search}</p>

      <div className="products-grid">
        {filteredBooks.map((book) => (
          <article className="book-card" key={book.id}>
            <h3>{book.title}</h3>
            <p className="book-author">{book.author}</p>
            <p className="book-price">${book.price}</p>

            <button onClick={() => addToCart(book)}>
              Agregar al carrito
            </button>
          </article>
        ))}
      </div>
    </section>
  )
}

export default Products