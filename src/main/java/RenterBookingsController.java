import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class RenterBookingsController {

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> colBookingId;
    @FXML private TableColumn<Booking, Integer> colListing;   // ✅ clearer name
    @FXML private TableColumn<Booking, Integer> colOwner;
    @FXML private TableColumn<Booking, java.sql.Date> colStart;
    @FXML private TableColumn<Booking, java.sql.Date> colEnd;
    @FXML private TableColumn<Booking, Double> colAmount;
    @FXML private TableColumn<Booking, String> colStatus;

    @FXML private Button btnCancel;
    @FXML private Button btnRefresh;

    private final BookingDAO bookingDAO = new BookingDAO();
    private int renterId; // ✅ local renterId (set by dashboard)

    /* -----------------------------------------------------------
       ✅ Initialization
       ----------------------------------------------------------- */
    @FXML
    private void initialize() {
        // ✅ Map table columns
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colListing.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /* -----------------------------------------------------------
       ✅ Called after login by RenterDashboardController
       ----------------------------------------------------------- */
    public void setRenterId(int renterId) {
        this.renterId = renterId;
        System.out.println("✅ [RenterBookingsController] renterId set: " + renterId);
        loadBookings();
    }

    /* -----------------------------------------------------------
       ✅ Load bookings for the logged-in renter
       ----------------------------------------------------------- */
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

    /* -----------------------------------------------------------
       🔹 Cancel booking
       ----------------------------------------------------------- */
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
            loadBookings(); // ✅ refresh
        } else {
            showAlert("Error", "Failed to cancel booking.");
        }
    }

    /* -----------------------------------------------------------
       🔹 Manual refresh
       ----------------------------------------------------------- */
    @FXML
    private void handleRefresh() {
        loadBookings();
    }

    /* -----------------------------------------------------------
       🔹 Utility alert helper
       ----------------------------------------------------------- */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
