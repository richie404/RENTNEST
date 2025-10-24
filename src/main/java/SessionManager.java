public class SessionManager {

    private static User loggedInUser;
    private static Runnable postLoginAction;   // ✅ action to run after login
    private static String lastVisitedPage;     // ✅ remember last visited page (like "browse.fxml")

    private SessionManager() {
        // prevent instantiation
    }

    // ✅ Called when user logs in successfully
    public static void setLoggedInUser(User user) {
        loggedInUser = user;

        // After login → open stored action if available
        if (postLoginAction != null) {
            postLoginAction.run();
            postLoginAction = null;
        }
        // Or if no action → go back to last visited page (like Browse)
        else if (lastVisitedPage != null) {
            switch (lastVisitedPage) {
                case "browse":
                    Router.goToBrowse();
                    break;
                case "homepage":
                    Router.goToIndex();
                    break;
                default:
                    Router.goToIndex();
                    break;
            }
            lastVisitedPage = null;
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    // ✅ Allow controllers to store a “do this after login” task
    public static void setPostLoginAction(Runnable action) {
        postLoginAction = action;
    }

    // ✅ Remember where the user was (like Browse, PropertyDetails, etc.)
    public static void setLastVisitedPage(String page) {
        lastVisitedPage = page;
    }

    public static String getLastVisitedPage() {
        return lastVisitedPage;
    }

    // ✅ Clear everything
    public static void logout() {
        loggedInUser = null;
        postLoginAction = null;
        lastVisitedPage = null;
    }
}
