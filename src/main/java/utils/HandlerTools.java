package utils;

import handlers.FinishedHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public final class HandlerTools {

    private HandlerTools() {

    }

    public static void cleanupConnection(final AsynchronousSocketChannel clientChannel, final ByteBuffer buffer) {
        buffer.flip();
        clientChannel.write(buffer, null, new FinishedHandler(clientChannel, buffer));
    }
}
