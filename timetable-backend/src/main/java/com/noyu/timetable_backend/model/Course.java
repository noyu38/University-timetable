package com.noyu.timetable_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // 授業名

    @Column(length = 50)
    private String room; // 教室名

    @Column(length = 50)
    private String teacher; // 教員名

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseCategory category;

    // 多くの授業が1つの学科に所属するため@ManyToOneを使用する
    // nullable = trueにすることで、全額共通科目など特定の学科に紐づかない授業も登録可能にする
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    public Course() {

    }

    public Course(String name, String room, String teacher, CourseCategory category, Department department) {
        this.name = name;
        this.room = room;
        this.teacher = teacher;
        this.category = category;
        this.department = department;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacher() {
        return teacher;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public Department getDepartment() {
        return department;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room='" + room + '\'' +
                ", teacher='" + teacher + '\'' +
                ", category=" + category +
                ", department_id=" + (department != null ? department.getId() : null) +
                '}';
    }
}
