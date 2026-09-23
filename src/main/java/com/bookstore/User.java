package com.bookstore;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String role = "USER";

    private String location;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public User() {
    }


    // =========================================================
    // ID
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // =========================================================
    // NAME
    // =========================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    // =========================================================
    // EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================================================
    // PASSWORD
    // =========================================================

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // =========================================================
    // ROLE
    // =========================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    // =========================================================
    // LOCATION
    // =========================================================

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}