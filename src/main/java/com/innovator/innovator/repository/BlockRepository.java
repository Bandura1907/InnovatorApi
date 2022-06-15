package com.innovator.innovator.repository;

import com.innovator.innovator.models.Blocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Blocks, Integer> {
}
