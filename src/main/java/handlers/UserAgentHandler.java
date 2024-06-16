package handlers;

import utils.BufferPool;
import utils.HandlerTools;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

import static utils.Constants.*;

public final class UserAgentHandler implements ResponseHandler{
    private final String userAgent;
    private final BufferPool bufferPool;

    public UserAgentHandler(final String userAgent, final BufferPool bufferPool) {
        this.userAgent = userAgent;
        this.bufferPool = bufferPool;
    }

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {
        if (!userAgent.isEmpty()) {
            final var response_to_encode = PLAIN_TEXT_RESPONSE + userAgent.length() + END_OF_MESSAGE + userAgent;
            byteBuffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
        } else {
            byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        }
        HandlerTools.cleanupConnection(clientChannel, byteBuffer, bufferPool);
    }
}
