package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class AdminDashboardController {

    @FXML
    private void handleLogout() {
        Router.goToLogin();
    }

    @FXML
    private void handleApproveOwners() {
        System.out.println("✅ Approve Owners clicked!");
    }

    @FXML
    private void handleManageRenters() {
        System.out.println("👥 Manage Renters clicked!");
    }

    @FXML
    private void handleApproveListings() {
        System.out.println("🏠 Approve Listings clicked!");
    }

    @FXML
    private void handleFlaggedListings() {
        System.out.println("🚩 Flagged Listings clicked!");
    }

    @FXML
    private void handleRevenueReports() {
        System.out.println("💰 Revenue Reports clicked!");
    }

    @FXML
    private void handleUserActivityLogs() {
        System.out.println("📊 User Activity Logs clicked!");
    }
}
