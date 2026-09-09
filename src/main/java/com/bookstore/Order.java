package com.bookstore;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ==========================================================
    // CUSTOMER DETAILS
    // ==========================================================

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;


    // ==========================================================
    // ORDER AMOUNT
    // ==========================================================

    @Column(nullable = false)
    private Double totalAmount;


    // ==========================================================
    // PAYMENT DETAILS
    // ==========================================================

    // RAZORPAY ORDER ID
    private String razorpayOrderId;

    // RAZORPAY PAYMENT ID
    private String razorpayPaymentId;

    // RAZORPAY / COD
    @Column(nullable = false)
    private String paymentMethod = "RAZORPAY";

    /*
     * Possible values:
     *
     * PAID        -> Razorpay successful payment
     * FAILED      -> Razorpay payment failed
     * COD_PENDING -> Cash on Delivery, not collected
     * COD_PAID    -> Cash on Delivery collected
     */
    @Column(nullable = false)
    private String paymentStatus = "PENDING";


    // ==========================================================
    // ORDER STATUS
    // ==========================================================

    /*
     * Possible values:
     *
     * PENDING
     * PROCESSING
     * SHIPPED
     * COMPLETED
     * CANCELLED
     */
    @Column(nullable = false)
    private String status = "PENDING";


    // ==========================================================
    // ORDER DATE
    // ==========================================================

    @Column(nullable = false)
    private LocalDateTime orderDate;


    // ==========================================================
    // CONSTRUCTOR
    // ==========================================================

    public Order() {
        this.orderDate = LocalDateTime.now();
    }


    // ==========================================================
    // GETTERS AND SETTERS
    // ==========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }


    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }


    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}