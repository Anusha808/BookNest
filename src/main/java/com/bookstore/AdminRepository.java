package com.bookstore.repository;

import com.bookstore.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByEmail(String email);
}