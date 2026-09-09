package com.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findTop5ByOrderByOrderDateDesc();

    long countByStatus(String status);

    long countByPaymentStatus(String paymentStatus);

    @Query("""
           SELECT COALESCE(SUM(o.totalAmount), 0)
           FROM Order o
           WHERE o.paymentStatus = 'PAID'
              OR o.paymentStatus = 'COD_PAID'
           """)
    Double getTotalRevenue();

    @Query("""
           SELECT COUNT(o)
           FROM Order o
           WHERE o.paymentStatus = 'COD_PENDING'
              OR o.paymentStatus = 'COD_PAID'
           """)
    long countCodOrders();

    @Query("""
           SELECT COUNT(o)
           FROM Order o
           WHERE o.paymentStatus = 'COD_PENDING'
           """)
    long countCodPending();

    @Query("""
           SELECT COUNT(o)
           FROM Order o
           WHERE o.paymentStatus = 'COD_PAID'
           """)
    long countCodPaid();
}