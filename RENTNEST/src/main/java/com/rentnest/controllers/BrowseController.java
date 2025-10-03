package com.rentnest.controllers;

import com.rentnest.db.Database;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class BrowseController {

    @FXML private TilePane browseContainer;

    @FXML
    public void initialize() {
        try (var conn = Database.getConnection();
             var st = conn.createStatement();
             var rs = st.executeQuery(
                     "SELECT id, name, location, price, image_url FROM listings ORDER BY created_at DESC"
             )) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("name");
                String location = rs.getString("location");
                BigDecimal price = rs.getBigDecimal("price");
                String imageUrl = rs.getString("image_url");

                browseContainer.getChildren().add(
                        createPropertyCard(id, title, location, price, imageUrl)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Optional helper (kept for your code flow, now BigDecimal-based)
    private void loadProperties() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, location, price FROM listings")) {

            while (rs.next()) {
                String title = rs.getString("name");
                String location = rs.getString("location");
                BigDecimal price = rs.getBigDecimal("price");

                browseContainer.getChildren().add(
                        createPropertyCard(title, location, price)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- BigDecimal overloads (Option A) ----------------

    // (title, location, price)
    private VBox createPropertyCard(String title, String location, BigDecimal price) {
        return buildBaseCard(title, location, price, null);
    }

    // (id, title, location, price) → clickable
    private VBox createPropertyCard(int id, String title, String location, BigDecimal price) {
        var card = buildBaseCard(title, location, price, null);
        card.setUserData(id);
        card.setOnMouseClicked(e -> {
            com.rentnest.state.SelectionState.set(id);
            com.rentnest.router.Router.goToDetails(); // or Router.go("details")
        });
        return card;
    }

    // (title, location, price, imageUrl)
    private VBox createPropertyCard(String title, String location, BigDecimal price, String imageUrl) {
        return buildBaseCard(title, location, price, imageUrl);
    }

    // (id, title, location, price, imageUrl) → clickable
    private VBox createPropertyCard(int id, String title, String location, BigDecimal price, String imageUrl) {
        var card = buildBaseCard(title, location, price, imageUrl);
        card.setUserData(id);
        card.setOnMouseClicked(e -> {
            com.rentnest.state.SelectionState.set(id);
            com.rentnest.router.Router.goToDetails();
        });
        return card;
    }

    // Shared renderer
    private VBox buildBaseCard(String title, String location, BigDecimal price, String imageUrl) {
        var card = new VBox(6);
        card.getStyleClass().add("property-card");

        var imageView = new ImageView();
        imageView.setFitWidth(240);
        imageView.setFitHeight(160);
        imageView.setPreserveRatio(true);

        try {
            Image img = (imageUrl == null || imageUrl.isBlank())
                    ? new Image("https://via.placeholder.com/240x160.png?text=RentNest", true)
                    : new Image(imageUrl, true);
            imageView.setImage(img);
        } catch (Exception ignored) {}

        var nameLabel = new Label(title);
        nameLabel.getStyleClass().add("property-title");

        var locationLabel = new Label(location);
        locationLabel.getStyleClass().add("property-location");

        String priceText = (price == null) ? "—" : price.stripTrailingZeros().toPlainString();
        var priceLabel = new Label("$" + priceText + " / month");
        priceLabel.getStyleClass().add("property-price");

        card.getChildren().addAll(imageView, nameLabel, locationLabel, priceLabel);
        return card;
    }
}
