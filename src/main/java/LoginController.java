import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private final UserDAO userDAO = new UserDAO();

    /* -----------------------------------------------------------
       ✅ Normal Login (Main Entry Point)
       ----------------------------------------------------------- */
    @FXML
    private void handleSubmitLogin() {

        String email = emailField.getText().trim();
        String pass = passwordField.getText().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Failed", "Please fill in both fields.");
            return;
        }

        try {
            if (!userDAO.checkCredentials(email, pass)) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                return;
            }

            Optional<User> u = userDAO.findByEmail(email);
            if (u.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "User not found after authentication.");
                return;
            }

            User user = u.get();

            // ✅ Save the logged-in user globally
            SessionManager.setLoggedInUser(user);

            // ✅ Also store ID for RenterBookingsController / other dashboards
            UserStore.setCurrentUserId(user.getId()); // 👈 This was missing before!

            System.out.println("✅ Logged in as: " + user.getEmail() + " (ID: " + user.getId() + ")");

            // ✅ Route based on role
            if (user.getRoles().contains("OWNER")) {
                Router.goToOwnerDashboard();
            } else if (user.getRoles().contains("RENTER")) {
                Router.goToRenterDashboard();
            } else if (user.getRoles().contains("ADMIN")) {
                Router.goToAdminDashboard();
            } else {
                showAlert(Alert.AlertType.WARNING, "Access Denied", "Unknown user role.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong: " + e.getMessage());
        }
    }

    /* -----------------------------------------------------------
       🔗 Register Link
       ----------------------------------------------------------- */
    @FXML
    private void handleGoRegister() {
        Router.goToRegister();
    }

    /* -----------------------------------------------------------
       🔐 Admin Login Popup
       ----------------------------------------------------------- */
    @FXML
    private void handleAdminLogin() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Admin Login");
        dialog.setHeaderText("Enter Admin Credentials");

        // Buttons
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField email = new TextField();
        email.setPromptText("Admin email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(email, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(email.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(credentials -> {
            String adminEmail = credentials.getKey().trim();
            String adminPassword = credentials.getValue().trim();

            if (adminEmail.isEmpty() || adminPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Login Failed", "Please fill in both fields.");
                return;
            }

            try {
                if (!userDAO.checkCredentials(adminEmail, adminPassword)) {
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "Invalid email or password.");
                    return;
                }

                Optional<User> userOpt = userDAO.findByEmail(adminEmail);
                if (userOpt.isPresent() && userOpt.get().getRoles().contains("ADMIN")) {
                    User admin = userOpt.get();

                    // ✅ Store admin session globally
                    SessionManager.setLoggedInUser(admin);
                    UserStore.setCurrentUserId(admin.getId()); // optional consistency

                    Router.goToAdminDashboard();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "No admin account found with this email.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
            }
        });
    }

    /* -----------------------------------------------------------
       🏠 Back to Homepage
       ----------------------------------------------------------- */
    @FXML
    private void handleBackToHome() {
        Router.goToHomepage();
    }

    /* -----------------------------------------------------------
       ⚙️ Alert Helper
       ----------------------------------------------------------- */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
