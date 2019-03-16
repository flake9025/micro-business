package com.ecommerce.ecommerce.dao;

import com.ecommerce.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    Product findById(int id);

    List<Product> findByPrixGreaterThan(int prixLimit);

    List<Product> findByNomLike(String recherche);
}
