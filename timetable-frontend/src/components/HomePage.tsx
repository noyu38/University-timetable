import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext"
import type { TimetableSlotDTO } from "../dto/TimetableDTO";
import apiClient from "../services/api";
import TimetableGrid from "./TimetableGrid";

const HomePage = () => {
    const {setToken} = useAuth();
    const [timetable, setTimetable] = useState<TimetableSlotDTO[]>([]);
    const [error, setError] = useState('');

    // 一度だけ実行されるuseEffect
    useEffect(() => {
        const fetchTimetable = async () => {
            try {
                // 時間割データを取得
                const response = await apiClient.get<TimetableSlotDTO[]>("/timetable");
                setTimetable(response.data);
            } catch (e) {
                console.error("時間割の取得に失敗しました: ", e);
                setError("時間割の取得に失敗しました。");
            }
        };

        fetchTimetable();
    }, []); // 第二引数の配列が空のため、一度だけ実行される

    const handleLogout = () => {
        setToken(null);
    };

    return (
        <div>
            <h2>ホームページ あなたの時間割</h2>
            <button onClick={handleLogout}>ログアウト</button>
            {error && <p style={{color: "red"}}>{error}</p>}

            <TimetableGrid slots={timetable} />
        </div>
    );
};

export default HomePage;