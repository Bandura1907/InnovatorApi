package com.innovator.innovator.repository;

import com.innovator.innovator.models.EType;
import com.innovator.innovator.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
    Products findByType(EType type);
    Boolean existsByType(EType type);
}
