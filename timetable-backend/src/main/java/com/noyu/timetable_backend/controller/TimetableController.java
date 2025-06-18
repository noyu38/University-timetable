package com.noyu.timetable_backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.AddTimetableSlotRequestDTO;
import com.noyu.timetable_backend.dto.TimetableSlotDTO;
import com.noyu.timetable_backend.service.TimetableService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private static final Logger logger = LoggerFactory.getLogger(TimetableController.class);

    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public ResponseEntity<List<TimetableSlotDTO>> getMyTimetable(@AuthenticationPrincipal UserDetails currentUser) {

        String username = currentUser.getUsername();
        List<TimetableSlotDTO> timetable = timetableService.getTimetableForUser(username);

        return ResponseEntity.ok(timetable);
    }

    @PostMapping
    public ResponseEntity<?> addTimetableSlot(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody AddTimetableSlotRequestDTO requestDTO) {
        try {
            String username = currentUser.getUsername();
            TimetableSlotDTO newSlot = timetableService.addSlotToTimetable(username, requestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newSlot);
        } catch (EntityNotFoundException e) {
            // ユーザーまたは授業が見つからなかった場合
            logger.error("エンティティが見つかりません: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            // (すでにコマが埋まっている場合)
            logger.error("授業がすでに登録されています: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("指定された時間にはすでに授業が登録されています。");
        } catch (Exception e) {
            // その他のエラー
            logger.error("予期せぬエラーが発生しました: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<?> deleteTimetableSlot(
            @AuthenticationPrincipal UserDetails currentUser,
            @PathVariable Long slotId) {
        try {
            String username = currentUser.getUsername();
            timetableService.deleteSlotFromTimetable(username, slotId);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.error("エンティティが見つかりません: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            logger.warn("アクセスが拒否されました: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("予期せぬエラーが発生しました: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
