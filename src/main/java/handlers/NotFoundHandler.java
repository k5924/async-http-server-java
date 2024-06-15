package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import static utils.Constants.NOT_FOUND_BYTES;

public final class NotFoundHandler implements ResponseHandler{

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel, final ByteBuffer byteBuffer) {
        byteBuffer.put(NOT_FOUND_BYTES);
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }
}
