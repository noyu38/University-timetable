package com.noyu.timetable_backend.dto;

import java.time.DayOfWeek;

public class AddTimetableSlotRequestDTO {

    private Long courseId;
    private DayOfWeek dayOfWeek;
    private int period;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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
}
