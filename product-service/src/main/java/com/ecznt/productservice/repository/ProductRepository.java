package com.ecznt.productservice.repository;


import com.ecznt.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



public interface ProductRepository extends JpaRepository<Product, String> {
}
