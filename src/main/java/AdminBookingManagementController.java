import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;


public class AdminBookingManagementController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> colId;
    @FXML private TableColumn<Booking, Integer> colProperty;
    @FXML private TableColumn<Booking, Integer> colRenter;
    @FXML private TableColumn<Booking, Integer> colOwner;
    @FXML private TableColumn<Booking, String> colStart;
    @FXML private TableColumn<Booking, String> colEnd;
    @FXML private TableColumn<Booking, Double> colAmount;
    @FXML private TableColumn<Booking, String> colStatus;

    private final BookingDAO bookingDAO = new BookingDAO();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colProperty.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getListingId()).asObject());
        colRenter.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRenterId()).asObject());
        colOwner.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOwnerId()).asObject());
        colStart.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStartDate().toString()));

        colEnd.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEndDate().toString()));

        colAmount.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotalAmount()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        loadBookings();
    }

    private void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        bookingTable.setItems(FXCollections.observableArrayList(bookings));
    }
    @FXML
    private void handleCancel() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a booking first."); return; }

        if (bookingDAO.updateBookingStatus(selected.getId(), "CANCELLED")) {
            showAlert("Booking cancelled successfully.");
            loadBookings();
        }
    }

    @FXML
    private void handleRefresh() {
        loadBookings();
    }
    @FXML
    private void handleApprove() {
        // TODO: implement approval logic
        System.out.println("Approve clicked (Booking Management)");
    }

    @FXML
    private void handleReject() {
        System.out.println("Reject button clicked ❌");
        // TODO: Add logic here to update booking status to "REJECTED"
    }

    @FXML
    private void handleDelete() {
        System.out.println("Delete button clicked 🗑️");
        // TODO: Add logic here to remove the booking record
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
