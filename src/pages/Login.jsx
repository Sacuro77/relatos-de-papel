import { useState, useContext } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { AuthContext } from '../context/auth/AuthContext'
import { useLogin } from '../hooks/useLogin'
import './Login.css'

function Login() {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
  })

  const [fieldErrors, setFieldErrors] = useState({})

  const { user } = useContext(AuthContext)
  const location = useLocation()
  const { login, isLoading, error, clearError } = useLogin()

  if (user) {
    const from = location.state?.from || '/profile'
    return <Navigate to={from} replace />
  }

  function handleChange(e) {
    const { name, value } = e.target

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))

    if (fieldErrors[name]) {
      setFieldErrors((prev) => ({
        ...prev,
        [name]: '',
      }))
    }

    if (error) {
      clearError()
    }
  }

  async function handleSubmit(e) {
    e.preventDefault()

    const newErrors = {}

    if (!formData.username.trim()) {
      newErrors.username = 'El usuario es obligatorio'
    }

    if (!formData.password.trim()) {
      newErrors.password = 'La contraseña es obligatoria'
    }

    if (Object.keys(newErrors).length > 0) {
      setFieldErrors(newErrors)
      return
    }

    const result = await login(formData.username, formData.password)

    if (result.success) {
      setFormData({ username: '', password: '' })
      setFieldErrors({})
    }
  }

  return (
    <section className="login-page">
      <h1>Iniciar Sesión</h1>
      <p>Accede a tu cuenta de Relatos de Papel</p>

      <form onSubmit={handleSubmit}>
        {error && <p className="login-error">{error}</p>}

        <div>
          <label htmlFor="username">Usuario</label>
          <input
            id="username"
            name="username"
            type="text"
            value={formData.username}
            onChange={handleChange}
            placeholder="Nombre de usuario"
            disabled={isLoading}
          />
          {fieldErrors.username && (
            <p className="login-error">{fieldErrors.username}</p>
          )}
        </div>

        <div>
          <label htmlFor="password">Contraseña</label>
          <input
            id="password"
            name="password"
            type="password"
            value={formData.password}
            onChange={handleChange}
            placeholder="Contraseña"
            disabled={isLoading}
          />
          {fieldErrors.password && (
            <p className="login-error">{fieldErrors.password}</p>
          )}
        </div>

        <button type="submit" disabled={isLoading}>
          {isLoading ? 'Iniciando sesión...' : 'Iniciar sesión'}
        </button>
      </form>

      <div className="login-demo">
        <h3>Credenciales de prueba</h3>
        <p><strong>Usuario:</strong> santiago</p>
        <p><strong>Clave:</strong> 123456</p>
      </div>
    </section>
  )
}

export default Login