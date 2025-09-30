package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class HomepageController {

    @FXML
    private void handleBrowse() {
        Router.goToBrowse();
    }

    @FXML
    private void handleLogin() {
        Router.goToLogin();
    }

    @FXML
    private void handleRegister() {
        Router.goToRegister();
    }

    @FXML
    private void handleListings() {
        Router.goToListings();
    }
}
