package handlers;

import utils.BufferPool;
import utils.HandlerTools;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import static utils.Constants.OK_RESPONSE_TERMINATION_BYTES;

public final class DefaultResponseHandler implements ResponseHandler{

    private final BufferPool bufferPool;

    public DefaultResponseHandler(final BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {

        byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        HandlerTools.cleanupConnection(clientChannel, byteBuffer, bufferPool);
    }
}
