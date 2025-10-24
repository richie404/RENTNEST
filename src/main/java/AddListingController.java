import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddListingController extends BaseController {

    @FXML private TextField titleField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private TextField depositField;
    @FXML private TextField sizeField;
    @FXML private TextField imageUrlField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private CheckBox furnishedCheck;
    @FXML private CheckBox bachelorCheck;
    @FXML private CheckBox familyCheck;

    private final ListingDAO listingDAO = new ListingDAO();
    private int ownerId = 0; // ✅ Populated via Router or SessionManager

    /* -----------------------------------------------------------
       🔹 Initialize ComboBox defaults and ownerId
       ----------------------------------------------------------- */
    @FXML
    private void initialize() {
        if (typeComboBox != null && typeComboBox.getItems().isEmpty()) {
            typeComboBox.getItems().addAll(
                    "SINGLE_ROOM", "SEAT", "ROOMS", "FLAT", "PARKING", "OFFICE", "STOREHOUSE"
            );
        }

        if (bachelorCheck != null) bachelorCheck.setSelected(true);
        if (familyCheck != null) familyCheck.setSelected(true);

        // Auto-assign ownerId if user is logged in
        if (SessionManager.isLoggedIn() && SessionManager.getLoggedInUser().isOwner()) {
            ownerId = SessionManager.getLoggedInUser().getId();
        }
    }

    /* -----------------------------------------------------------
       🔹 Called by Router (owner context)
       ----------------------------------------------------------- */
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /* -----------------------------------------------------------
       💾 Save new listing to database
       ----------------------------------------------------------- */
    @FXML
    private void handleSave() {
        try {
            // ✅ Validation
            if (titleField.getText().trim().isEmpty() || locationField.getText().trim().isEmpty()) {
                warn("Missing Fields", "Please fill out title and location.");
                return;
            }

            // ✅ Build listing object
            Listing listing = new Listing()
                    .setTitle(titleField.getText().trim())
                    .setListingType(typeComboBox.getValue())
                    .setLocation(locationField.getText().trim())
                    .setDescription(descriptionArea.getText().trim())
                    .setPricePerMonth(Double.parseDouble(priceField.getText().trim()))
                    .setDeposit(depositField.getText().isBlank() ? 0.0 : Double.parseDouble(depositField.getText().trim()))
                    .setSizeSqft(sizeField.getText().isBlank() ? null : Integer.parseInt(sizeField.getText().trim()))
                    .setFurnished(furnishedCheck.isSelected())
                    .setBachelorAllowed(bachelorCheck.isSelected())
                    .setFamilyAllowed(familyCheck.isSelected())
                    .setOwnerId(ownerId)
                    .setImageUrl(imageUrlField == null ? null : imageUrlField.getText().trim());

            int newId = listingDAO.add(listing);

            info("✅ Listing Added", "Your property has been successfully saved!\nListing ID: " + newId);

            clearForm();

            // ✅ Redirect back to Owner Dashboard
            Router.goToOwnerDashboard(ownerId);

        } catch (NumberFormatException e) {
            error("Invalid Input", "Please enter valid numbers for price, deposit, and size.");
        } catch (Exception e) {
            error("Save Failed", "Error: " + e.getMessage());
        }
    }

    /* -----------------------------------------------------------
       🔙 Back button handler
       ----------------------------------------------------------- */
    @FXML
    private void handleBack() {
        Router.goToOwnerDashboard(ownerId);
    }

    /* -----------------------------------------------------------
       🧹 Clear form after saving
       ----------------------------------------------------------- */
    private void clearForm() {
        titleField.clear();
        locationField.clear();
        priceField.clear();
        depositField.clear();
        sizeField.clear();
        descriptionArea.clear();

        if (imageUrlField != null) imageUrlField.clear();
        if (typeComboBox != null) typeComboBox.getSelectionModel().clearSelection();

        furnishedCheck.setSelected(false);
        bachelorCheck.setSelected(true);
        familyCheck.setSelected(true);
    }
}
