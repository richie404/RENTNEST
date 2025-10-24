import java.time.LocalDateTime;

/** Simple POJO representing a message between users. */
public class Message {

    private int id;
    private Integer listingId;  // ✅ Nullable for general chat
    private int senderId;
    private int receiverId;
    private String messageText;
    private LocalDateTime timestamp;

    // ------------------------------------------------------------
    // 🟢 Default constructor (fixes no-args error)
    // ------------------------------------------------------------
    public Message() {
        // Used by Client, Server, and Controllers before fields are set
        this.timestamp = LocalDateTime.now();
    }

    // ------------------------------------------------------------
    // 🧱 Full constructor (DB read)
    // ------------------------------------------------------------
    public Message(int id, Integer listingId, int senderId, int receiverId,
                   String messageText, LocalDateTime timestamp) {
        this.id = id;
        this.listingId = listingId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // ------------------------------------------------------------
    // ✉️ Convenience constructor (for sending new messages)
    // ------------------------------------------------------------
    public Message(Integer listingId, int senderId, int receiverId, String messageText) {
        this.listingId = listingId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.timestamp = LocalDateTime.now();
    }

    // ------------------------------------------------------------
    // 🧩 Getters
    // ------------------------------------------------------------
    public int getId() { return id; }
    public Integer getListingId() { return listingId; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessageText() { return messageText; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // ------------------------------------------------------------
    // 🧩 Setters
    // ------------------------------------------------------------
    public void setId(int id) { this.id = id; }
    public void setListingId(Integer listingId) { this.listingId = listingId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", listingId=" + listingId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", messageText='" + messageText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
