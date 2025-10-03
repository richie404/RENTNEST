package com.rentnest.controllers;

import com.rentnest.db.Database;
import com.rentnest.models.Listing;
import com.rentnest.router.Router;
import com.rentnest.state.SelectionState;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.ResultSet;

public class ListingsController {

    @FXML private TableView<Listing> table;
    @FXML private TableColumn<Listing, String> colName;
    @FXML private TableColumn<Listing, String> colLocation;
    @FXML private TableColumn<Listing, BigDecimal> colPrice;
    @FXML private TextField filterField;

    private final ObservableList<Listing> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        colLocation.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLocation()));
        colPrice.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrice()));
        table.setItems(data);

        loadData(null);

        filterField.textProperty().addListener((obs, o, n) -> {
            loadData((n == null || n.isBlank()) ? null : n.trim());
        });
    }

    private void loadData(String filter) {
        data.clear();
        String sql = "SELECT id, name, location, price FROM listings ";
        if (filter != null) sql += "WHERE name LIKE ? OR location LIKE ? ";
        sql += "ORDER BY created_at DESC";

        try (var conn = Database.getConnection();
             var ps = conn.prepareStatement(sql)) {

            if (filter != null) {
                String like = "%" + filter + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.add(new Listing(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getBigDecimal("price")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            info("Failed to load listings: " + e.getMessage());
        }
    }

    public void handleAdd() { Router.go("AddListing"); } // or Router.goToAddListing();

    public void handleView() {
        var sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { info("Select a row first."); return; }
        SelectionState.set(sel.getId());
        Router.go("details"); // or Router.goToDetails();
    }

    public void handleDelete() {
        var sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { info("Select a row first."); return; }

        try (var conn = Database.getConnection();
             var ps = conn.prepareStatement("DELETE FROM listings WHERE id=?")) {
            ps.setInt(1, sel.getId());
            if (ps.executeUpdate() > 0) { data.remove(sel); info("Deleted."); }
            else info("Delete failed.");
        } catch (Exception e) {
            e.printStackTrace();
            info("Error: " + e.getMessage());
        }
    }

    public void handleBack() { Router.go("browse"); } // or Router.goToBrowse();

    private void info(String m) {
        var a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}
