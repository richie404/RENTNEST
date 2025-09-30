package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class BrowseController {

    @FXML
    private void handleBack() {
        Router.goToIndex();
    }

    @FXML
    private void handleOpenDetails() {
        Router.goToDetails();
    }
}
