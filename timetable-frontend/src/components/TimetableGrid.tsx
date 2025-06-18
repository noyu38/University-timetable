import type React from "react";
import type { DayOfWeek, TimetableSlotDTO } from "../dto/TImetableDTO";

interface TimetableGridProps {
    slots: TimetableSlotDTO[];
}

const TimetableGrid: React.FC<TimetableGridProps> = ({ slots }) => {
    // 曜日と時限の定義
    const days: DayOfWeek[] = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"];
    const periods: number[] = [1, 2, 3, 4, 5];

    // スロットのリストを、曜日と時限で検索できるようなデータ構造に変換
    const slotMap = new Map<string, TimetableSlotDTO>();
    slots.forEach(slot => {
        const key = `<span class="math-inline>${slot.dayOfWeek}-</span>${slot.period}`;
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
                        <th>{period}限</th>
                        {days.map(day => {
                            const key = `<span class="math-inline">${day}-</span>${period}`;
                            const slot = slotMap.get(key);
                            return (
                                <td key={key}>
                                    {slot ? (
                                        <div>
                                        <div className="slot-course">{slot.course.name}</div>
                                        <div className="slot-details">{slot.course.room}</div>
                                        <div className="slot-details">{slot.course.teacher}</div>
                                        </div>
                                    ) : (
                                        // スロットが空の場合
                                        ''
                                    )}
                                </td>
                            );
                        })}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

export default TimetableGrid;