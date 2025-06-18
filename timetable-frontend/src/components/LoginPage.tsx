import React, { useState } from "react"
import apiClient from "../services/api";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
    // 入力されたユーザー名を保持
    const [username, setUsername] = useState('');
    // 入力されたパスワードを保持
    const [password, setPassword] = useState('');
    // エラーメッセージを保持
    const [error, serError] = useState('');
    // 成功メッセージを保持
    // const [successMessage, setSuccessMessage] = useState('');

    const {setToken} = useAuth();
    const navigate = useNavigate();

    // フォームが送信されたときの処理
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        serError('');

        try {
            // apiClientを使って、バックエンドの/auth/loginにPOSTリクエストを送信
            const response = await apiClient.post("/auth/login", {
                username: username,
                password: password,
            });

            // ログインに成功した場合
            setToken(response.data.token);

            navigate("/");
        } catch (e: any) {
            console.error("ログイン失敗: ", e);
            if (e.response) {
                serError(e.response.data.message || "ユーザー名またはパスワードが無効です。");
            } else {
                serError("ログイン中にエラーが発生しました。");
            }
        }
    };

    return (
        <div>
            <h2>ログイン</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="username">ユーザー名</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">パスワード</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">ログイン</button>
            </form>
            {error && <p style={{color: "red"}}>{error}</p>}
        </div>
    );
};

export default LoginPage;