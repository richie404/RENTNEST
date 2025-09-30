package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void handleBack() {
        Router.goToIndex();
    }

    @FXML
    private void handleSubmitLogin() {
        // TODO: Add login logic later
        Router.goToBrowse();
    }
}
