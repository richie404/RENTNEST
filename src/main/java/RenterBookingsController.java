import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class RenterBookingsController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> colBookingId;
    @FXML private TableColumn<Booking, Integer> colListing;
    @FXML private TableColumn<Booking, Integer> colOwner;
    @FXML private TableColumn<Booking, String> colProperty;
    @FXML private TableColumn<Booking, String> colLocation;
    @FXML private TableColumn<Booking, java.sql.Date> colStart;
    @FXML private TableColumn<Booking, java.sql.Date> colEnd;
    @FXML private TableColumn<Booking, Double> colAmount;
    @FXML private TableColumn<Booking, String> colStatus;

    @FXML private Button btnCancel;
    @FXML private Button btnRefresh;

    private final BookingDAO bookingDAO = new BookingDAO();
    private int renterId;

    @FXML
    private void initialize() {
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colListing.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colProperty.setCellValueFactory(new PropertyValueFactory<>("listingName"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
        System.out.println("✅ [RenterBookingsController] renterId set: " + renterId);
        loadBookings();
    }

    private void loadBookings() {
        if (renterId <= 0) {
            System.out.println("⚠️ No renterId set — skipping load.");
            return;
        }

        try {
            ObservableList<Booking> bookings =
                    FXCollections.observableArrayList(bookingDAO.getBookingsByRenter(renterId));
            bookingTable.setItems(bookings);
            System.out.println("✅ Loaded " + bookings.size() + " bookings for renter " + renterId);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load bookings: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a booking to cancel.");
            return;
        }

        String status = selected.getStatus();
        if ("CANCELLED_BY_RENTER".equalsIgnoreCase(status)) {
            showAlert("Info", "You already cancelled this booking.");
            return;
        }

        if ("CONFIRMED".equalsIgnoreCase(status)) {
            showAlert("Info", "Confirmed bookings cannot be cancelled directly.");
            return;
        }

        boolean success = bookingDAO.cancelBooking(selected.getId());
        if (success) {
            showAlert("Cancelled", "Your booking has been cancelled successfully.");
            loadBookings();
        } else {
            showAlert("Error", "Failed to cancel booking.");
        }
    }

    @FXML
    private void handleRefresh() {
        loadBookings();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
