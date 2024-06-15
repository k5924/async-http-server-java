import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public final class Server {

    private final int port;
    private volatile boolean running;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Server(final int port) {
        this.port = port;
    }

    public void startServer() {
        running = true;

        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            final var address = new InetSocketAddress(port);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(address);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (running) {
                selector.select();
                final var selectionKeys = selector.selectedKeys();
                final var iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    final var key = iterator.next();
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        if (selector != null && selector.isOpen()) {
            try {
                for (final var key : selector.keys()) {
                    key.channel().close();
                }
                selector.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
            try {
                serverSocketChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
