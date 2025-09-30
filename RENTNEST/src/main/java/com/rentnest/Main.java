package com.rentnest;

import com.rentnest.router.Router;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Router.setStage(stage);
        Router.goToIndex(); // ✅ updated from goTo("homepage.fxml")
    }

    public static void main(String[] args) {
        launch();
    }
}
