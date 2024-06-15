package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel serverSocketChannel;
    private final HandlerFactory handlerFactory;

    public AcceptHandler(final AsynchronousServerSocketChannel serverSocketChannel,
                         final HandlerFactory handlerFactory) {

        this.serverSocketChannel = serverSocketChannel;
        this.handlerFactory = handlerFactory;
    }

    @Override
    public void completed(final AsynchronousSocketChannel clientChannel, final Void attachment) {

        serverSocketChannel.accept(null, this);
        final var byteBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(byteBuffer, null, new RequestHandler(clientChannel, byteBuffer, handlerFactory));
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        System.out.println("Failed to accept connection due to: " + exc.getMessage());
    }
}
