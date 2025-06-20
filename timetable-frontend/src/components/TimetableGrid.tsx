import type React from "react";
import type { DayOfWeek, TimetableSlotDTO } from "../dto/TimetableSlotDTO";
import "./css/TimetableGrid.css";
import { useDrop } from "react-dnd";
import { ItemTypes } from "../dnd/itemTypes";

interface TimetableGridProps {
    slots: TimetableSlotDTO[];
    onDeleteSlot: (slotId: number) => void;
    onAddSlot: (day: DayOfWeek, period: number, courseId: number) => void;
}

const TimetableCell: React.FC<{
    day: DayOfWeek;
    period: number;
    slot: TimetableSlotDTO | undefined;
    onAddSlot: (day: DayOfWeek, period: number, courseId: number) => void;
    onDeleteSlot: (slotId: number) => void;
}> = ({ day, period, slot, onAddSlot, onDeleteSlot }) => {
    const [{ isOver, canDrop }, drop] = useDrop(() => ({
        accept: ItemTypes.COURSE, // "course" タイプのアイテムのみ受け入れる
        canDrop: () => !slot, // スロットが空の場合のみドロップ可
        drop: (item: { id: number }) => onAddSlot(day, period, item.id),
        collect: (monitor) => ({
            isOver: !!monitor.isOver(),
            canDrop: !!monitor.canDrop(),
        }),
    }));

    const isActive = isOver && canDrop;

    return (
        <td
            ref={drop}
            className={isActive ? "droppable-cell" : ""}
        >
            {slot ? (
                <div>
                    <div className="slot-course">{slot.course.name}</div>
                    <div className="slot-details">{slot.course.room}</div>
                    <div className="slot-details">{slot.course.teacher}</div>
                    <button
                        className="delete-slot-button"
                        onClick={(e) => {
                            e.stopPropagation();
                            onDeleteSlot(slot.slotId)
                        }}
                    >
                        ×
                    </button>
                </div>
            ) : ("")}
        </td>
    )
}


const TimetableGrid: React.FC<TimetableGridProps> = ({ slots, onDeleteSlot, onAddSlot }) => {
    // 曜日と時限の定義
    const days: DayOfWeek[] = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];
    const periods: number[] = [1, 2, 3, 4, 5];

    // スロットのリストを、曜日と時限で検索できるようなデータ構造に変換
    const slotMap = new Map<string, TimetableSlotDTO>();
    slots.forEach(slot => {
        const key = `${slot.dayOfWeek}-${slot.period}`;
        slotMap.set(key, slot);
    });

    return (
        <table className="timetable-grid">
            <thead>
                <tr>
                    <th></th>
                    {days.map(day => (
                        <th key={day}>{day.substring(0, 3)}</th> // MON, TUE, …のように表示される
                    ))}
                </tr>
            </thead>
            <tbody>
                {periods.map(period => (
                    <tr key={period}>
                        <th>{period}コマ</th>
                        {days.map(day => {
                            const key = `${day}-${period}`;
                            const slot = slotMap.get(key);
                            return (
                                <TimetableCell
                                    key={key}
                                    day={day}
                                    period={period}
                                    slot={slot}
                                    onAddSlot={onAddSlot}
                                    onDeleteSlot={onDeleteSlot}
                                />
                            );
                        })}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default TimetableGrid;