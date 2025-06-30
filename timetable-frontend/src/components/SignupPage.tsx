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
        <div className="w-full max-w-md mx-auto">
            <form onSubmit={handleSignup} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
                <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">新規登録</h2>

                {error && (
                    <p className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4 text-center">
                        {error}
                    </p>
                )}

                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">
                        ユーザー名
                    </label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="email">
                        メールアドレス
                    </label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="faculty">学部</label>
                    <select id="faculty" value={selectedFaculty} onChange={(e) => setSelectedFaculty(e.target.value)} required className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        <option value="">学部を選択してください</option>
                        {faculties.map(faculty => (
                            <option key={faculty.id} value={faculty.id}>{faculty.name}</option>
                        ))}
                    </select>
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="department">学科</label>
                    <select id="department" value={selectedDepartment} onChange={(e) => setSelectedDepartment(e.target.value)} required disabled={!selectedFaculty} className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        <option value="">学科を選択してください</option>
                        {departments.map(department => (
                            <option key={department.id} value={department.id}>{department.name}</option>
                        ))}
                    </select>
                </div>


                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
                        パスワード
                    </label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>

                <div className="mb-6">
                    <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="confirmPassword">
                        パスワード（確認）
                    </label>
                    <input
                        className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                        id="confirmPassword"
                        type="password"
                        value={checkPassword}
                        onChange={(e) => setCheckPassword(e.target.value)}
                        required
                    />
                </div>

                <div className="flex items-center justify-between">
                    <button className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full" type="submit">
                        登録する
                    </button>
                </div>

                <p className="text-center text-gray-500 text-xs mt-6">
                    アカウントをお持ちですか？
                    <Link to="/login" className="font-bold text-blue-500 hover:text-blue-800 ml-1">
                        ログインはこちら
                    </Link>
                </p>
            </form>
        </div>
    );
};

export default SignupPage;