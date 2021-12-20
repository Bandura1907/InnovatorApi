package com.innovator.innovator.repository;

import com.innovator.innovator.models.ReportError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportErrorRepository extends JpaRepository<ReportError, Integer> {
}
