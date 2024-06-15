package handlers;

import parser.HttpRequestParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

import static utils.Constants.*;

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
            byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        } else if (request.uri().startsWith(ECHO_ENDPOINT)){
            final var content = request.uri().split(LEADING_SLASH);
            if (content.length > 2) {
                final var response = content[2];
                final var response_to_encode = OK_RESPONSE + CONTENT_TYPE + TEXT_CONTENT + CONTENT_LENGTH + HEADER_SEPARATOR + response.length() + END_OF_MESSAGE + response;
                byteBuffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
            } else {
                byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
            }
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
