package com.innovator.innovator.controllers;

import com.innovator.innovator.models.MessageEmail;
import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.models.User;
import com.innovator.innovator.services.ReportErrorService;
import com.innovator.innovator.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Slf4j
public class ReportErrorController {

    private UserService userService;
    private ReportErrorService reportErrorService;

    @GetMapping("/all_reports")
    public ResponseEntity<List<ReportError>> allReports() {
        return ResponseEntity.ok(reportErrorService.findAll());
    }

    @PostMapping("/report/sendMail")
    public ResponseEntity<Map<String, Object>> sendMail(@RequestBody @Valid MessageEmail messageEmail) {
        try {
            reportErrorService.sendMail(messageEmail);

            return ResponseEntity.ok(Map.of("status", "ok"));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return new ResponseEntity<>(Map.of("status", "failed send message"), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/report_by_id/{id}")
    public ResponseEntity<ReportError> reportById(@PathVariable int id) {
        ReportError reportError = reportErrorService.findById(id);
        return reportError == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(reportError);
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

    @PutMapping("/solve_the_problem/{id}")
    public ResponseEntity<ReportError> solveTheProblem(@PathVariable int id) {
        ReportError reportError = reportErrorService.findById(id);

        reportError.setClosedDate(LocalDateTime.now());
        reportError.setStatus("Решено");
        reportErrorService.saveReport(reportError);

        return ResponseEntity.ok(reportError);
    }

    @DeleteMapping("/report_error_delete/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable int id) {
        reportErrorService.deleteReportById(id);
        return ResponseEntity.ok().build();
    }
}
