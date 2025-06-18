export interface CourseDTO {
    id: number;
    name: string;
    room: string | null;
    teacher: string | null;
    departmentId: number | null;
    departmentName: string;
}