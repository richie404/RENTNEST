package com.rentnest.router;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Router {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    // ---------- Internal helpers ----------

    private static void ensureStage() {
        if (stage == null) {
            throw new IllegalStateException("Router.setStage(primaryStage) must be called before navigation.");
        }
    }

    private static String resolveViewName(String name) {
        // Normalize to the correct file casing used in /resources/views
        String key = name.endsWith(".fxml") ? name.substring(0, name.length() - 5) : name;
        switch (key.toLowerCase()) {
            case "addlisting":        return "AddListing";
            case "admindashboard":    return "AdminDashboard";
            case "ownerdashboard":    return "OwnerDashboard";
            case "renterdashboard":   return "RenterDashboard";
            // lowercase files
            case "homepage":
            case "login":
            case "register":
            case "browse":
            case "listings":
            case "details":
                return key.toLowerCase();
            default:
                return key; // assume exact name provided
        }
    }

    private static String defaultTitleFor(String view) {
        switch (view.toLowerCase()) {
            case "homepage":       return "RentNest - Home";
            case "login":          return "RentNest - Login";
            case "register":       return "RentNest - Register";
            case "browse":         return "RentNest - Browse Properties";
            case "listings":       return "RentNest - Listings";
            case "addlisting":     return "RentNest - List Your Property";
            case "details":        return "RentNest - Property Details";
            case "admindashboard": return "RentNest - Admin Dashboard";
            case "ownerdashboard": return "RentNest - Home Owner Dashboard";
            case "renterdashboard":return "RentNest - Renter Dashboard";
            default:               return "RentNest";
        }
    }

    // Router.java
// Router.java
    private static Scene loadView(String viewName) {
        try {
            String resolved = viewName.endsWith(".fxml")
                    ? viewName.substring(0, viewName.length() - 5)
                    : viewName;
            String path = "/views/" + resolved + ".fxml";    // e.g. /views/homepage.fxml

            var url = Router.class.getResource(path);
            if (url == null) {
                throw new IllegalStateException(
                        "View not found: " + path +
                                "\nExpected at: src/main/resources" + path +
                                "\n(After build: target/classes" + path + ")"
                );
            }

            // give FXMLLoader a base location; relative @... and fx:include will resolve
            var loader = new javafx.fxml.FXMLLoader(url);
            javafx.scene.Parent root = loader.load();
            return new javafx.scene.Scene(root, 900, 600);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Cannot load FXML view: " + viewName, e);
        }
    }



    private static void show(String viewName, String title) {
        ensureStage();
        stage.setScene(loadView(viewName));
        stage.setTitle(title);
        stage.show();
    }

    // ---------- Generic navigation (for calls like Router.go("details")) ----------

    public static void go(String viewName) {
        String resolved = resolveViewName(viewName);
        show(resolved, defaultTitleFor(resolved));
    }

    // ---------- Public, explicit navigation helpers ----------

    public static void goToHomePage()      { show("homepage",       defaultTitleFor("homepage")); }
    public static void goToLogin()         { show("login",          defaultTitleFor("login")); }
    public static void goToRegister()      { show("register",       defaultTitleFor("register")); }
    public static void goToBrowse()        { show("browse",         defaultTitleFor("browse")); }
    public static void goToListings()      { show("listings",       defaultTitleFor("listings")); }       // TABLE view
    public static void goToAddListing()    { show("AddListing",     defaultTitleFor("addlisting")); }     // FORM view
    public static void goToDetails()       { show("details",        defaultTitleFor("details")); }

    public static void goToAdminDashboard(){ show("AdminDashboard", defaultTitleFor("admindashboard")); }
    public static void goToOwnerDashboard(){ show("OwnerDashboard", defaultTitleFor("ownerdashboard")); }
    public static void goToRenterDashboard(){show("RenterDashboard",defaultTitleFor("renterdashboard")); }
}
