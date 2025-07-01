import type React from "react";
import type { DayOfWeek, TimetableSlotDTO } from "../dto/TimetableSlotDTO";
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
            className={`border-l border-gray-200 h-24 p-1 align-top relative transition-colors ${isActive ? "bg-blue-100" : "hover:bg-gray-50"}`}
        >
            {slot ? (
                <div className="bg-gray-200 border border-blue-200 rounded-md p-2 h-full text-xs">
                    <div className="font-bold text-blue-800">{slot.course.name}</div>
                    <div className="text-gray-600">{slot.course.room}</div>
                    <div className="text-gray-600 mt-1">{slot.course.teacher}</div>
                    <button
                        className="absolute top-1 right-1 w-5 h-5 bg-red-500 text-white rounded-full flex items-center justify-center text-xs opacity-50 hover:opacity-100 transition-opacity"
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
        <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="w-full table-fixed">
                <thead className="bg-gray-50">
                    <tr>
                        <th className="w-16 p-2 border-b border-gray-200"></th>
                        {days.map(day => (
                            <th key={day} className="p-2 border-b border-l border-gray-200 text-sm font-semibold text-gray-600">
                                {day.substring(0, 3)}
                            </th> // MON, TUE, …のように表示される
                        ))}
                    </tr>
                </thead>
                <tbody>
                    {periods.map(period => (
                        <tr key={period} className="border-t border-gray-200">
                            <th className="p-2 border-r border-gray-200 text-sm font-semibold text-gray-600">{period}コマ</th>
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
        </div>
    );
};

export default TimetableGrid;