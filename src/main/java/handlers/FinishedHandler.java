package handlers;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class FinishedHandler implements CompletionHandler<Integer, Void> {

    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer byteBuffer;

    public FinishedHandler(final AsynchronousSocketChannel clientChannel,
                           final ByteBuffer byteBuffer) {
        this.clientChannel = clientChannel;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void completed(final Integer result, final Void attachment) {
        finishHandler();
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.err.println("Write failed: " + exc.getMessage());
        finishHandler();
    }

    private void finishHandler() {
        try {
            clientChannel.close();
        } catch (final IOException e) {
            System.err.println("Failed to close channel: " + e.getMessage());
        }
    }
}
