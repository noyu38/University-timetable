import { useAuth } from "../context/AuthContext"

const HomePage = () => {
    const {setToken} = useAuth();

    // ログアウト処理
    const handleLogout = () => {
        setToken(null);
    };

    return (
        <div>
            <h2>ホームページ</h2>
            <p>ようこそ！</p>
            <button onClick={handleLogout}>ログアウト</button>
        </div>
    );
};

export default HomePage;