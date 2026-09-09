package com.bookstore.controller;

import com.razorpay.Order;
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

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;


    // =========================================================
    // CREATE RAZORPAY ORDER
    // POST: /api/payment/create-order
    // =========================================================

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @RequestBody Map<String, Object> request) {

        try {

            // Get amount from checkout page
            Object amountObject = request.get("amount");

            if (amountObject == null) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message", "Amount is required"
                        )
                );
            }

            // Convert amount to rupees
            double amountInRupees =
                    Double.parseDouble(amountObject.toString());

            if (amountInRupees <= 0) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "success", false,
                                "message", "Invalid amount"
                        )
                );
            }

            // Razorpay requires amount in paise
            int amountInPaise =
                    (int) Math.round(amountInRupees * 100);

            // Create Razorpay client
            RazorpayClient razorpayClient =
                    new RazorpayClient(
                            razorpayKeyId,
                            razorpayKeySecret
                    );

            // Create order request
            JSONObject orderRequest = new JSONObject();

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
                    "BOOKNEST_" + System.currentTimeMillis()
            );

            // Create order in Razorpay
            Order order =
                    razorpayClient.orders.create(orderRequest);

            // Send response to checkout.html
            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "success",
                    true
            );

            response.put(
                    "orderId",
                    order.get("id")
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

            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    "Unable to create Razorpay order"
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    "Payment initialization failed"
                            )
                    );
        }
    }


    // =========================================================
    // VERIFY RAZORPAY PAYMENT
    // POST: /api/payment/verify
    // =========================================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody Map<String, String> paymentData) {

        try {

            String razorpayOrderId =
                    paymentData.get("razorpay_order_id");

            String razorpayPaymentId =
                    paymentData.get("razorpay_payment_id");

            String razorpaySignature =
                    paymentData.get("razorpay_signature");


            // Check required payment information
            if (razorpayOrderId == null ||
                    razorpayPaymentId == null ||
                    razorpaySignature == null) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "success",
                                        false,

                                        "message",
                                        "Payment information is incomplete"
                                )
                        );
            }


            // Create signature payload
            String payload =
                    razorpayOrderId
                            + "|"
                            + razorpayPaymentId;


            // Verify signature
            boolean verified =
                    Utils.verifySignature(
                            payload,
                            razorpaySignature,
                            razorpayKeySecret
                    );


            if (verified) {

                return ResponseEntity.ok(
                        Map.of(
                                "success",
                                true,

                                "message",
                                "Payment verified successfully",

                                "paymentId",
                                razorpayPaymentId,

                                "orderId",
                                razorpayOrderId
                        )
                );
            }


            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    "Payment verification failed"
                            )
                    );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success",
                                    false,

                                    "message",
                                    "Unable to verify payment"
                            )
                    );
        }
    }
}