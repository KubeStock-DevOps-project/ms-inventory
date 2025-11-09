package com.kubestock.inventory.repository;

import com.kubestock.inventory.model.ProductStock;
import com.kubestock.inventory.model.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {

    Optional<ProductStock> findBySku(String sku);

    boolean existsBySku(String sku);

    List<ProductStock> findByStatus(StockStatus status);

    @Query("SELECT p FROM ProductStock p WHERE p.quantity <= p.reorderLevel")
    List<ProductStock> findLowStockProducts();

    @Query("SELECT p FROM ProductStock p WHERE p.damagedQuantity > 0")
    List<ProductStock> findProductsWithDamagedStock();
}
