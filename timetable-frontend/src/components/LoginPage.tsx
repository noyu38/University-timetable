import React, { useState } from "react"
import apiClient from "../services/api";

const LoginPage = () => {
    // 入力されたユーザー名を保持
    const [username, setUsername] = useState('');
    // 入力されたパスワードを保持
    const [password, setPassword] = useState('');
    // エラーメッセージを保持
    const [error, serError] = useState('');
    // 成功メッセージを保持
    const [successMessage, setSuccessMessage] = useState('');

    // フォームが送信されたときの処理
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        serError('');
        setSuccessMessage('');

        try {
            // apiClientを使って、バックエンドの/auth/loginにPOSTリクエストを送信
            const response = await apiClient.post("/auth/login", {
                username: username,
                password: password,
            });

            // ログインに成功した場合
            console.log("ログイン成功: ", response.data);

            const token = response.data.token;
            localStorage.setItem("token", token);

            setSuccessMessage("ログインに成功しました");
        } catch (e: any) {
            console.error("ログイン失敗: ", e);
            if (e.response) {
                serError(e.response.data);
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
            {successMessage && <p style={{color: "green"}}>{successMessage}</p>}
        </div>
    );
};

export default LoginPage;