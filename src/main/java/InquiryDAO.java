import java.sql.*;
import java.time.Instant;
import java.util.*;

/**
 * DAO for inquiries (booking requests / messages).
 * Uses a lightweight view class to avoid adding a new model file.
 */
public class InquiryDAO {

    public static final Set<String> ALLOWED_STATUSES =
            Set.of("NEW", "SEEN", "RESPONDED", "CLOSED");

    /** Minimal view of an inquiry with useful joins for UI lists. */
    public static class InquiryView {
        private final int id;
        private final int listingId;
        private final String listingTitle;
        private final int ownerId;
        private final int renterId;
        private final String renterName;
        private final String message;
        private final String status;
        private final Instant createdAt;

        public InquiryView(int id, int listingId, String listingTitle, int ownerId,
                           int renterId, String renterName, String message, String status, Instant createdAt) {
            this.id = id; this.listingId = listingId; this.listingTitle = listingTitle; this.ownerId = ownerId;
            this.renterId = renterId; this.renterName = renterName; this.message = message; this.status = status;
            this.createdAt = createdAt;
        }
        public int getId() { return id; }
        public int getListingId() { return listingId; }
        public String getListingTitle() { return listingTitle; }
        public int getOwnerId() { return ownerId; }
        public int getRenterId() { return renterId; }
        public String getRenterName() { return renterName; }
        public String getMessage() { return message; }
        public String getStatus() { return status; }
        public Instant getCreatedAt() { return createdAt; }
    }

    /** Create a new inquiry. Returns generated inquiry id. */
    public int create(int listingId, int renterId, String message) {
        String sql = "INSERT INTO inquiries (listing_id, renter_id, message, status) VALUES (?,?,?, 'NEW')";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, listingId);
            ps.setInt(2, renterId);
            ps.setString(3, message);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next(); return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("create inquiry failed", e);
        }
    }

    /** All inquiries for listings owned by this owner. */
    public List<InquiryView> listForOwner(int ownerId) {
        String sql =
                "SELECT i.id, i.listing_id, l.title AS listing_title, l.owner_id, " +
                        "       i.renter_id, COALESCE(u.username, u.email) AS renter_name, " +
                        "       i.message, i.status, i.created_at " +
                        "FROM inquiries i " +
                        "JOIN listings l ON l.id=i.listing_id " +
                        "JOIN users u ON u.id=i.renter_id " +
                        "WHERE l.owner_id=? " +
                        "ORDER BY i.created_at DESC";
        return queryList(sql, ps -> ps.setInt(1, ownerId));
    }

    /** All inquiries created by a renter. */
    public List<InquiryView> listForRenter(int renterId) {
        String sql =
                "SELECT i.id, i.listing_id, l.title AS listing_title, l.owner_id, " +
                        "       i.renter_id, COALESCE(u.username, u.email) AS renter_name, " +
                        "       i.message, i.status, i.created_at " +
                        "FROM inquiries i " +
                        "JOIN listings l ON l.id=i.listing_id " +
                        "JOIN users u ON u.id=i.renter_id " +
                        "WHERE i.renter_id=? " +
                        "ORDER BY i.created_at DESC";
        return queryList(sql, ps -> ps.setInt(1, renterId));
    }

    /** Get a single inquiry with joins. */
    public Optional<InquiryView> get(int inquiryId) {
        String sql =
                "SELECT i.id, i.listing_id, l.title AS listing_title, l.owner_id, " +
                        "       i.renter_id, COALESCE(u.username, u.email) AS renter_name, " +
                        "       i.message, i.status, i.created_at " +
                        "FROM inquiries i " +
                        "JOIN listings l ON l.id=i.listing_id " +
                        "JOIN users u ON u.id=i.renter_id " +
                        "WHERE i.id=?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, inquiryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("get inquiry failed", e);
        }
    }

    /** Update status -> one of NEW/SEEN/RESPONDED/CLOSED */
    public boolean updateStatus(int inquiryId, String newStatus) {
        String s = newStatus == null ? "" : newStatus.trim().toUpperCase(Locale.ROOT);
        if (!ALLOWED_STATUSES.contains(s)) {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
        String sql = "UPDATE inquiries SET status=? WHERE id=?";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s);
            ps.setInt(2, inquiryId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("updateStatus failed", e);
        }
    }

    /** For owner dashboard badges: count NEW inquiries for this owner’s listings. */
    public int countNewForOwner(int ownerId) {
        String sql =
                "SELECT COUNT(*) " +
                        "FROM inquiries i JOIN listings l ON l.id=i.listing_id " +
                        "WHERE l.owner_id=? AND i.status='NEW'";
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next(); return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("countNewForOwner failed", e);
        }
    }

    /* ---------- helpers ---------- */

    private interface Binder { void bind(PreparedStatement ps) throws SQLException; }

    private List<InquiryView> queryList(String sql, Binder binder) {
        List<InquiryView> out = new ArrayList<>();
        try (Connection c = DatabaseConnection.get();
             PreparedStatement ps = c.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("queryList failed", e);
        }
        return out;
    }

    private InquiryView map(ResultSet rs) throws SQLException {
        return new InquiryView(
                rs.getInt("id"),
                rs.getInt("listing_id"),
                rs.getString("listing_title"),
                rs.getInt("owner_id"),
                rs.getInt("renter_id"),
                rs.getString("renter_name"),
                rs.getString("message"),
                rs.getString("status"),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}
