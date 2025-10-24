import java.sql.*;
import java.util.*;

public class ListingDAO {

    /* -----------------------------------------------------------
       Get ALL listings (for Browse / Homepage)
       ----------------------------------------------------------- */
    public List<Listing> findAll() {
        String sql =
                "SELECT l.id, l.name AS title, l.location, l.price, l.owner_id, " +
                        "MIN(p.url) AS image_url " +
                        "FROM listings l " +
                        "LEFT JOIN listing_photos p ON p.listing_id = l.id " +
                        "GROUP BY l.id ORDER BY l.created_at DESC";

        List<Listing> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(map(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("findAll failed: " + e.getMessage(), e);
        }
        return out;
    }

    /* -----------------------------------------------------------
       Random featured listings (for renter dashboard)
       ----------------------------------------------------------- */
    public List<Listing> findFeatured(int limit) {
        String sql =
                "SELECT l.id, l.name AS title, l.location, l.price, l.owner_id, " +
                        "MIN(p.url) AS image_url " +
                        "FROM listings l " +
                        "LEFT JOIN listing_photos p ON p.listing_id = l.id " +
                        "GROUP BY l.id ORDER BY RAND() LIMIT " + limit;

        List<Listing> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) out.add(map(rs));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("findFeatured failed: " + e.getMessage(), e);
        }
        return out;
    }

    /* -----------------------------------------------------------
       Find listing by ID (for Property Details page)
       ----------------------------------------------------------- */
    public Optional<Listing> findById(int id) {
        String sql =
                "SELECT l.id, l.name AS title, l.location, l.price, l.owner_id, " +
                        "MIN(p.url) AS image_url " +
                        "FROM listings l " +
                        "LEFT JOIN listing_photos p ON p.listing_id = l.id " +
                        "WHERE l.id = ? GROUP BY l.id";

        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("findById failed: " + e.getMessage(), e);
        }
    }

    /* -----------------------------------------------------------
       Find listings by owner (for Owner Dashboard)
       ----------------------------------------------------------- */
    public List<Listing> findByOwner(int ownerId) {
        String sql =
                "SELECT l.id, l.name AS title, l.location, l.price, l.owner_id, " +
                        "MIN(p.url) AS image_url " +
                        "FROM listings l " +
                        "LEFT JOIN listing_photos p ON p.listing_id = l.id " +
                        "WHERE l.owner_id = ? " +
                        "GROUP BY l.id ORDER BY l.created_at DESC";

        List<Listing> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) out.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("findByOwner failed: " + e.getMessage(), e);
        }
        return out;
    }

    /* -----------------------------------------------------------
       Search listings by keyword (for Browse / Homepage)
       ----------------------------------------------------------- */
    public List<Listing> search(String query) {
        String sql =
                "SELECT l.id, l.name AS title, l.location, l.price, l.owner_id, " +
                        "MIN(p.url) AS image_url " +
                        "FROM listings l " +
                        "LEFT JOIN listing_photos p ON p.listing_id = l.id " +
                        "WHERE l.name LIKE ? OR l.location LIKE ? " +
                        "GROUP BY l.id ORDER BY l.created_at DESC";

        List<Listing> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {

            String q = "%" + query + "%";
            ps.setString(1, q);
            ps.setString(2, q);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) out.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("search failed: " + e.getMessage(), e);
        }
        return out;
    }

    /* -----------------------------------------------------------
       Add a new listing (for Owner)
       ----------------------------------------------------------- */
    public int add(Listing l) {
        String sql =
                "INSERT INTO listings (name, location, price, owner_id, created_at) " +
                        "VALUES (?, ?, ?, ?, NOW())";

        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, l.getTitle());
            ps.setString(2, l.getLocation());
            ps.setDouble(3, l.getPricePerMonth());
            ps.setInt(4, l.getOwnerId());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException("add listing failed: " + e.getMessage(), e);
        }
        return -1;
    }

    /* -----------------------------------------------------------
       Update existing listing (for editing)
       ----------------------------------------------------------- */
    public void update(Listing l) {
        String sql =
                "UPDATE listings SET name = ?, location = ?, price = ?, owner_id = ? WHERE id = ?";

        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitle());
            ps.setString(2, l.getLocation());
            ps.setDouble(3, l.getPricePerMonth());
            ps.setInt(4, l.getOwnerId());
            ps.setInt(5, l.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("update failed: " + e.getMessage(), e);
        }
    }

    /* -----------------------------------------------------------
       Delete a listing (owner safety check)
       ----------------------------------------------------------- */
    public boolean delete(int id, int ownerId) {
        String sql = "DELETE FROM listings WHERE id = ? AND owner_id = ?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, ownerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("delete failed: " + e.getMessage(), e);
        }
    }

    /* -----------------------------------------------------------
       Mapper: convert ResultSet → Listing
       ----------------------------------------------------------- */
    private Listing map(ResultSet rs) throws SQLException {
        Listing l = new Listing();
        l.setId(rs.getInt("id"));
        l.setTitle(rs.getString("title"));
        l.setLocation(rs.getString("location"));
        l.setPricePerMonth(rs.getDouble("price"));
        l.setOwnerId(rs.getInt("owner_id"));
        try {
            l.setImageUrl(rs.getString("image_url"));
        } catch (SQLException ignored) {}
        return l;
    }

    /* -----------------------------------------------------------
       Find photo URLs for a listing (Property Details gallery)
       ----------------------------------------------------------- */
    public String[] findPhotosByListingId(int listingId) {
        String sql = "SELECT url FROM listing_photos WHERE listing_id = ?";
        List<String> urls = new ArrayList<>();

        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                urls.add(rs.getString("url"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findPhotosByListingId failed: " + e.getMessage(), e);
        }
        return urls.toArray(new String[0]);
    }

    /* -----------------------------------------------------------
       ✅ NEW: Find Owner ID by Listing (for chat)
       ----------------------------------------------------------- */
    public int findOwnerIdByListing(int listingId) {
        String sql = "SELECT owner_id FROM listings WHERE id = ?";
        try (Connection conn = DatabaseConnection.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, listingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("owner_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
