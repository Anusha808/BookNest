package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/notifications")
public class AdminNotificationsController {

    private final NotificationRepository notificationRepository;

    public AdminNotificationsController(
            NotificationRepository notificationRepository) {

        this.notificationRepository =
                notificationRepository;
    }

    // =========================================================
    // SHOW NOTIFICATIONS
    // =========================================================

    @GetMapping
    public String notifications(Model model) {

        List<Notification> notifications =
                notificationRepository
                        .findAllByOrderByCreatedAtDesc();

        long unreadCount =
                notificationRepository.countByReadFalse();

        model.addAttribute(
                "notifications",
                notifications
        );

        model.addAttribute(
                "unreadCount",
                unreadCount
        );

        return "admin/notifications";
    }

    // =========================================================
    // MARK ONE NOTIFICATION AS READ
    // =========================================================

    @GetMapping("/read/{id}")
    public String markAsRead(
            @PathVariable Long id) {

        notificationRepository
                .findById(id)
                .ifPresent(notification -> {

                    notification.setRead(true);

                    notificationRepository.save(
                            notification
                    );
                });

        return "redirect:/admin/notifications";
    }

    // =========================================================
    // MARK ALL AS READ
    // =========================================================

    @GetMapping("/read-all")
    public String markAllAsRead() {

        List<Notification> notifications =
                notificationRepository.findAll();

        for (Notification notification :
                notifications) {

            notification.setRead(true);
        }

        notificationRepository.saveAll(
                notifications
        );

        return "redirect:/admin/notifications";
    }

    // =========================================================
    // DELETE NOTIFICATION
    // =========================================================

    @GetMapping("/delete/{id}")
    public String deleteNotification(
            @PathVariable Long id) {

        if (notificationRepository
                .existsById(id)) {

            notificationRepository
                    .deleteById(id);
        }

        return "redirect:/admin/notifications";
    }
}