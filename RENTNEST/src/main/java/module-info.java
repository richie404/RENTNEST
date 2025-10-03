module com.rentnest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.rentnest.controllers to javafx.fxml; // FXML controllers
    opens com.rentnest.models to javafx.base;      // TableView models

    exports com.rentnest;              // <-- Main lives here
    exports com.rentnest.router;
    exports com.rentnest.controllers;
}
