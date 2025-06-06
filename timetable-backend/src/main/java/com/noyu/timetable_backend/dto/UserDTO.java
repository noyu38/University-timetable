package com.noyu.timetable_backend.dto;

import java.util.Objects;

public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private FacultyDTO faculty;
    private DepartmentDTO department;

    public UserDTO() {

    }

    public UserDTO(Long id, String username, String email, FacultyDTO faculty, DepartmentDTO department) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FacultyDTO getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyDTO faculty) {
        this.faculty = faculty;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        UserDTO user = (UserDTO) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(faculty, user.faculty) &&
                Objects.equals(department, user.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, faculty, department);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id = " + id +
                ", username = " + username + '\'' +
                ", email = " + email + '\'' +
                ", faculty_id=" + (faculty != null ? faculty.getId() : null) +
                ", department_id=" + (department != null ? department.getId() : null) +
                '}';
    }
}
