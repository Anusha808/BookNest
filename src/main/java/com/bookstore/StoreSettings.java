package com.bookstore;

import jakarta.persistence.*;

@Entity
@Table(name = "store_settings")
public class StoreSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store Information
    @Column(nullable = false)
    private String storeName = "BookNest";

    private String storeEmail;

    private String storePhone;

    private String storeAddress;

    // Store Configuration
    private String currency = "INR";

    @Column(nullable = false)
    private boolean storeOpen = true;

    // Payment Settings
    @Column(nullable = false)
    private boolean razorpayEnabled = true;

    @Column(nullable = false)
    private boolean codEnabled = true;

    // Inventory
    @Column(nullable = false)
    private int lowStockThreshold = 10;

    public StoreSettings() {
    }

    // =========================
    // GETTERS AND SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isStoreOpen() {
        return storeOpen;
    }

    public void setStoreOpen(boolean storeOpen) {
        this.storeOpen = storeOpen;
    }

    public boolean isRazorpayEnabled() {
        return razorpayEnabled;
    }

    public void setRazorpayEnabled(boolean razorpayEnabled) {
        this.razorpayEnabled = razorpayEnabled;
    }

    public boolean isCodEnabled() {
        return codEnabled;
    }

    public void setCodEnabled(boolean codEnabled) {
        this.codEnabled = codEnabled;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }
}