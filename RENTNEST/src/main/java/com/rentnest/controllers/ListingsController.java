package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class ListingsController {

    @FXML
    private void handleBack() {
        Router.goToIndex();
    }

    @FXML
    private void handleSubmitListing() {
        // TODO: Add listing submission logic later
        Router.goToBrowse();
    }
}
