package com.satvik.inventory.repository;

import com.satvik.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);

    // JPQL: find all low stock products
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= p.lowStockThreshold")
    List<Product> findLowStockProducts();

    // JPQL: find out of stock products
    @Query("SELECT p FROM Product p WHERE p.stockQuantity = 0")
    List<Product> findOutOfStockProducts();

    // JPQL: total inventory value
    @Query("SELECT SUM(p.price * p.stockQuantity) FROM Product p")
    java.math.BigDecimal getTotalInventoryValue();

    // JPQL: products by category with stock info
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.stockQuantity > 0")
    List<Product> findInStockByCategoryId(@Param("categoryId") Long categoryId);

    boolean existsBySku(String sku);
}
