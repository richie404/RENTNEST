import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminActionDAO {

    /* -----------------------------------------------------------
       🔹 Log a new admin action
       ----------------------------------------------------------- */
    public boolean logAction(int adminId, String type, int targetId, String details) {
        String sql = "INSERT INTO admin_actions(admin_id, action_type, target_id, details) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.setString(2, type);
            stmt.setInt(3, targetId);
            stmt.setString(4, details);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* -----------------------------------------------------------
       🔹 Fetch recent actions (for logs page)
       ----------------------------------------------------------- */
    public List<AdminAction> getRecentActions(int limit) {
        List<AdminAction> list = new ArrayList<>();
        String sql = "SELECT * FROM admin_actions ORDER BY created_at DESC LIMIT ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AdminAction a = new AdminAction();
                a.setId(rs.getInt("id"));
                a.setAdminId(rs.getInt("admin_id"));
                a.setActionType(rs.getString("action_type"));
                a.setTargetId(rs.getInt("target_id"));
                a.setDetails(rs.getString("details"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /* -----------------------------------------------------------
       🔹 Clear logs (use carefully)
       ----------------------------------------------------------- */
    public boolean clearLogs() {
        String sql = "DELETE FROM admin_actions";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
