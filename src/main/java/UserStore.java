public class UserStore {

    private static User currentUser;   // Holds the logged-in user
    private static int currentUserId = -1; // ✅ Added: store ID separately (for convenience)

    /* -----------------------------------------------------------
       ✅ Save full user object
       ----------------------------------------------------------- */
    public static void setCurrentUser(User user) {
        currentUser = user;
        if (user != null) {
            currentUserId = user.getId(); // Keep ID synced
        }
    }

    /* -----------------------------------------------------------
       ✅ Save only the user ID (if you don’t have User object yet)
       ----------------------------------------------------------- */
    public static void setCurrentUserId(int id) {
        currentUserId = id;
    }

    /* -----------------------------------------------------------
       ✅ Get the full user object
       ----------------------------------------------------------- */
    public static User getCurrentUser() {
        return currentUser;
    }

    /* -----------------------------------------------------------
       ✅ Get only the user ID (for quick lookup)
       ----------------------------------------------------------- */
    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getId();
        }
        return currentUserId;
    }

    /* -----------------------------------------------------------
       ✅ Clear stored data (on logout)
       ----------------------------------------------------------- */
    public static void clear() {
        currentUser = null;
        currentUserId = -1;
    }
}
