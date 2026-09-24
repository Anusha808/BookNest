# 📚 BookNest – Online Bookstore Management System

BookNest is a web-based **Online Bookstore Management System** developed using Java and Spring Boot.

The system provides a platform where customers can browse books, search for books, add books to their cart, place orders, make payments, and manage their profiles.

It also provides an **Admin Dashboard** to manage books, categories, users, orders, payments, inventory, notifications, and store settings.

---

## 🎯 Project Objective

The main objective of BookNest is to develop a simple and user-friendly online bookstore platform that makes book purchasing easier for customers and provides administrators with an efficient system for managing bookstore operations.

---

## ✨ Features

### 👤 Customer Features

- User Registration
- User Login
- Browse Books
- Search Books
- Browse Books by Category
- View Book Details
- Add Books to Cart
- Update Cart Quantity
- Remove Books from Cart
- Checkout
- Razorpay Online Payment
- Cash on Delivery
- View My Orders
- View Order Details
- Manage User Profile
- Edit Profile Information
- Manage Location
- Order Status Tracking

### 🛠️ Admin Features

- Admin Dashboard
- Book Management
- Add New Books
- Edit Books
- Delete Books
- Category Management
- User Management
- Order Management
- Payment Management
- Inventory Management
- Stock Management
- Out-of-Stock Management
- Notifications Management
- Store Settings
- Revenue Statistics
- Order Statistics

---

## 💻 Technologies Used

### Backend

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- REST APIs

### Frontend

- HTML5
- CSS3
- JavaScript
- Thymeleaf
- Font Awesome

### Database

- MySQL 8.0

### Payment Gateway

- Razorpay Test Mode

### Development Tools

- Visual Studio Code
- Maven
- MySQL Workbench
- Git
- GitHub

---

## 🏗️ System Architecture

```text
              ┌──────────────────────┐
              │      Customer        │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │   Thymeleaf / HTML   │
              │      CSS / JS        │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │    Spring Boot       │
              │    Controllers       │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │   Service / JPA      │
              │     Hibernate        │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │       MySQL          │
              │   online_bookstore   │
              └──────────────────────┘

                         │
                         ▼
              ┌──────────────────────┐
              │      Razorpay        │
              │   Test Payment API   │
              └──────────────────────┘

