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
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;
import java.time.DayOfWeek;
import java.util.Objects;

@Entity

// テーブル名と複合ユニーク制約を設定することで、同じ時限に複数授業を登録できなくする
@Table(name = "timetable_slots", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "day_of_week", "period" })
})
public class TimetableSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING) // DayOfWeekは列挙型、Enumの値を文字列としてDBに保存
    @JoinColumn(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // 曜日（MONDAY, TUESDAY, …）

    @Column(nullable = false)
    private int period; // 時限(1, 2, 3, …)

    public TimetableSlot() {

    }

    public TimetableSlot(User user, Course course, DayOfWeek dayOfWeek, int period) {
        this.user = user;
        this.course = course;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TimetableSlot that = (TimetableSlot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "TimetableSlot{" +
                "id=" + id +
                ", user_id=" + (user != null ? user.getId() : null) +
                ", course?id=" + (course != null ? course.getId() : null) +
                ", dayOfWeek=" + dayOfWeek +
                ", period=" + period +
                '}';
    }
}
