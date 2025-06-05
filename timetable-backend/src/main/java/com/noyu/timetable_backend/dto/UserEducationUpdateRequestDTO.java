package com.noyu.timetable_backend.dto;

import java.util.Objects;

import jakarta.validation.constraints.NotNull;

public class UserEducationUpdateRequestDTO {

    @NotNull
    private Long facultyId;

    @NotNull
    private Long departmentId;

    public UserEducationUpdateRequestDTO() {

    }

    public UserEducationUpdateRequestDTO(Long facultyId, Long departmentId) {
        this.facultyId = facultyId;
        this.departmentId = departmentId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        UserEducationUpdateRequestDTO userEducationUpdateRequestDTO = (UserEducationUpdateRequestDTO) o;
        return Objects.equals(facultyId, userEducationUpdateRequestDTO.facultyId) &&
                Objects.equals(departmentId, userEducationUpdateRequestDTO.departmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyId, departmentId);
    }

    @Override
    public String toString() {
        return "UserEducationUpdateRequestDTO{" +
                "facultyId = " + facultyId +
                ", departmentId = " + departmentId +
                "}";
    }
}
