import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext"
import type { TimetableSlotDTO } from "../dto/TimetableSlotDTO";
import apiClient from "../services/api";
import TimetableGrid from "./TimetableGrid";
import CourseList from "./CourseList";
import { DndProvider, useDragLayer } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";

const HomePageContent = () => {
    const { setToken } = useAuth();
    const [timetable, setTimetable] = useState<TimetableSlotDTO[]>([]);
    const [error, setError] = useState('');
    // const [selectedCourse, setSelectedCourse] = useState<CourseDTO | null>(null);
    const [isDrawerOpen, setIsDrawerOpen] = useState<boolean>(false);

    const { isDragging } = useDragLayer((monitor) => ({
        isDragging: monitor.isDragging(),
    }));

    useEffect(() => {
        if (isDragging && isDrawerOpen) {
            setIsDrawerOpen(false);
        }
    }, [isDragging, isDrawerOpen])

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
    const handleAddSlot = async (day: string, period: number, courseId: number) => {

        try {
            const requestBody = {
                courseId: courseId,
                dayOfWeek: day,
                period: period
            };

            const response = await apiClient.post<TimetableSlotDTO>("/timetable", requestBody);

            const newSlot = response.data;

            // 画面を更新
            setTimetable(currentTimetable => [...currentTimetable, newSlot]);
            // setSelectedCourse(null);
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

    const toggleDrawer = () => {
        setIsDrawerOpen(prevState => !prevState);
    };

    return (
        <DndProvider backend={HTML5Backend}>
            <div>
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-800">あなたの時間割</h2>
                    <button
                        onClick={handleLogout}
                        className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded-lg transition duration-200"
                    >ログアウト</button>
                </div>

                {error && <p className="bg-red-100 text-red-700 p-3 rounded-md text-center mb-4">{error}</p>}


                <div className="grid grid-cols-1 lg:grid-cols-3 lg:gap-8">
                    
                    <div className="lg:col-span-2">
                        <TimetableGrid
                            slots={timetable}
                            onDeleteSlot={handleDeleteSlot}
                            onAddSlot={handleAddSlot}
                        />
                    </div>
                    
                    <div className="hidden lg:block">
                        <CourseList />
                    </div>
                </div>

                <div className={`fixed bottom-0 left-0 w-full bg-white shadow-[-2px_0_10px_rgba(0,0,0,0.1)] rounded-t-2xl transition-transform duration-300 ease-in-out lg:hidden ${isDrawerOpen ? 'translate-y-0' : 'translate-y-[calc(100%-50px)]'}`}>
                    <div className="h-[50px] w-full flex justify-center items-center cursor-pointer border-t border-gray-200" onClick={toggleDrawer}>
                        <span className={`text-2xl text-gray-500 transition-transform duration-300 ${isDrawerOpen ? "rotate-180" : ""}`}>▲</span>
                    </div>
                    <div className="p-4 max-h-[40vh] overflow-y-auto">
                        <CourseList />
                    </div>
                </div>
            </div>
        </DndProvider>
    );
};

const HomePage = () => {
    return (
        <DndProvider backend={HTML5Backend}>
            <HomePageContent />
        </DndProvider>
    );
}

export default HomePage;