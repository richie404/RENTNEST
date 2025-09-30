package com.rentnest.controllers;

import com.rentnest.router.Router;
import javafx.fxml.FXML;

public class DetailsController {

    @FXML
    private void handleBack() {
        Router.goToBrowse();
    }
}
