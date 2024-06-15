package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public interface ResponseHandler {

    void writeResponse(final AsynchronousSocketChannel clientChannel, final ByteBuffer byteBuffer);
}
