package handlers;

import utils.BufferPool;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
    private final AsynchronousServerSocketChannel serverSocketChannel;
    private final BufferPool bufferPool;

    public AcceptHandler(final AsynchronousServerSocketChannel serverSocketChannel,
                         final BufferPool bufferPool) {

        this.serverSocketChannel = serverSocketChannel;
        this.bufferPool = bufferPool;
    }

    @Override
    public void completed(final AsynchronousSocketChannel clientChannel, final Void attachment) {

        serverSocketChannel.accept(null, this);
        clientChannel.write(null, null, new ReadRequestHandler(clientChannel, bufferPool));
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        System.out.println("Failed to accept connection due to: " + exc.getMessage());
    }
}
