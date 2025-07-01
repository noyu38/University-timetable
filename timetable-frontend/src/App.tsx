import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage'
import { AuthProvider, useAuth } from './context/AuthContext';
import HomePage from './components/HomePage';
import SignupPage from './components/SignupPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}

const AppRoutes = () => {
  const { token } = useAuth();

  return (
    <div className='App bg-gray-50 min-h-screen font-sans'>
      <header className='bg-white shadow-sm'>
        <div className='max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8'>
          <h1 className='text-3xl font-bold leading-tight text-gray-900 text-center'>
            時間割アプリ
          </h1>
        </div>
      </header>
      <main>
        <div className='max-w-7xl mx-auto py-6 sm:px-6 lg-px-8'>
          <Routes>
            <Route
              path='/login'
              element={!token ? <LoginPage /> : <Navigate to="/" />}
            />

            <Route
              path='/signup'
              element={!token ? <SignupPage /> : <Navigate to="/" />}
            />

            <Route
              path='/'
              element={token ? <HomePage /> : <Navigate to="/login" />}
            />

            <Route path='*' element={<Navigate to="/" />} />
          </Routes>
        </div>
      </main>
    </div>
  )
}

export default App;
