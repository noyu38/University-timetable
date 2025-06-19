import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext"
import type { TimetableSlotDTO } from "../dto/TimetableDTO";
import apiClient from "../services/api";
import TimetableGrid from "./TimetableGrid";
import type { CourseDTO } from "../dto/CourseDTO";
import "./css/HomePage.css";
import CourseList from "./CourseList";

const HomePage = () => {
    const { setToken } = useAuth();
    const [timetable, setTimetable] = useState<TimetableSlotDTO[]>([]);
    const [error, setError] = useState('');
    const [selectedCourse, setSelectedCourse] = useState<CourseDTO | null>(null);

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

    // 時間割に授業を追加
    const handleAddSlot = async (day: string, period: number) => {
        if (!selectedCourse) {
            alert("先に追加したい授業をリストから選択してください。");
            return;
        }

        try {
            const requestBody = {
                courseId: selectedCourse.id,
                dayOfWeek: day,
                period: period
            };

            const response = await apiClient.post<TimetableSlotDTO>("/timetable", requestBody);

            // 画面を更新
            setTimetable([...timetable, response.data]);
            setSelectedCourse(null);
        } catch (e: any) {
            console.error("授業の登録に失敗しました: ", e);
            if (e.response && e.response.status === 409) {
                alert("指定された時間にはすでに授業が登録されています。");
            } else {
                alert("授業の登録に失敗しました。");
            }
        }
    }

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

            <div className="main-container">
                <div className="timetable-container">
                    <TimetableGrid
                        slots={timetable}
                        onDeleteSlot={handleDeleteSlot}
                        onAddSlot={handleAddSlot}
                    />
                </div>
                <div className="course-list-wrapper">
                    <CourseList
                        selectedCourse={selectedCourse}
                        onSelectCourse={setSelectedCourse}
                    />
                </div>
            </div>
        </div>
    );
};

export default HomePage;