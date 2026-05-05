import { Link } from 'react-router-dom'
import './Menu.css'

function Menu() {
  return (
    <nav className="menu">
      <Link to="/">Inicio</Link>
      <Link to="/products">Libros</Link>
      <Link to="/login">Login</Link>
      <Link to="/profile">Perfil</Link>
      <Link to="/checkout">Checkout</Link>
    </nav>
  )
}

export default Menu