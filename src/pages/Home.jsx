import { Link } from 'react-router-dom'
import './Home.css'

function Home() {
  return (
    <section className="home-page">
      <div className="home-hero">
        <div className="home-content">
          <h1>Relatos de Papel</h1>
          <p>
            Plataforma front-end desarrollada con React para explorar libros,
            consultar detalles y simular una experiencia de compra en línea.
          </p>

          <div className="home-actions">
            <Link className="home-button primary" to="/products">
              Explorar libros
            </Link>

            <Link className="home-button secondary" to="/login">
              Iniciar sesión
            </Link>
          </div>
        </div>

        <div className="home-panel">
          <h2>Actividad 1</h2>
          <p>SPA con React, Hooks, Context API y React Router.</p>

          <ul>
            <li>Catálogo de libros con búsqueda</li>
            <li>Detalle individual del libro</li>
            <li>Carrito de compras</li>
            <li>Perfil y checkout protegidos</li>
          </ul>

          <div className="integrantes">
            <h3>Integrantes:</h3>
            <p>Santiago Rivera</p>
            <p>Wilson Quito</p>
            <p>Juan Diego Leon</p>
          </div>
        </div>
      </div>
    </section>
  )
}

export default Home