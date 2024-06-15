package handlers;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class CompletedHandler<I extends Number, B extends Buffer> implements CompletionHandler<Integer, Void> {

    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer buffer;
    private volatile boolean isFinished = false;

    public CompletedHandler(final AsynchronousSocketChannel clientChannel,
                            final ByteBuffer buffer) {
        this.clientChannel = clientChannel;
        this.buffer = buffer;
    }

    @Override
    public void completed(final Integer result, final Void attachment) {
        setFinished();
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
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
        }
    }
}
