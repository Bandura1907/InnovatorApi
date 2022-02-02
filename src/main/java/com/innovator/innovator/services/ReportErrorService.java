package com.innovator.innovator.services;

import com.innovator.innovator.models.MessageEmail;
import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.repository.ReportErrorRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportErrorService {

    private ReportErrorRepository reportErrorRepository;
    private JavaMailSender emailSender;

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

    @Async
    public void sendMail(MessageEmail messageEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(messageEmail.getTo());
        message.setSubject(messageEmail.getSubject());
        message.setText(messageEmail.getText());

        emailSender.send(message);
    }
}
