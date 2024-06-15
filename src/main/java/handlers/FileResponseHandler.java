package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static utils.Constants.*;

public final class FileResponseHandler implements ResponseHandler{
    private final String uri;

    public FileResponseHandler(final String uri) {
        this.uri = uri;
    }

    @Override
    public void writeResponse(AsynchronousSocketChannel clientChannel, ByteBuffer byteBuffer) {
        final var contents = uri.split("/");
        if (contents.length > 2) {
            final var fileName = contents[2];
            final var filePath = Paths.get("/tmp/" + fileName);
            try {
                final var fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.READ);
                final var fileBuffer = ByteBuffer.allocate((int) fileChannel.size());
                fileChannel.read(fileBuffer, 0, null,
                        new FileReadHandler(fileBuffer, byteBuffer, clientChannel, fileChannel));
            } catch (final Exception e) {
                System.err.println("Error opening file: " + e.getMessage());
                byteBuffer.put(NOT_FOUND_BYTES);
                byteBuffer.flip();
                clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
            }
        } else {
            byteBuffer.put(NOT_FOUND_BYTES);
            byteBuffer.flip();
            clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));;
        }
    }
}
