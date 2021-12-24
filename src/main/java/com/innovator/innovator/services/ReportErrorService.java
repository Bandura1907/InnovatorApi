package com.innovator.innovator.services;

import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.repository.ReportErrorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportErrorService {

    private ReportErrorRepository reportErrorRepository;

    public List<ReportError> findAll() {
        return reportErrorRepository.findAll();
    }

    public void saveReport(ReportError reportError) {
        reportErrorRepository.save(reportError);
    }
}
