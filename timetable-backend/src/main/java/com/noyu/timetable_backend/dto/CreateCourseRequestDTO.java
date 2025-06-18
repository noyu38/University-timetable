package com.noyu.timetable_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCourseRequestDTO {

    @NotBlank(message = "授業名は必須です")
    @Size(max = 100, message = "授業名は100文字以内で入力してください")
    private String name;

    @Size(max = 50, message = "教室名は50文字以内で入力してください")
    private String room;

    @Size(max = 50, message = "教員名は50文字以内で入力してください")
    private String teacher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
