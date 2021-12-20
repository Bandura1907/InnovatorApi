package com.innovator.innovator.services;

import com.innovator.innovator.models.ReportError;
import com.innovator.innovator.repository.ReportErrorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReportErrorService {

    private ReportErrorRepository reportErrorRepository;

    public void saveReport(ReportError reportError) {
        reportErrorRepository.save(reportError);
    }
}
