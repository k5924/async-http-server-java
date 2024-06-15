package handlers;

import utils.BufferPool;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static utils.Constants.OK_RESPONSE_BYTES;

public final class OkHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel clientChannel;
    private final BufferPool bufferPool;
    private final ByteBuffer byteBuffer;

    public OkHandler(final AsynchronousSocketChannel clientChannel, final BufferPool bufferPool,
                     final ByteBuffer byteBuffer) {
        this.clientChannel = clientChannel;
        this.bufferPool = bufferPool;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void completed(final Integer result, final ByteBuffer attachment) {
        byteBuffer.clear();
        byteBuffer.put(OK_RESPONSE_BYTES);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new CompletedHandler(clientChannel, byteBuffer, bufferPool));
    }

    @Override
    public void failed(final Throwable exc, final ByteBuffer attachment) {
        System.out.println("Failed to send response: " + exc.getMessage());
    }
}
