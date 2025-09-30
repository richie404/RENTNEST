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
            // Note: fxml files are in resources/views/
            FXMLLoader loader = new FXMLLoader(Router.class.getResource("/views/" + fxmlFile));
            Parent root = loader.load();
            return new Scene(root, 900, 600); // Fixed size window
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load FXML file: " + fxmlFile, e);
        }
    }

    // Navigation methods
    public static void goToIndex() {
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
}
