package com.rentnest.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.rentnest.router.Router;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // ✅ Only Owner & Renter selectable
        roleComboBox.getItems().addAll("Owner", "Renter");
    }

    @FXML
    private void handleSubmitRegister() {
        String name = nameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (!password.equals(confirmPassword)) {
            System.out.println("❌ Passwords do not match!");
            return;
        }

        if (role == null || role.isEmpty()) {
            System.out.println("❌ Please select a role!");
            return;
        }

        // ✅ Save user in temporary in-memory DB
        LoginController.registerUser(username, role);

        System.out.println("✅ Registered: " + username + " as " + role);

        // Go to Login page after success
        Router.goToLogin();
    }

    @FXML
    private void handleBack() {
        Router.goToHomePage();
    }
}
