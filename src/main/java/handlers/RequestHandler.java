package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public final class RequestHandler implements CompletionHandler<Integer, Void> {

    private static final String OK_RESPONSE = "HTTP/1.1 200 0K\r\n\r\n";
    private static final byte[] OK_RESPONSE_BYTES = OK_RESPONSE.getBytes(StandardCharsets.UTF_8);
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

        byteBuffer.flip();
        final var data = new byte[bytesRead];
        byteBuffer.get(data);

        final var request = new String(data, StandardCharsets.UTF_8);
        System.out.println("Request is: " + request);

        byteBuffer.clear();
        byteBuffer.put(OK_RESPONSE_BYTES);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("Failed to read request: " + exc.getMessage());
    }
}
