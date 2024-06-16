package handlers;

import utils.BufferPool;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel serverSocketChannel;
    private final HandlerFactory handlerFactory;
    private final BufferPool bufferPool;

    public AcceptHandler(final AsynchronousServerSocketChannel serverSocketChannel,
                         final HandlerFactory handlerFactory,
                         final BufferPool bufferPool) {

        this.serverSocketChannel = serverSocketChannel;
        this.handlerFactory = handlerFactory;
        this.bufferPool = bufferPool;
    }

    @Override
    public void completed(final AsynchronousSocketChannel clientChannel, final Void attachment) {

        serverSocketChannel.accept(null, this);
        final ByteBuffer byteBuffer;
        try {
            byteBuffer = bufferPool.getBuffer();
            clientChannel.read(byteBuffer, null, new RequestHandler(clientChannel, byteBuffer, handlerFactory,
                    bufferPool));
        } catch (final InterruptedException e) {
            System.err.println("Failed to get buffer from pool, closing socket: " + e.getMessage());
            try {
                clientChannel.close();
            } catch (final IOException ex) {
                System.err.println("Failed to close client socket");
            }
        }
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        System.err.println("Failed to accept connection due to: " + exc.getMessage());
    }
}
