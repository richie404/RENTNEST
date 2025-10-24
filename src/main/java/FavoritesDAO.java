import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoritesDAO {

    /** Add a listing to a renter's favorites. Returns true if inserted, false if it already existed. */
    public boolean add(int userId, int listingId) {
        String sql = "INSERT IGNORE INTO favorites (user_id, listing_id) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("add favorite failed", e);
        }
    }

    /** Remove a listing from a renter's favorites. */
    public boolean remove(int userId, int listingId) {
        String sql = "DELETE FROM favorites WHERE user_id=? AND listing_id=?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("remove favorite failed", e);
        }
    }

    /** Check if a listing is in the renter's favorites. */
    public boolean isFavorite(int userId, int listingId) {
        String sql = "SELECT 1 FROM favorites WHERE user_id=? AND listing_id=? LIMIT 1";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("isFavorite failed", e);
        }
    }

    /** Get all favorite listings for a renter (with a cover image if available). */
    public List<Listing> list(int userId) {
        String sql =
                "SELECT l.id,l.title,l.listing_type,l.location,l.description," +
                        "       l.price_month,l.deposit,l.size_sqft,l.furnished," +
                        "       l.bachelor_allowed,l.family_allowed,l.owner_id," +
                        "       MIN(p.url) AS image_url " +
                        "FROM favorites f " +
                        "JOIN listings l ON l.id=f.listing_id " +
                        "LEFT JOIN listing_photos p ON p.listing_id=l.id " +
                        "WHERE f.user_id=? " +
                        "GROUP BY l.id " +
                        "ORDER BY f.created_at DESC";

        List<Listing> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapListing(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("list favorites failed", e);
        }
        return out;
    }

    /** Count how many users have favorited a listing (useful for badges). */
    public int countForListing(int listingId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE listing_id=?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("count favorites failed", e);
        }
    }

    /* Map DB row -> Listing model */
    private Listing mapListing(ResultSet rs) throws SQLException {
        return new Listing()
                .setId(rs.getInt("id"))
                .setTitle(rs.getString("title"))
                .setListingType(rs.getString("listing_type"))
                .setLocation(rs.getString("location"))
                .setDescription(rs.getString("description"))
                .setPricePerMonth(rs.getDouble("price_month"))
                .setDeposit(rs.getDouble("deposit"))
                .setSizeSqft(rs.getObject("size_sqft") == null ? null : rs.getInt("size_sqft"))
                .setFurnished(rs.getBoolean("furnished"))
                .setBachelorAllowed(rs.getBoolean("bachelor_allowed"))
                .setFamilyAllowed(rs.getBoolean("family_allowed"))
                .setOwnerId(rs.getInt("owner_id"))
                .setImageUrl(rs.getString("image_url"));
    }
}
