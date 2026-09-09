package com.bookstore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminSettingsController {

    private final StoreSettingsRepository settingsRepository;

    public AdminSettingsController(
            StoreSettingsRepository settingsRepository) {

        this.settingsRepository = settingsRepository;
    }


    // ==========================================================
    // SETTINGS PAGE
    // ==========================================================

    @GetMapping("/admin/settings")
    public String settings(Model model) {

        StoreSettings settings;

        if (settingsRepository.count() == 0) {

            settings = new StoreSettings();

            settings.setStoreName("BookNest");
            settings.setStoreEmail("admin@booknest.com");
            settings.setStorePhone("");
            settings.setStoreAddress("");
            settings.setCurrency("INR");
            settings.setStoreOpen(true);
            settings.setRazorpayEnabled(true);
            settings.setCodEnabled(true);
            settings.setLowStockThreshold(10);

            settings = settingsRepository.save(settings);

        } else {

            settings = settingsRepository.findAll()
                    .get(0);
        }

        model.addAttribute("settings", settings);

        return "admin/settings";
    }


    // ==========================================================
    // SAVE SETTINGS
    // ==========================================================

    @PostMapping("/admin/settings/save")
    public String saveSettings(

            @RequestParam String storeName,

            @RequestParam(required = false)
            String storeEmail,

            @RequestParam(required = false)
            String storePhone,

            @RequestParam(required = false)
            String storeAddress,

            @RequestParam String currency,

            @RequestParam(defaultValue = "false")
            boolean storeOpen,

            @RequestParam(defaultValue = "false")
            boolean razorpayEnabled,

            @RequestParam(defaultValue = "false")
            boolean codEnabled,

            @RequestParam int lowStockThreshold) {


        StoreSettings settings;

        if (settingsRepository.count() == 0) {

            settings = new StoreSettings();

        } else {

            settings = settingsRepository.findAll()
                    .get(0);
        }


        // Store Information

        settings.setStoreName(storeName.trim());

        settings.setStoreEmail(
                storeEmail != null
                        ? storeEmail.trim()
                        : ""
        );

        settings.setStorePhone(
                storePhone != null
                        ? storePhone.trim()
                        : ""
        );

        settings.setStoreAddress(
                storeAddress != null
                        ? storeAddress.trim()
                        : ""
        );


        // Store Configuration

        settings.setCurrency(currency);

        settings.setStoreOpen(storeOpen);


        // Payment Configuration

        settings.setRazorpayEnabled(
                razorpayEnabled
        );

        settings.setCodEnabled(
                codEnabled
        );


        // Inventory

        if (lowStockThreshold < 0) {
            lowStockThreshold = 0;
        }

        settings.setLowStockThreshold(
                lowStockThreshold
        );


        settingsRepository.save(settings);


        return "redirect:/admin/settings?saved=true";
    }
}