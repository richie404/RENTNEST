package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class RegisterController {

    @FXML
    private void handleBack() {
        Router.goToIndex();
    }

    @FXML
    private void handleSubmitRegister() {
        // TODO: Add registration logic later
        Router.goToLogin();
    }
}
