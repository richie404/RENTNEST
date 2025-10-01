package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Temporary in-memory "user database"
    private static final Map<String, String> userDatabase = new HashMap<>();

    public static void registerUser(String username, String role) {
        userDatabase.put(username, role);
    }

    @FXML
    private void handleSubmitLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // not validated yet

        if (username == null || username.isEmpty()) {
            System.out.println("⚠ Username required!");
            return;
        }

        if (!userDatabase.containsKey(username)) {
            System.out.println("⚠ No account found for: " + username);
            return;
        }

        String role = userDatabase.get(username);

        // ✅ Redirect using specific Router methods
        switch (role) {
            case "Owner":
                Router.goToOwnerDashboard();
                break;
            case "Renter":
                Router.goToRenterDashboard();
                break;
            case "Admin":
                Router.goToAdminDashboard();
                break;
            default:
                System.out.println("⚠ Unknown role: " + role);
                Router.goToHomePage();
        }
    }

    @FXML
    private void handleBack() {
        Router.goToHomePage();
    }
}
