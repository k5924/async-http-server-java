package handlers;

import utils.HandlerTools;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

import static utils.Constants.CREATED_RESPONSE;
import static utils.Constants.NOT_FOUND_BYTES;

public final class FileWriteHandler implements CompletionHandler<Integer, Void> {

    private final ByteBuffer byteBuffer;
    private final AsynchronousSocketChannel clientChannel;
    private final AsynchronousFileChannel fileChannel;

    public FileWriteHandler(final ByteBuffer byteBuffer,
                            final AsynchronousSocketChannel clientChannel,
                            final AsynchronousFileChannel fileChannel) {
        this.byteBuffer = byteBuffer;
        this.clientChannel = clientChannel;
        this.fileChannel = fileChannel;
    }

    @Override
    public void completed(final Integer result, final Void attachment) {
        byteBuffer.put(CREATED_RESPONSE.getBytes(StandardCharsets.UTF_8));
        finishHandler();
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.err.println("Failed to write file: " + exc.getMessage());
        byteBuffer.put(NOT_FOUND_BYTES);
        finishHandler();
    }

    private void finishHandler() {
        HandlerTools.cleanupConnection(clientChannel, byteBuffer);
        try {
            fileChannel.close();
        } catch (final IOException e) {
            System.err.println("Failed to close file channel: " + e.getMessage());
        }
    }
}
