package com.noyu.timetable_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.TimetableSlotDTO;
import com.noyu.timetable_backend.service.TimetableService;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

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
}
