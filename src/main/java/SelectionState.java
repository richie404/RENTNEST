public final class SelectionState {
    private static String searchQuery;

    private SelectionState() {}

    public static void setSearchQuery(String q) { searchQuery = (q == null ? "" : q); }

    /** Returns the query once and clears it, so it’s used only on first load. */
    public static String consumeSearchQuery() {
        String q = searchQuery;
        searchQuery = null;
        return q;
    }
}
