package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

import static utils.Constants.*;

public final class FileReadHandler implements CompletionHandler<Integer, Void> {

    private final ByteBuffer fileBuffer;
    private final ByteBuffer byteBuffer;
    private final AsynchronousSocketChannel clientChannel;
    private final AsynchronousFileChannel fileChannel;

    public FileReadHandler(final ByteBuffer fileBuffer,
                           final ByteBuffer byteBuffer,
                           final AsynchronousSocketChannel clientChannel,
                           final AsynchronousFileChannel fileChannel) {
        this.fileBuffer = fileBuffer;
        this.byteBuffer = byteBuffer;
        this.clientChannel = clientChannel;
        this.fileChannel = fileChannel;
    }

    @Override
    public void completed(final Integer result, final Void attachment) {
        fileBuffer.flip();
        final var response = FILE_RESPONSE + result + END_OF_MESSAGE;
        byteBuffer.put(response.getBytes(StandardCharsets.UTF_8));
        byteBuffer.put(fileBuffer);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
        try {
            fileChannel.close();
        } catch (Exception e) {
            System.err.println("Failed to close file channel: " + e.getMessage());
        }
    }

    @Override
    public void failed(final Throwable exc, final Void attachment) {
        System.err.println("Failed to read file: " + exc.getMessage());
        byteBuffer.put(NOT_FOUND_BYTES);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
        try {
            fileChannel.close();
        } catch (Exception e) {
            System.err.println("Failed to close file channel: " + e.getMessage());
        }
    }
}
