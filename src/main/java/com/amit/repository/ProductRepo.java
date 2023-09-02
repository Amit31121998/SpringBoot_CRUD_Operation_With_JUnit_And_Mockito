package com.amit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amit.entity.ProductEntity;

public interface ProductRepo extends JpaRepository<ProductEntity, Integer> {

}
