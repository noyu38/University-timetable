import './App.css'
import { BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from './components/LoginPage'
import { AuthProvider, useAuth } from './context/AuthContext';
import HomePage from './components/HomePage';

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
  const {token} = useAuth();

  return (
    <div className='App'>
      <header className='App-header'>
        <h1>時間割管理アプリ</h1>
        <Routes>
          <Route
            path='/login'
            element={!token ? <LoginPage /> : <Navigate to="/" />}
          />

          <Route
            path='/'
            element={token ? <HomePage /> : <Navigate to="/login" />}
          />

          <Route path='*' element={<Navigate to="/" />} />
        </Routes>
      </header>
    </div>
  )
}

export default App;
