import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;

    public void connect(String host, int port, Consumer<String> onMessage) {
        new Thread(() -> {
            try {
                socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String msg;
                while ((msg = in.readLine()) != null) {
                    onMessage.accept(msg);
                }
            } catch (IOException e) {
                onMessage.accept("❌ Connection lost");
            }
        }).start();
    }

    public void send(String message) {
        if (out != null) out.println(message);
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
