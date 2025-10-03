package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class OwnerDashboardController {

    @FXML
    private ListView<String> propertyList;

    @FXML
    public void initialize() {
        // Temporary dummy data with Bangladesh context
        propertyList.getItems().addAll(
                "🏠 2BHK Apartment - Dhanmondi, Dhaka",
                "🏠 Single Room - Chawkbazar, Chattogram",
                "🏠 Flat - Sylhet Sadar",
                "🏠 Office Space - Motijheel, Dhaka",
                "🏠 Storehouse - Khulna"
        );
    }

    @FXML
    private void handleAddListing() {
        // Navigate to Listings page
        Router.goToListings();
    }

    @FXML
    private void handleCalendar() {
        System.out.println("📅 Availability Calendar feature coming soon...");
        // Future: open AvailabilityCalendar.fxml
    }

    @FXML
    private void handleLogout() {
        Router.goToLogin();
    }
}
