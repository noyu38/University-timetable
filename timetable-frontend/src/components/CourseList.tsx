import type React from "react";
import { useEffect, useState } from "react";
import type { CourseDTO } from "../dto/CourseDTO";
import apiClient from "../services/api";
import "./css/CourseList.css";

interface CourseListProps {
    selectedCourse: CourseDTO | null;
    onSelectCourse: (course: CourseDTO | null) => void;
}

const CourseList: React.FC<CourseListProps> = ({ selectedCourse, onSelectCourse }) => {
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

    const handleSelect = (course: CourseDTO) => {
        // もし選択中の授業を再度クリックしたら、選択を解除する
        if (selectedCourse && selectedCourse.id === course.id) {
            onSelectCourse(null);
        } else {
            onSelectCourse(course);
        }
    };

    return (
        <div className="course-list-container">
            <h3>履修可能な授業</h3>
            <p>授業を選んでから、時間割の空きコマをクリックしてください。</p>
            {error && <p style={{color: "red"}}>{error}</p>}
            <ul className="course-list">
                {courses.map(course => (
                    <li 
                        key={course.id} 
                        className={`course-item ${selectedCourse?.id === course.id ? "selected" : ""}`}
                        onClick={() => handleSelect(course)}
                    >
                        <div className="course-name">{course.name}</div>
                        <div className="course-details">
                            {course.teacher} / {course.room} / {course.departmentName}
                        </div>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default CourseList;