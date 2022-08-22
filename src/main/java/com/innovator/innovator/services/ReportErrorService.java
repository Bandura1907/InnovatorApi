package com.innovator.innovator.services;

import com.innovator.innovator.email.EmailSender;
import com.innovator.innovator.models.MessageEmail;
import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.repository.ReportErrorRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportErrorService {

    private ReportErrorRepository reportErrorRepository;
    private TemplateEngine templateEngine;
    private EmailSender emailSender;

    public List<ReportError> findAll() {
        return reportErrorRepository.findAll();
    }

    public ReportError findById(int id) {
        return reportErrorRepository.findById(id).get();
    }

    public void saveReport(ReportError reportError) {
        reportErrorRepository.save(reportError);
    }

    public void deleteReportById(int id) {
        reportErrorRepository.deleteById(id);
    }

    public void sendMail(MessageEmail messageEmail) {
        emailSender.send(messageEmail.getTo(), messageEmail.getSubject(), buildReportHtmlEmail(messageEmail.getText()));
    }

    private String buildReportHtmlEmail(String text) {
        Context context = new Context();
        context.setVariable("text", text);
        return templateEngine.process("report-email", context);
    }
}
