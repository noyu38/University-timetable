package com.noyu.timetable_backend.dto;

import java.time.DayOfWeek;
import java.util.Objects;

public class TimetableSlotDTO {

    private Long slotId;
    private DayOfWeek dayOfWeek;
    private int period;
    private CourseDTO course;

    public TimetableSlotDTO() {

    }

    public TimetableSlotDTO(Long slotId, DayOfWeek dayOfWeek, int period, CourseDTO course) {
        this.slotId = slotId;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.course = course;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        TimetableSlotDTO that = (TimetableSlotDTO) o;
        return Objects.equals(slotId, that.slotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotId);
    }
}
