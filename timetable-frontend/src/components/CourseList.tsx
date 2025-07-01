import type React from "react";
import { useEffect, useState } from "react";
import type { CourseDTO } from "../dto/CourseDTO";
import apiClient from "../services/api";
import DraggableCourse from "./DraggableCourse";


const CourseList: React.FC = () => {
    const [courses, setCourses] = useState<CourseDTO[]>([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                const response = await apiClient.get<CourseDTO[]>("/courses");
                setCourses(response.data);
            } catch (e) {
                console.error("授業リストの取得に失敗: ", e);
                setError("授業リストの取得に失敗しました。");
            }
        };

        fetchCourses();
    }, []);

    // const handleSelect = (course: CourseDTO) => {
    //     // もし選択中の授業を再度クリックしたら、選択を解除する
    //     if (selectedCourse && selectedCourse.id === course.id) {
    //         onSelectCourse(null);
    //     } else {
    //         onSelectCourse(course);
    //     }
    // };

    return (
        <div className="bg-white rounded-lg shadow p-4">
            <h3 className="text-lg font-bold mb-2 text-gray-700">履修可能な授業</h3>
            <p className="text-sm text-gray-500 mb-4">授業を時間割にドラッグ＆ドロップしてください。</p>
            {error && <p className="text-red-500">{error}</p>}
            <div className="space-y-2">
                {courses.map(course => (
                    <DraggableCourse key={course.id} course={course} />
                ))}
            </div>
        </div>
    );
};

export default CourseList;