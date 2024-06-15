import handlers.AcceptHandler;
import handlers.HandlerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Server {
    private final int port;
    private final ExecutorService executorService;
    private AsynchronousServerSocketChannel serverSocketChannel;

    public Server(final int port) {
        this.port = port;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void startServer() throws Exception {
        final var handlerFactory = new HandlerFactory();
        serverSocketChannel = AsynchronousServerSocketChannel.open();
        final var socketAddress = new InetSocketAddress(port);
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.accept(null, new AcceptHandler(serverSocketChannel, handlerFactory));

        executorService.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void stopServer() throws IOException {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("Executor didnt terminate in specified time");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        serverSocketChannel.close();
        System.out.println("Server stopped");
    }
}
