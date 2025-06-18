import './App.css'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage'
import { useAuth } from './context/AuthContext';
import HomePage from './components/HomePage';

function App() {
  const { token } = useAuth();

  return (
    <Router>
      <div className='App'>
        <h1>時間割管理アプリ</h1>
        <Routes>
          {/* "/login"パスへのルート定義 */}
          <Route
            path='/login'
            element={token ? <Navigate to="/" /> : <LoginPage />}
          />

          {/* "/（ホームページ）"パスへのルート定義 */}
          <Route
            path='/'
            element={token ? <HomePage /> : <Navigate to="/login" />}
          />
        </Routes>
      </div>
    </Router>
  )
}

export default App;
