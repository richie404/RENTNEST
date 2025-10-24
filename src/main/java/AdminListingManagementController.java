import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class AdminListingManagementController {

    @FXML private TableView<Listing> listingTable;
    @FXML private TableColumn<Listing, String> colTitle;
    @FXML private TableColumn<Listing, String> colLocation;
    @FXML private TableColumn<Listing, Double> colPrice;
    @FXML private TableColumn<Listing, Integer> colOwner;
    @FXML private TableColumn<Listing, String> colStatus;
    @FXML private Button btnApprove, btnReject, btnRefresh;

    private final AdminDAO adminDAO = new AdminDAO();

    @FXML
    private void initialize() {
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colLocation.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPricePerMonth()).asObject());
        colOwner.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getOwnerId()).asObject());
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        loadListings();
    }

    private void loadListings() {
        List<Listing> listings = adminDAO.getAllListings();
        listingTable.setItems(FXCollections.observableArrayList(listings));
    }

    @FXML
    private void handleApprove() {
        Listing selected = listingTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a listing first"); return; }

        if (adminDAO.updateListingStatus(selected.getId(), "APPROVED")) {
            showAlert("✅ Listing Approved!");
            loadListings();
        }
    }

    @FXML
    private void handleViewDetails() {
        // TODO: implement property details view popup
        System.out.println("View details clicked (Listing Management)");
    }

    @FXML
    private void handleReject() {
        Listing selected = listingTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a listing first"); return; }

        if (adminDAO.updateListingStatus(selected.getId(), "REJECTED")) {
            showAlert("❌ Listing Rejected!");
            loadListings();
        }
    }

    @FXML
    private void handleRefresh() {
        loadListings();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
