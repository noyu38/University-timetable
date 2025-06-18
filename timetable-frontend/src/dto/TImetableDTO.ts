import type { CourseDTO } from "./CourseDTO";

export type DayOfWeek = "MONDAY" | "TUESDAY" | "WEDNESDAY" | "THURSDAY" | "FRIDAY" | "SATURDAY" | "SUNDAY";

export interface TimetableSlotDTO {
    slotId: number;
    dayOfWeek: DayOfWeek;
    period: number;
    course: CourseDTO;
}