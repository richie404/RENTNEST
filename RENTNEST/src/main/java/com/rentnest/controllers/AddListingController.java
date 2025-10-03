package com.rentnest.controllers;

import com.rentnest.db.Database;
import com.rentnest.router.Router;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

public class AddListingController {

    @FXML private TextField nameField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextArea descriptionField;

    private String selectedImageUrl; // stored to listings.image_url

    @FXML
    public void initialize() {
        // Seed some types — tweak as you like
        typeComboBox.getItems().setAll("Flat", "Room", "Studio", "Office", "Parking");
    }

    public void handleUploadPhoto() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a photo");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File f = fc.showOpenDialog(null);
        if (f != null) {
            selectedImageUrl = f.toURI().toString(); // store URI as string
            info("Photo selected.");
        }
    }

    public void handleSubmitListing() {
        String name = val(nameField.getText());
        String location = val(locationField.getText());
        String priceText = val(priceField.getText());
        String type = typeComboBox.getValue();
        String description = val(descriptionField.getText());

        if (name.isEmpty() || location.isEmpty() || priceText.isEmpty()) {
            error("Please fill in name, location, and price.");
            return;
        }

        try (var conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO listings (name, location, price, description, `type`, image_url, status, owner_id) " +
                             "VALUES (?,?,?,?,?,?,'active',NULL)"
             )) {

            ps.setString(1, name);
            ps.setString(2, location);
            ps.setBigDecimal(3, new BigDecimal(priceText));
            ps.setString(4, description.isEmpty() ? null : description);
            ps.setString(5, (type == null || type.isBlank()) ? null : type);
            ps.setString(6, (selectedImageUrl == null || selectedImageUrl.isBlank()) ? null : selectedImageUrl);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                info("Listing created.");
                Router.go("listings"); // if your Router uses goToListings(), replace this line accordingly
            } else error("Insert failed.");

        } catch (Exception e) {
            e.printStackTrace();
            error("Error: " + e.getMessage());
        }
    }

    public void handleBack() {
        Router.go("listings"); // or Router.goToListings();
    }

    private String val(String s) { return s == null ? "" : s.trim(); }
    private void info(String m){ Alert a=new Alert(Alert.AlertType.INFORMATION); a.setHeaderText(null); a.setContentText(m); a.showAndWait();}
    private void error(String m){ Alert a=new Alert(Alert.AlertType.ERROR); a.setHeaderText(null); a.setContentText(m); a.showAndWait();}
}
