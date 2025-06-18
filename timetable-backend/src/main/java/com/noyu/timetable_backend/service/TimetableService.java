package com.noyu.timetable_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import com.noyu.timetable_backend.dto.AddTimetableSlotRequestDTO;
import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.dto.TimetableSlotDTO;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.model.Course;
import com.noyu.timetable_backend.model.TimetableSlot;
import com.noyu.timetable_backend.repository.CourseRepository;
import com.noyu.timetable_backend.repository.TimetableSlotRepository;
import com.noyu.timetable_backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TimetableService {

        private final TimetableSlotRepository timetableSlotRepository;
        private final UserRepository userRepository;
        private final CourseRepository courseRepository;

        @Autowired
        public TimetableService(TimetableSlotRepository timetableSlotRepository, UserRepository userRepository,
                        CourseRepository courseRepository) {
                this.timetableSlotRepository = timetableSlotRepository;
                this.userRepository = userRepository;
                this.courseRepository = courseRepository;
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
                                (slot.getCourse().getDepartment() != null) ? slot.getCourse().getDepartment().getId()
                                                : null,
                                (slot.getCourse().getDepartment() != null) ? slot.getCourse().getDepartment().getName()
                                                : "共通");

                return new TimetableSlotDTO(
                                slot.getId(),
                                slot.getDayOfWeek(),
                                slot.getPeriod(),
                                courseDTO);
        }

        @Transactional
        public TimetableSlotDTO addSlotToTimetable(String username, AddTimetableSlotRequestDTO requestDTO) {

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "ユーザーが見つかりません: " + requestDTO.getCourseId()));

                Course course = courseRepository.findById(requestDTO.getCourseId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "授業が見つかりません ID: " + requestDTO.getCourseId()));

                TimetableSlot newSlot = new TimetableSlot();
                newSlot.setUser(user);
                newSlot.setCourse(course);
                newSlot.setDayOfWeek(requestDTO.getDayOfWeek());
                newSlot.setPeriod(requestDTO.getPeriod());

                TimetableSlot savedSlot = timetableSlotRepository.save(newSlot);

                return convertToDTO(savedSlot);
        }

        @Transactional
        public void deleteSlotFromTimetable(String username, Long slotId) {
                // 削除対象のスロットIDでTimetableSlotエンティティを取得
                TimetableSlot slot = timetableSlotRepository.findById(slotId)
                                .orElseThrow(() -> new EntityNotFoundException("指定された授業が見つかりません ID: " + slotId));

                // 指定されたコマが、リクエストを送信してきたユーザーのものであるか確認
                if (!slot.getUser().getUsername().equals(username)) {
                        throw new AccessDeniedException("この時間割のコマを削除する権限がありません。");
                }

                timetableSlotRepository.delete(slot);
        }
}
