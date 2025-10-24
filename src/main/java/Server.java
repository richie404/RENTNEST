import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RentNest LAN Chat Server
 * ------------------------
 * Listens for incoming clients over LAN (default port: 5000).
 * Each client sends:
 *   REGISTER <userId>
 *   MSG\t<listingId or -1>\t<senderId>\t<receiverId>\t<base64(messageText)>
 *
 * The server:
 *   ✅ Saves every message in the MySQL database (via MessageDAO)
 *   ✅ Forwards the message in real-time to the receiver if connected
 *
 * Requirements:
 *   - Message.java
 *   - MessageDAO.java
 *   - DatabaseConnection.java (used by MessageDAO)
 */
public class Server {

    private static final int PORT = 5000;
    private static final Map<Integer, ClientHandler> CLIENTS = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("💬 RentNest Chat Server starting on port " + PORT + " ...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Server is now listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles one connected client.
     */
    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private PrintWriter out;
        private int userId = -1;
        private final MessageDAO messageDAO = new MessageDAO();

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String remote = socket.getRemoteSocketAddress().toString();
            System.out.println("🔌 New connection from " + remote);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out = new PrintWriter(socket.getOutputStream(), true);

                // Step 1: Expect a registration line ("REGISTER <userId>")
                String reg = in.readLine();
                if (reg == null || !reg.startsWith("REGISTER")) {
                    System.out.println("⚠️ Invalid handshake from " + remote);
                    close();
                    return;
                }

                String[] parts = reg.trim().split("\\s+");
                if (parts.length < 2) {
                    System.out.println("⚠️ Missing userId during handshake from " + remote);
                    close();
                    return;
                }

                userId = Integer.parseInt(parts[1]);
                CLIENTS.put(userId, this);
                System.out.println("✅ Registered user " + userId + " (" + remote + ")");

                // Step 2: Handle incoming messages
                String line;
                while ((line = in.readLine()) != null) {
                    if (!line.startsWith("MSG\t")) continue;

                    String[] f = line.split("\t", 5);
                    if (f.length < 5) continue;

                    // Extract message details
                    Integer listingId = null;
                    try {
                        int l = Integer.parseInt(f[1]);
                        if (l >= 0) listingId = l;
                    } catch (NumberFormatException ignored) {}

                    int senderId = Integer.parseInt(f[2]);
                    int receiverId = Integer.parseInt(f[3]);
                    String messageText = new String(Base64.getDecoder().decode(f[4]));

                    // Save message to database
                    Message msg = new Message();
                    msg.setListingId(listingId);
                    msg.setSenderId(senderId);
                    msg.setReceiverId(receiverId);
                    msg.setMessageText(messageText);

                    try {
                        messageDAO.addMessage(msg);
                    } catch (Exception dbEx) {
                        System.err.println("❌ Database insert failed: " + dbEx.getMessage());
                        dbEx.printStackTrace();
                    }

                    // Forward to receiver if they are connected
                    ClientHandler receiver = CLIENTS.get(receiverId);
                    if (receiver != null) {
                        receiver.send(line);
                    }

                    System.out.println("📨 " + senderId + " → " + receiverId +
                            " | listing=" + (listingId == null ? "none" : listingId) +
                            " | " + messageText);
                }

            } catch (Exception e) {
                System.err.println("⚠️ Client (" + userId + ") disconnected: " + e.getMessage());
            } finally {
                close();
            }
        }

        /**
         * Sends a message line to this client.
         */
        private void send(String line) {
            if (out != null) out.println(line);
        }

        /**
         * Cleanly disconnects the client.
         */
        private void close() {
            if (userId != -1) {
                CLIENTS.remove(userId);
                System.out.println("🔌 Disconnected user " + userId);
            }
            try { socket.close(); } catch (IOException ignored) {}
        }
    }
}



