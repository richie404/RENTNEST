package com.rentnest;

import com.rentnest.router.Router;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(javafx.stage.Stage stage) {
        try {
            com.rentnest.router.Router.setStage(stage);
            com.rentnest.router.Router.goToHomePage();
        } catch (Throwable t) {
            t.printStackTrace();
            StringBuilder sb = new StringBuilder(t.toString());
            for (Throwable c = t.getCause(); c != null; c = c.getCause()) {
                sb.append("\nCaused by: ").append(c.getClass().getName())
                        .append(": ").append(c.getMessage());
            }
            new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, sb.toString()).showAndWait();
        }
    }
}

