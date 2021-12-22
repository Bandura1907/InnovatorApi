package com.innovator.innovator.controllers;

import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.models.User;
import com.innovator.innovator.services.ReportErrorService;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class ReportErrorController {

    private UserService userService;
    private ReportErrorService reportErrorService;

    @PostMapping("/report_error/{clientId}")
    public ResponseEntity<Map<String, String>> reportError(@PathVariable int clientId, @RequestBody ReportError reportErrorBody) {
        User user = userService.findById(clientId);
        ReportError reportError = new ReportError();

        reportError.setCustomEmail(reportErrorBody.getCustomEmail());
        reportError.setUser(user);

        reportErrorService.saveReport(reportError);

        return ResponseEntity.ok(Map.of("message", "Message sender"));
    }
}
