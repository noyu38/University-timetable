import { createContext, useContext, useState } from "react";

interface AuthContextType {
    token: string | null;
    setToken: (token: string | null) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// アプリケーション全体をラップして、コンテキストの値を渡すためのプロバイダーコンポーネント
export const AuthProvider = ({ children }: {children: string | null}) => {
    // トークンの状態を管理。初期値としてlocalStorageから読み込む
    const [token, setTokenState] = useState<string | null>(localStorage.getItem("token"));

    // トークンを設定する関数。useStateとlocalStorageの両方を更新
    const setToken = (newToken: string | null) => {
        setTokenState(newToken);
        if (newToken) {
            localStorage.setItem("token", newToken);
        } else {
            localStorage.removeItem("token");
        }
    };

    return (
        <AuthContext.Provider value={{token, setToken}}>
            {children}
        </AuthContext.Provider>
    );
};

// 他のコンポーネントから簡単にコンテキストを呼び出すためのカスタムフック
export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }

    return context;
};