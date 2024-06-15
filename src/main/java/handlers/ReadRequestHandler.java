package handlers;

import utils.BufferPool;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public final class ReadRequestHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel clientChannel;
    private final BufferPool bufferPool;

    public ReadRequestHandler(final AsynchronousSocketChannel clientChannel,
                              final BufferPool bufferPool) {
        this.clientChannel = clientChannel;
        this.bufferPool = bufferPool;
    }


    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        final var message = StandardCharsets.UTF_8.decode(attachment).toString();
        System.out.print("message sent on request is: " + message);
        try {
            final var byteBuffer = bufferPool.acquireBuffer();
            clientChannel.write(byteBuffer, null, new OkHandler(clientChannel, bufferPool, byteBuffer));
        } catch (final InterruptedException e) {
            System.err.println("Exception thrown on getting buffer: " + e.getMessage());
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
