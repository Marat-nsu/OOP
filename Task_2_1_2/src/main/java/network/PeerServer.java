package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class PeerServer implements AutoCloseable {
    private final Consumer<Message> messageHandler;
    private final Consumer<String> errorHandler;
    private ServerSocket serverSocket;
    private Thread serverThread;
    private volatile boolean running;

    public PeerServer(int port, String threadName, Consumer<Message> messageHandler, Consumer<String> errorHandler) {
        this.messageHandler = messageHandler;
        this.errorHandler = errorHandler;
        start(port, threadName);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    @Override
    public void close() {
        running = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void start(int port, String threadName) {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            serverThread = new Thread(this::acceptLoop, threadName);
            serverThread.setDaemon(true);
            serverThread.start();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to start peer server", e);
        }
    }

    private void acceptLoop() {
        while (running) {
            try (Socket socket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line = reader.readLine();
                if (line != null) {
                    messageHandler.accept(Message.parse(line));
                }
            } catch (IOException ignored) {
                if (running) {
                    errorHandler.accept("network:accept failed");
                }
            }
        }
    }
}
