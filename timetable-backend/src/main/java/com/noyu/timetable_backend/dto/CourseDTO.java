package com.noyu.timetable_backend.dto;

import java.util.Objects;

public class CourseDTO {

    private Long id;
    private String name;
    private String room;
    private String teacher;
    private Long departmentId;
    private String departmentName;

    public CourseDTO() {

    }

    public CourseDTO(Long id, String name, String room, String teacher, Long departmentId, String departmentName) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(id, courseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
