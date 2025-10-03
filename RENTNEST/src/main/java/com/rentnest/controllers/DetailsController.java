package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class DetailsController {

    @FXML
    private void handleBack() {
        Router.goToBrowse();
    }
    @FXML private javafx.scene.control.Label nameLabel;
    @FXML private javafx.scene.control.Label locationLabel;
    @FXML private javafx.scene.control.Label priceLabel;
// (Optional) if your details.fxml has an ImageView:
// @FXML private javafx.scene.image.ImageView imageView;

    @FXML
    public void initialize() {
        Integer id = com.rentnest.state.SelectionState.get();
        if (id == null) {
            nameLabel.setText("No listing selected");
            locationLabel.setText("");
            priceLabel.setText("");
            return;
        }

        String sql = "SELECT name, location, price, image_url, description, `type`, status " +
                "FROM listings WHERE id = ?";

        try (var conn = com.rentnest.db.Database.getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    nameLabel.setText("Name: " + rs.getString("name"));
                    locationLabel.setText("Location: " + rs.getString("location"));
                    priceLabel.setText("Price: $" + rs.getBigDecimal("price"));

                    // Optional image
                /*
                String imageUrl = rs.getString("image_url");
                if (imageView != null && imageUrl != null && !imageUrl.isBlank()) {
                    imageView.setImage(new javafx.scene.image.Image(imageUrl, true));
                }
                */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
