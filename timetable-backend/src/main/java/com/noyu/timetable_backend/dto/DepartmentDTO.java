package com.noyu.timetable_backend.dto;

import java.util.Objects;

public class DepartmentDTO {

    private Long id;
    private String name;

    public DepartmentDTO() {

    }

    public DepartmentDTO(Long id, String name) {
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
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        DepartmentDTO department = (DepartmentDTO) o;
        return Objects.equals(id, department.id) &&
                Objects.equals(name, department.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id = " + id +
                ", name = " + name + '\'' +
                "}";
    }
}
