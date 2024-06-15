package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel serverSocketChannel;

    public AcceptHandler(final AsynchronousServerSocketChannel serverSocketChannel) {

        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void completed(final AsynchronousSocketChannel clientChannel, final Void attachment) {

        serverSocketChannel.accept(null, this);
        final var byteBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(byteBuffer, byteBuffer, new RequestHandler(clientChannel, byteBuffer));
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        System.out.println("Failed to accept connection due to: " + exc.getMessage());
    }
}
