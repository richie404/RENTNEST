package com.rentnest.controllers;

import com.rentnest.router.Router;
import com.rentnest.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RenterDashboardController {
    @FXML private ListView<String> favoritesList;

    @FXML
    private void initialize() { loadFavoritesOrRecent(); }

    @FXML private void handleLogout()   { Router.goToLogin(); }
    @FXML private void handleBrowse()   { Router.goToBrowse(); }
    @FXML private void handleFavorites(){ new Alert(Alert.AlertType.INFORMATION,
            "Favorites screen coming soon. Showing recent listings for now.").showAndWait(); }
    @FXML private void handleBookings() { new Alert(Alert.AlertType.INFORMATION,
            "Booking requests screen coming soon.").showAndWait(); }

    private void loadFavoritesOrRecent() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String sql = """
                SELECT name, location, price
                FROM listings
                ORDER BY created_at DESC
                LIMIT 8
                """;
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                String loc  = rs.getString("location");
                BigDecimal price = rs.getBigDecimal("price");
                String p = price==null ? "—" : price.stripTrailingZeros().toPlainString();
                items.add(String.format("%s — %s — $%s/mo", name, loc, p));
            }
        } catch (Exception ex) {
            items.add("Couldn’t reach the database. Start MySQL & check db.properties.");
        }
        if (items.isEmpty()) items.add("No data yet. Add listings or mark favorites!");
        favoritesList.setItems(items);
    }
}
