import { useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/auth/AuthContext'

export function useLogin() {
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const { setUser } = useContext(AuthContext)
  const navigate = useNavigate()

  const login = async (username, password) => {
    setIsLoading(true)
    setError('')

    // Validación básica
    if (!username.trim() || !password.trim()) {
      setError('Usuario y contraseña son obligatorios')
      setIsLoading(false)
      return { success: false }
    }

    // Simulación (como tu profe)
    await new Promise((resolve) => setTimeout(resolve, 1000))

    // 🔥 USUARIO HARDCODED (igual idea del profe)
    if (username === 'santiago' && password === '123456') {
      const userData = {
        username: 'santiago',
        name: 'Santiago Rivera',
        email: 'santiago@relatos.com',
      }

      setUser(userData)
      setIsLoading(false)
      return { success: true }
    } else {
      setError('Credenciales incorrectas')
      setIsLoading(false)
      return { success: false }
    }
  }

  const logout = () => {
    setUser(null)
    navigate('/')
  }

  return {
    login,
    logout,
    isLoading,
    error,
    clearError: () => setError(''),
  }
}