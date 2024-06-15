package handlers;

import utils.BufferPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class CompletedHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer buffer;
    private final BufferPool bufferPool;
    private volatile boolean isFinished = false;

    public CompletedHandler(final AsynchronousSocketChannel clientChannel,
                            final ByteBuffer buffer,
                            final BufferPool bufferPool) {
        this.clientChannel = clientChannel;
        this.buffer = buffer;
        this.bufferPool = bufferPool;
    }

    @Override
    public void completed(final Integer result, final ByteBuffer attachment) {
        setFinished();
    }

    @Override
    public void failed(final Throwable exc, final ByteBuffer attachment) {
        System.out.println("Failed to send response: " + exc.getMessage());
        setFinished();
    }

    private void setFinished() {
        if (!isFinished) {
            isFinished = true;
            try {
                clientChannel.close();
            } catch (final IOException e) {
                System.err.println("Failed to close socket channel: " + e.getMessage());
            }
            buffer.clear();
            try {
                bufferPool.releaseBuffer(buffer);
            } catch (final InterruptedException e) {
                System.err.println("Failed to return buffer to pool");
            }
        }
    }
}
