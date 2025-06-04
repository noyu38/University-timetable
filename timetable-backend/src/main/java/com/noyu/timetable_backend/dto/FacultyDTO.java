package com.noyu.timetable_backend.dto;

import java.util.Objects;
public class FacultyDTO {
    
    private Long id;
    private String name;

    public FacultyDTO() {

    }

    public FacultyDTO(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        FacultyDTO faculty = (FacultyDTO) o;
        return Objects.equals(id, faculty.id) &&
               Objects.equals(name, faculty.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "FacultyDTO{" +
               "id = " + id +
               ", name = " + name + '\'' +
               "}";
    }
}
