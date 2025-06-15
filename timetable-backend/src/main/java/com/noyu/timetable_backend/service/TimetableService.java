package com.noyu.timetable_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.dto.TimetableSlotDTO;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.model.TimetableSlot;
import com.noyu.timetable_backend.repository.TimetableSlotRepository;
import com.noyu.timetable_backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TimetableService {

    private final TimetableSlotRepository timetableSlotRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimetableService(TimetableSlotRepository timetableSlotRepository, UserRepository userRepository) {
        this.timetableSlotRepository = timetableSlotRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TimetableSlotDTO> getTimetableForUser(String username) {

        // ユーザー名でUserエンティティを取得
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません: " + username));

        // ユーザーIDを使って、そのユーザーの時間割スロットをすべて取得
        List<TimetableSlot> slots = timetableSlotRepository.findByUserId(user.getId());

        return slots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TimetableSlotDTO convertToDTO(TimetableSlot slot) {
        if (slot == null) {
            return null;
        }

        CourseDTO courseDTO = new CourseDTO(
                slot.getCourse().getId(),
                slot.getCourse().getName(),
                slot.getCourse().getRoom(),
                slot.getCourse().getTeacher(),
                (slot.getCourse().getDepartment() != null) ? slot.getCourse().getDepartment().getId() : null,
                (slot.getCourse().getDepartment() != null) ? slot.getCourse().getDepartment().getName() : "共通");

        return new TimetableSlotDTO(
                slot.getId(),
                slot.getDayOfWeek(),
                slot.getPeriod(),
                courseDTO);
    }
}
