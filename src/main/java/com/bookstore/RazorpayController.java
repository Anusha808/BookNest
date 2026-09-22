package com.bookstore.controller;

import com.bookstore.Notification;
import com.bookstore.NotificationRepository;
import com.bookstore.Order;
import com.bookstore.OrderRepository;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class RazorpayController {

    // =========================================================
    // RAZORPAY CONFIGURATION
    // =========================================================

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final OrderRepository orderRepository;

    private final NotificationRepository notificationRepository;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RazorpayController(
            OrderRepository orderRepository,
            NotificationRepository notificationRepository) {

        this.orderRepository =
                orderRepository;

        this.notificationRepository =
                notificationRepository;
    }

    // =========================================================
    // CREATE RAZORPAY ORDER
    // =========================================================

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody Map<String, Object> request) {

        try {

            Object amountObject =
                    request.get("amount");

            if (amountObject == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Amount is required"
                                )
                        );
            }

            double amount =
                    Double.parseDouble(
                            amountObject.toString()
                    );

            if (amount <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Invalid amount"
                                )
                        );
            }

            // Rupees -> Paise
            int amountInPaise =
                    (int) Math.round(
                            amount * 100
                    );

            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            razorpayKeyId,
                            razorpayKeySecret
                    );

            JSONObject orderRequest =
                    new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "BOOKNEST_" +
                            System.currentTimeMillis()
            );

            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(
                            orderRequest
                    );

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "orderId",
                    razorpayOrder.get("id")
            );

            response.put(
                    "amount",
                    amountInPaise
            );

            response.put(
                    "currency",
                    "INR"
            );

            response.put(
                    "keyId",
                    razorpayKeyId
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Unable to create Razorpay order"
                            )
                    );
        }
    }

    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    // =========================================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody Map<String, Object> request) {

        try {

            String razorpayOrderId =
                    String.valueOf(
                            request.get(
                                    "razorpay_order_id"
                            )
                    );

            String razorpayPaymentId =
                    String.valueOf(
                            request.get(
                                    "razorpay_payment_id"
                            )
                    );

            String razorpaySignature =
                    String.valueOf(
                            request.get(
                                    "razorpay_signature"
                            )
                    );

            String customerName =
                    String.valueOf(
                            request.get(
                                    "customerName"
                            )
                    );

            String customerEmail =
                    String.valueOf(
                            request.get(
                                    "customerEmail"
                            )
                    );

            double totalAmount =
                    Double.parseDouble(
                            String.valueOf(
                                    request.get(
                                            "amount"
                                    )
                            )
                    );

            // =================================================
            // VERIFY SIGNATURE
            // =================================================

            JSONObject options =
                    new JSONObject();

            options.put(
                    "razorpay_order_id",
                    razorpayOrderId
            );

            options.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );

            options.put(
                    "razorpay_signature",
                    razorpaySignature
            );

            boolean verified =
                    Utils.verifyPaymentSignature(
                            options,
                            razorpayKeySecret
                    );

            if (!verified) {

                return ResponseEntity
                        .status(
                                HttpStatus.BAD_REQUEST
                        )
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Payment verification failed"
                                )
                        );
            }

            // =================================================
            // SAVE RAZORPAY ORDER
            // =================================================

            Order order =
                    new Order();

            order.setCustomerName(
                    customerName
            );

            order.setCustomerEmail(
                    customerEmail
            );

            order.setTotalAmount(
                    totalAmount
            );

            order.setRazorpayOrderId(
                    razorpayOrderId
            );

            order.setRazorpayPaymentId(
                    razorpayPaymentId
            );

            order.setPaymentMethod(
                    "RAZORPAY"
            );

            order.setPaymentStatus(
                    "PAID"
            );

            order.setStatus(
                    "PROCESSING"
            );

            Order savedOrder =
                    orderRepository.save(
                            order
                    );

            // =================================================
            // RESPONSE
            // =================================================

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Payment successful"
            );

            response.put(
                    "booknestOrderId",
                    savedOrder.getId()
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Payment verification error"
                            )
                    );
        }
    }

    // =========================================================
    // CASH ON DELIVERY
    // =========================================================

    @PostMapping("/cod")
    public ResponseEntity<?> createCodOrder(
            @RequestBody Map<String, Object> request) {

        try {

            // =================================================
            // GET CUSTOMER DETAILS
            // =================================================

            String customerName =
                    String.valueOf(
                            request.get(
                                    "customerName"
                            )
                    );

            String customerEmail =
                    String.valueOf(
                            request.get(
                                    "customerEmail"
                            )
                    );

            Object amountObject =
                    request.get("amount");

            // =================================================
            // VALIDATION
            // =================================================

            if (customerName == null
                    || customerName.trim().isEmpty()
                    || customerName.equals("null")) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Customer name is required"
                                )
                        );
            }

            if (customerEmail == null
                    || customerEmail.trim().isEmpty()
                    || customerEmail.equals("null")) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Customer email is required"
                                )
                        );
            }

            if (amountObject == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Amount is required"
                                )
                        );
            }

            double totalAmount =
                    Double.parseDouble(
                            amountObject.toString()
                    );

            if (totalAmount <= 0) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,
                                        "message",
                                        "Invalid amount"
                                )
                        );
            }

            // =================================================
            // CREATE COD ORDER
            // =================================================

            Order order =
                    new Order();

            order.setCustomerName(
                    customerName
            );

            order.setCustomerEmail(
                    customerEmail
            );

            order.setTotalAmount(
                    totalAmount
            );

            order.setPaymentMethod(
                    "COD"
            );

            order.setPaymentStatus(
                    "COD_PENDING"
            );

            order.setStatus(
                    "PENDING"
            );

            // =================================================
            // SAVE ORDER
            // =================================================

            Order savedOrder =
                    orderRepository.save(
                            order
                    );

            // =================================================
            // CREATE ADMIN NOTIFICATION
            // =================================================

            Notification notification =
                    new Notification();

            notification.setTitle(
                    "New Cash on Delivery Order"
            );

            notification.setMessage(
                    "New COD order #" +
                    savedOrder.getId() +
                    " received from " +
                    customerName +
                    ". Order amount: ₹" +
                    String.format(
                            "%.2f",
                            totalAmount
                    ) +
                    ". Payment status: COD Pending."
            );

            notification.setType(
                    "ORDER"
            );

            notification.setRead(
                    false
            );

            notificationRepository.save(
                    notification
            );

            // =================================================
            // RESPONSE
            // =================================================

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "COD order placed successfully"
            );

            response.put(
                    "booknestOrderId",
                    savedOrder.getId()
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            Map.of(
                                    "success",
                                    false,
                                    "message",
                                    "Unable to place COD order"
                            )
                    );
        }
    }
}