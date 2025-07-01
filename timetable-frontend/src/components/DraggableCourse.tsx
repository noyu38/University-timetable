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
            className="p-3 bg-gray-50 border border-gray-200 rounded-md cursor-move hover:bg-gray-100 transition-colors"
        >
            <div className="font-semibold text-gray-800">{course.name}</div>
            <div className="text-xs text-gray-500 mt-1">
                <span>{course.teacher}</span> / <span>{course.room}</span>
            </div>
            <div className={`text-xs font-bold mt-1 ${course.departmentId ? "text-blue-600" : "text-green-600"}`}>
                {course.departmentName}
            </div>
        </div>
    );
};

export default DraggableCourse;