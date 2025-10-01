package com.rentnest.router;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Router {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    private static Scene loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            return new Scene(root, 1000, 700); // consistent size across app
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load FXML file: " + fxmlFile, e);
        }
    }

    // ========== PUBLIC NAVIGATION ==========

    public static void goToHomePage() {
        stage.setScene(loadScene("homepage.fxml"));
        stage.setTitle("RentNest - Home");
        stage.show();
    }


    public static void goToLogin() {
        stage.setScene(loadScene("login.fxml"));
        stage.setTitle("RentNest - Login");
        stage.show();
    }

    public static void goToRegister() {
        stage.setScene(loadScene("register.fxml"));
        stage.setTitle("RentNest - Register");
        stage.show();
    }

    public static void goToBrowse() {
        stage.setScene(loadScene("browse.fxml"));
        stage.setTitle("RentNest - Browse Properties");
        stage.show();
    }

    public static void goToListings() {
        stage.setScene(loadScene("listings.fxml"));
        stage.setTitle("RentNest - List Your Property");
        stage.show();
    }

    public static void goToDetails() {
        stage.setScene(loadScene("details.fxml"));
        stage.setTitle("RentNest - Property Details");
        stage.show();
    }

    // ========== ROLE-BASED NAVIGATION ==========

    public static void goToAdminDashboard() {
        stage.setScene(loadScene("AdminDashboard.fxml"));
        stage.setTitle("RentNest - Admin Dashboard");
        stage.show();
    }

    public static void goToOwnerDashboard() {
        stage.setScene(loadScene("OwnerDashboard.fxml"));
        stage.setTitle("RentNest - Home Owner Dashboard");
        stage.show();
    }

    public static void goToRenterDashboard() {
        stage.setScene(loadScene("RenterDashboard.fxml"));
        stage.setTitle("RentNest - Renter Dashboard");
        stage.show();
    }
}
