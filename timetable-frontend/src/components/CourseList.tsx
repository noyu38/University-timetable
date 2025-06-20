import type React from "react";
import { useEffect, useState } from "react";
import type { CourseDTO } from "../dto/CourseDTO";
import apiClient from "../services/api";
import "./css/CourseList.css";
import DraggableCourse from "./DraggableCourse";

interface CourseListProps {
    // selectedCourse: CourseDTO | null;
    // onSelectCourse: (course: CourseDTO | null) => void;
}

const CourseList: React.FC<CourseListProps> = () => {
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
        <div className="course-list-container">
            <h3>履修可能な授業</h3>
            <p>授業を時間割にドラッグ＆ドロップしてください。</p>
            {error && <p style={{color: "red"}}>{error}</p>}
            <div className="course-list">
                {courses.map(course => (
                    <DraggableCourse key={course.id} course={course} />
                ))}
            </div>
        </div>
    );
};

export default CourseList;