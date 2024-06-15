package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public final class RequestHandler implements CompletionHandler<Integer, ByteBuffer> {
    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer byteBuffer;

    public RequestHandler(final AsynchronousSocketChannel clientChannel, final ByteBuffer byteBuffer) {
        this.clientChannel = clientChannel;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void completed(final Integer result, final ByteBuffer attachment) {
        attachment.flip();
        final var message = StandardCharsets.UTF_8.decode(attachment);
        System.out.println("Message on request is " + message);
        byteBuffer.clear();
        byteBuffer.put("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        clientChannel.write(byteBuffer, byteBuffer, new CompletedHandler<>(clientChannel, byteBuffer));
    }

    @Override
    public void failed(final Throwable exc, final ByteBuffer attachment) {
        System.out.println("Failed to read request");

    }
}
