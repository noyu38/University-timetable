import type React from "react";
import type { CourseDTO } from "../dto/CourseDTO";
import { useDrag } from "react-dnd";
import { ItemTypes } from "../dnd/itemTypes";

interface DraggableCourseProps {
    course: CourseDTO;
}

const DraggableCourse: React.FC<DraggableCourseProps> = ({ course }) => {
    // useDragフック：コンポーネントをドラッグ可能に
    const [{ isDragging }, drag] = useDrag(() => ({
        type: ItemTypes.COURSE, // ドラッグするアイテムのタイプを設定
        item: { id: course.id }, // ドロップ先に渡すデータを設定
        collect: (monitor) => ({
            // ドラッグ中かどうかを監視し、isDraggingという変数に格納
            isDragging: !!monitor.isDragging(),
        }),
    }));

    return (
        <div
            ref={drag}
            className="course-item"
            style={{ opacity: isDragging ? 0.5 : 1 }}
        >
            <div className="course-name">{course.name}</div>
            <div className="course-details">
                {course.teacher} / {course.room} / {course.departmentName}
            </div>
        </div>
    );
};

export default DraggableCourse;