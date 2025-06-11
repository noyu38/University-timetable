package com.noyu.timetable_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.noyu.timetable_backend.model.TimetableSlot;

@Repository
public interface TimetableSlotRepository extends JpaRepository<TimetableSlot, Long> {

    List<TimetableSlot> findByUserId(Long userId);

    List<TimetableSlot> findByUserIdAndDayOfWeekOrderByPeriodAsc(Long userId, java.time.DayOfWeek dayOfWeek);
}
