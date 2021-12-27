package com.innovator.innovator.controllers;

import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.models.User;
import com.innovator.innovator.services.ReportErrorService;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class ReportErrorController {

    private UserService userService;
    private ReportErrorService reportErrorService;

    @GetMapping("/all_reports")
    public ResponseEntity<List<ReportError>> allReports() {
        return ResponseEntity.ok(reportErrorService.findAll());
    }



    @PostMapping("/report_error/{clientId}")
    public ResponseEntity<Map<String, String>> reportError(@PathVariable int clientId, @RequestBody ReportError reportErrorBody) {
        User user = userService.findById(clientId);
        ReportError reportError = new ReportError();

        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        reportError.setCustomEmail(reportErrorBody.getCustomEmail());
        reportError.setUser(user);
        reportError.setMessageText(reportErrorBody.getMessageText());
        reportError.setStatus("Новый");

        reportErrorService.saveReport(reportError);

        return ResponseEntity.ok(Map.of("message", "Message sender"));
    }

    @DeleteMapping("/report_error_delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable int id) {
        reportErrorService.deleteReportById(id);
        return ResponseEntity.ok().build();
    }
}
