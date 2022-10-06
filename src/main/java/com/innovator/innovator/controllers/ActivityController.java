package com.innovator.innovator.controllers;

import com.innovator.innovator.models.Activity;
import com.innovator.innovator.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @GetMapping
    public ResponseEntity<?> getActivity(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int pageSize) {
        Map<String, Object> response = new HashMap<>();
        Page<Activity> pageTuts = activityRepository.findAll(PageRequest.of(page, pageSize,
                Sort.by(Sort.Direction.DESC, "id")));

        response.put("activity", pageTuts.getContent());
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
