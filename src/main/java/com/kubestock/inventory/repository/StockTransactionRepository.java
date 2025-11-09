package com.kubestock.inventory.repository;

import com.kubestock.inventory.model.StockTransaction;
import com.kubestock.inventory.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {

    List<StockTransaction> findByProductStockId(Long productStockId);

    List<StockTransaction> findByTransactionType(TransactionType transactionType);

    @Query("SELECT st FROM StockTransaction st WHERE st.productStock.id = :productStockId ORDER BY st.transactionDate DESC")
    List<StockTransaction> findRecentTransactionsByProductStockId(@Param("productStockId") Long productStockId);

    @Query("SELECT st FROM StockTransaction st WHERE st.transactionDate BETWEEN :startDate AND :endDate ORDER BY st.transactionDate DESC")
    List<StockTransaction> findTransactionsBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
