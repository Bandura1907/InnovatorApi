package com.innovator.innovator.repository;

import com.innovator.innovator.models.Useful;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsefulRepository extends JpaRepository<Useful, Integer> {
}
