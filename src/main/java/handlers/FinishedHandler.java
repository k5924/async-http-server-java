package handlers;

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
        try {
            clientChannel.close();
        } catch (final Exception e) {
            System.err.println("Failed to close channel: " + e.getMessage());
        }

    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("Write failed: " + exc.getMessage());
    }
}
