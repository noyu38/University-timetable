import React, { useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom";
import apiClient from "../services/api";

interface Faculty {
    id: number;
    name: string;
}

interface Department {
    id: number;
    name: string;
}

const SignupPage = () => {
    const [username, setUsername] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [checkPassword, setCheckPassword] = useState<string>(''); // 確認用パスワード（再入力させるやつ）
    const [email, setEmail] = useState<string>('');

    const [faculties, setFaculties] = useState<Faculty[]>([]);
    const [departments, setDepartments] = useState<Department[]>([]);
    const [selectedFaculty, setSelectedFaculty] = useState('');
    const [selectedDepartment, setSelectedDepartment] = useState('');
    const [error, setError] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const fetchFaculties = async () => {
            try {
                const response = await apiClient.get<Faculty[]>("/faculties");
                setFaculties(response.data);
            } catch (e) {
                console.error("学部リストの取得に失敗: ", e);
                setError("学部リストの取得に失敗しました。");
            }
        };

        fetchFaculties();
    }, []);

    // 選択された学部が変わったときに、学科リストを取得する
    useEffect(() => {
        if (selectedFaculty) {
            const fetchDepartments = async () => {
                try {
                    const response = await apiClient.get<Department[]>(`/departments?facultyId=${selectedFaculty}`);
                    setDepartments(response.data);
                    setSelectedDepartment('');
                } catch (e) {
                    console.error("学科リストの取得に失敗: ", e);
                    setError("学科リストの取得に失敗しました。");
                }
            };
            fetchDepartments();
        } else {
            setDepartments([]);
            setSelectedDepartment('');
        }
    }, [selectedFaculty]);

    const handleSignup = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setError('');

        if (password !== checkPassword) {
            setError("パスワードが一致しません");
            return;
        }

        if (!selectedFaculty || !selectedDepartment) {
            setError("学部と学科を選択してください。");
            return;
        }

        try {
            await apiClient.post("/auth/signup", {
                username: username,
                email: email,
                password: password,
                facultyId: Number(selectedFaculty),
                departmentId: Number(selectedDepartment),
            });

            alert("ユーザー登録に成功しました！ログイン画面に移行します");
            navigate("/auth/login");
        } catch (e: any) {
            console.error("ユーザー登録失敗", e);
            if (e.response) {
                setError(e.response.data.message);
            } else {
                setError("ユーザー登録時にエラーが発生しました。");
            }
        }
    };

    return (
        <div className="signup-container">
            <h2>新規登録</h2>
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSignup}>
                <div className="form-group">
                    <label htmlFor="username">ユーザー名</label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">メールアドレス</label>
                    <input
                        type="email"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">パスワード</label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="checkPassword">パスワード（確認用）</label>
                    <input
                        type="password"
                        id="checkPassword"
                        value={checkPassword}
                        onChange={(e) => setCheckPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="faculty">学部</label>
                    <select
                        id="faculty"
                        value={selectedFaculty}
                        onChange={(e) => setSelectedFaculty(e.target.value)}
                        required
                    >
                        <option value="">学部を選択してください</option>
                        {faculties.map(faculty => (
                            <option key={faculty.id} value={faculty.id}>{faculty.name}</option>
                        ))}
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="department">学科</label>
                    <select
                        id="department"
                        value={selectedDepartment}
                        onChange={(e) => setSelectedDepartment(e.target.value)}
                        required
                    >
                        <option value="">学科を選択してください</option>
                        {departments.map(department => (
                            <option key={department.id} value={department.id}>{department.name}</option>
                        ))}
                    </select>
                </div>
                <button type="submit">登録</button>
            </form>
            <p>
                アカウントをお持ちですか？ <Link to="/login">ログインはこちら</Link>
            </p>
        </div>
    );
};

export default SignupPage;