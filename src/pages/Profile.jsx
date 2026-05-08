import { useContext } from 'react'
import { AuthContext } from '../context/auth/AuthContext'
import { useLogin } from '../hooks/useLogin'
import './Profile.css'

function Profile() {
  const { user } = useContext(AuthContext)
  const { logout } = useLogin()

  if (!user) return null

  const orders = [
    { id: '001', total: 25, status: 'Entregado' },
    { id: '002', total: 18.5, status: 'En proceso' },
    { id: '003', total: 40, status: 'Pendiente' },
    { id: '004', total: 12, status: 'Entregado' },
    { id: '005', total: 30, status: 'Entregado' },
  ]

  return (
    <section className="profile-page">
      <div className="profile-card">
        <div className="profile-header">
          <div>
            <h1>Perfil</h1>
            <p>Datos del usuario y últimos pedidos.</p>
          </div>

          <button className="profile-logout" onClick={logout}>
            Cerrar sesión
          </button>
        </div>

        <div className="profile-section">
          <h2>Datos del usuario</h2>
          <p><strong>Usuario:</strong> {user.username}</p>
          <p><strong>Email:</strong> {user.email}</p>
        </div>

        <div className="profile-section">
          <h2>Últimos pedidos</h2>

          <div className="orders-grid">
            {orders.map((order) => (
              <article className="order-card" key={order.id}>
                <h3>Pedido #{order.id}</h3>
                <p><strong>Total:</strong> ${order.total}</p>
                <p><strong>Estado:</strong> {order.status}</p>
              </article>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}

export default Profile