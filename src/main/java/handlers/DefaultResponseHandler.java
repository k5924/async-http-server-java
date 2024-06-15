package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import static utils.Constants.OK_RESPONSE_TERMINATION_BYTES;

public final class DefaultResponseHandler implements ResponseHandler{
    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {

        byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }
}
