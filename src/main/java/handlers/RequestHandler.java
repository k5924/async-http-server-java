package handlers;

import parser.HttpRequestParser;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public final class RequestHandler implements CompletionHandler<Integer, Void> {

    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer byteBuffer;
    private final HandlerFactory handlerFactory;

    public RequestHandler(final AsynchronousSocketChannel clientChannel,
                          final ByteBuffer byteBuffer,
                          final HandlerFactory handlerFactory) {
        this.clientChannel = clientChannel;
        this.byteBuffer = byteBuffer;
        this.handlerFactory = handlerFactory;
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

        final var handler = handlerFactory.getHandler(request);
        handler.writeResponse(clientChannel, byteBuffer);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        System.out.println("Failed to read request: " + exc.getMessage());
    }
}
