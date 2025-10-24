import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class RenterDashboardController extends BaseController {

    @FXML private ListView<Listing> favoritesList;
    @FXML private ListView<Message> messageList;
    @FXML private Button viewChatButton;

    // ✅ FXML injects VBox, not controller
    @FXML private VBox renterBookingsSection;

    private RenterBookingsController renterBookingsController;
    private final ListingDAO listingDAO = new ListingDAO();
    private final MessageDAO messageDAO = new MessageDAO();
    private int renterId;

    /* -----------------------------------------------------------
       🏠 Initialize dashboard
       ----------------------------------------------------------- */
    @FXML
    public void initialize() {
        if (!requireRole("RENTER")) return;

        User user = currentUser();
        if (user != null) {
            renterId = user.getId();
        }

        favoritesList.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(Listing l, boolean empty) {
                super.updateItem(l, empty);
                setText((empty || l == null) ? null :
                        l.getTitle() + " — ৳" + l.getPricePerMonth() + " | " + l.getLocation());
            }
        });

        messageList.setCellFactory(v -> new ListCell<>() {
            @Override
            protected void updateItem(Message m, boolean empty) {
                super.updateItem(m, empty);
                setText((empty || m == null) ? null :
                        "💬 From Owner ID: " + m.getSenderId() +
                                " | Listing ID: " + m.getListingId() +
                                "\n" + m.getMessageText());
            }
        });

        if (viewChatButton != null)
            viewChatButton.disableProperty().bind(messageList.getSelectionModel().selectedItemProperty().isNull());

        // ✅ Get controller for included FXML
        linkIncludedController();
    }

    /* -----------------------------------------------------------
       ✅ Connect included controller
       ----------------------------------------------------------- */
    private void linkIncludedController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RenterBookings.fxml"));
            loader.setRoot(renterBookingsSection);
            loader.load();
            renterBookingsController = loader.getController();
        } catch (Exception e) {
            System.err.println("⚠️ Failed to link RenterBookingsController: " + e.getMessage());
        }
    }

    /* -----------------------------------------------------------
       ✅ Called by Router after login
       ----------------------------------------------------------- */
    public void setRenterId(int renterId) {
        this.renterId = renterId;
        System.out.println("✅ [RenterDashboardController] Renter ID set: " + renterId);

        // Forward ID to included bookings tab
        if (renterBookingsController != null) {
            renterBookingsController.setRenterId(renterId);
            System.out.println("📤 Forwarded renterId to RenterBookingsController");
        } else {
            System.err.println("⚠️ renterBookingsController is null — check fx:include fx:id");
        }

        loadFeatured();
        loadMessages();
    }

    /* -----------------------------------------------------------
       🌟 Load featured listings
       ----------------------------------------------------------- */
    private void loadFeatured() {
        try {
            List<Listing> featured = listingDAO.findFeatured(6);
            favoritesList.setItems(FXCollections.observableArrayList(featured));
        } catch (Exception ex) {
            error("Failed to load listings", ex.getMessage());
        }
    }

    /* -----------------------------------------------------------
       💬 Load renter messages
       ----------------------------------------------------------- */
    private void loadMessages() {
        try {
            List<Message> msgs = messageDAO.getMessagesForUser(renterId);
            messageList.setItems(FXCollections.observableArrayList(msgs));
        } catch (Exception e) {
            error("Failed to load messages", e.getMessage());
        }
    }

    /* -----------------------------------------------------------
       💬 Open chat
       ----------------------------------------------------------- */
    @FXML
    private void handleViewChat() {
        Message selected = messageList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Message Selected", "Please select a message to open chat.");
            return;
        }

        try {
            int listingId = selected.getListingId();
            int renterId = this.renterId;
            int ownerId = (selected.getSenderId() == renterId)
                    ? selected.getReceiverId()
                    : selected.getSenderId();

            String title = "Listing #" + listingId;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChatWindow.fxml"));
            Parent root = loader.load();

            ChatWindowController controller = loader.getController();
            controller.initChat(listingId, renterId, ownerId, title);

            Stage stage = new Stage();
            stage.setTitle("Chat - " + title);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            error("Chat Error", "Unable to open chat:\n" + e.getMessage());
        }
    }

    /* -----------------------------------------------------------
       🔄 Refresh / Reload
       ----------------------------------------------------------- */
    @FXML
    private void handleRefresh() {
        loadFeatured();
        loadMessages();

        if (renterBookingsController != null)
            renterBookingsController.setRenterId(renterId);
    }

    /* -----------------------------------------------------------
       🔗 Navigation
       ----------------------------------------------------------- */
    @FXML private void handleBrowse() { Router.goToBrowse(); }
    @FXML private void handleHome() { Router.goToHomepage(); }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        UserStore.clear();
        Router.goToHomepage();
    }

    /* -----------------------------------------------------------
       ⚙️ Helper
       ----------------------------------------------------------- */
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
