import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext"
import type { TimetableSlotDTO } from "../dto/TimetableDTO";
import apiClient from "../services/api";
import TimetableGrid from "./TimetableGrid";

const HomePage = () => {
    const { setToken } = useAuth();
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

    const handleDeleteSlot = async (slotId: number) => {
        // ユーザーに削除を確認する
        if (!window.confirm("この授業を時間割から削除しますか？")) {
            return;
        }

        try {
            // 削除APIを呼び出す
            await apiClient.delete(`/timetable/slots/${slotId}`);

            // 画面を更新
            setTimetable(currentTimetable =>
                currentTimetable.filter(slot => slot.slotId !== slotId)
            );
        } catch (e) {
            console.error("時間割の削除に失敗しました: ", e);
            setError("時間割の削除に失敗しました。");
        }
    };

    return (
        <div>
            <h2>ホームページ あなたの時間割</h2>
            <button onClick={handleLogout}>ログアウト</button>
            {error && <p style={{ color: "red" }}>{error}</p>}

            <TimetableGrid slots={timetable} onDeleteSlot={handleDeleteSlot} />
        </div>
    );
};

export default HomePage;