import java.sql.Date;
import java.sql.Timestamp;

public class Booking {
    private int id;
    private int ListingId;
    private int renterId;
    private int ownerId;
    private Date startDate;
    private Date endDate;
    private double totalAmount;
    private String status; // PENDING_OWNER_APPROVAL, CONFIRMED, RENTED, CANCELLED
    private Timestamp createdAt;
    private String listingName;
    private String location;
    private double price;

    public Booking() {}

    public Booking(int ListingId, int renterId, int ownerId, Date startDate, Date endDate, double totalAmount, String status) {
        this.ListingId = ListingId;
        this.renterId = renterId;
        this.ownerId = ownerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // --- Getters ---
    public int getId() { return id; }
    public int getListingId() { return ListingId; }
    public int getRenterId() { return renterId; }
    public int getOwnerId() { return ownerId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public String getListingName() { return listingName; }
    public String getLocation() { return location; }
    public double getPrice() { return price; }

    // --- Setters ---

    public void setId(int id) { this.id = id; }
    public void setListingId(int listingId) { this.ListingId = listingId; }
    public void setRenterId(int renterId) { this.renterId = renterId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setListingName(String listingName) { this.listingName = listingName; }
    public void setLocation(String location) { this.location = location; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", ListingId=" + ListingId +
                ", renterId=" + renterId +
                ", ownerId=" + ownerId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
