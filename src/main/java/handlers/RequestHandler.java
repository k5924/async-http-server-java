package handlers;

import parser.HttpRequestParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static utils.Constants.LEADING_SLASH;
import static utils.Constants.NOT_FOUND_BYTES;
import static utils.Constants.OK_RESPONSE_BYTES;

public final class RequestHandler implements CompletionHandler<Integer, Void> {

    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer byteBuffer;

    public RequestHandler(final AsynchronousSocketChannel clientChannel,
                          final ByteBuffer byteBuffer) {
        this.clientChannel = clientChannel;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void completed(final Integer bytesRead, final Void attachment) {
        if (bytesRead == -1) {
            try {
                clientChannel.close();
            } catch (final Exception e) {
                System.err.println("error thrown when closing client channel: " + e.getMessage());
            }
            return;
        }

        final var request = HttpRequestParser.parse(byteBuffer);
        System.out.println("Request is: " + request);

        byteBuffer.clear();

        if (request.uri().equals(LEADING_SLASH)) {
            byteBuffer.put(OK_RESPONSE_BYTES);
        } else {
            byteBuffer.put(NOT_FOUND_BYTES);
        }
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("Failed to read request: " + exc.getMessage());
    }
}
