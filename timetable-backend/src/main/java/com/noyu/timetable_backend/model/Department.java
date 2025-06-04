package com.noyu.timetable_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 学科名

    // 多くの学科を一つの学部に所属させる
    // FetchType.LAZY 遅延フェッチ facultyフィールドにアクセスがあったときに初めてFacultyの情報を取得するようにする
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable =  false) // departmentsテーブルにfaculty_idカラムを作成し、facultiesテーブルの主キーと関連付ける
    private Faculty faculty; // この学科が所属する学部

    public Department() {

    }

    public Department(String name, Faculty faculty) {
        this.name = name;
        this.faculty = faculty;
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

    public Faculty getFaculty() {
        return this.faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return Objects.equals(id, department.id) &&
               Objects.equals(name, department.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Department{" +
               "id = " + id +
               ", name = " + name + '\'' +
               ", faculty_id = " + (faculty != null ? faculty.getId() : null) +
               "}";
    }

}
