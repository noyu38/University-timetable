import React, { useState } from "react"
import apiClient from "../services/api";
import { useAuth } from "../context/AuthContext";
import { Link, useNavigate } from "react-router-dom";

const LoginPage = () => {
    // 入力されたユーザー名を保持
    const [username, setUsername] = useState<string>('');
    // 入力されたパスワードを保持
    const [password, setPassword] = useState<string>('');
    // エラーメッセージを保持
    const [error, serError] = useState('');
    // 成功メッセージを保持
    // const [successMessage, setSuccessMessage] = useState('');

    const { setToken } = useAuth();
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
        <div className="w-full max-w-xs mx-auto">
            <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
                <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">ログイン</h2>

                {error && (
                    <p className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4 text-center">
                        {error}
                    </p>
                )}

                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">ユーザー名</label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="mb-6">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
                        パスワード
                    </label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="flex items-center justify-between">
                    <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full" type="submit">
                        ログイン
                    </button>
                </div>
            </form>
            {error && <p style={{ color: "red" }}>{error}</p>}
            <p className="text-center text-gray-500 text-xs mt-6">
                アカウントをお持ちではありませんか？
                <Link to="/signup" className="font-bold text-blue-500 hover:text-blue-800 ml-1">
                    新規登録はこちら
                </Link>
            </p>
        </div>
    );
};

export default LoginPage;