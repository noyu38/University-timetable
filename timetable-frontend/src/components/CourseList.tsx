import type React from "react";
import { useEffect, useState } from "react";
import type { CourseDTO } from "../dto/CourseDTO";
import apiClient from "../services/api";
import "./css/CourseList.css";

interface CourseListProps {

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

    return (
        <div className="course-list-container">
            <h3>履修可能な授業</h3>
            {error && <p style={{color: "red"}}>{error}</p>}
            <ul className="course-list">
                {courses.map(course => (
                    <li key={course.id} className="course-item">
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