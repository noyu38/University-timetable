import axios from "axios";

const apiClient = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json",
    },
});

// 今後のAPIリクエストで、ヘッダーにJWTトークンを自動的に追加するための設定
apiClient.interceptors.request.use(
    (config) => {
        // localStorageからトークンを取得する
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);
export default apiClient;
