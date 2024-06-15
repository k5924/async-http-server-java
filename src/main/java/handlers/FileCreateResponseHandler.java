package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static utils.Constants.NOT_FOUND_BYTES;

public final class FileCreateResponseHandler implements ResponseHandler{

    private final String uri;
    private final String body;

    public FileCreateResponseHandler(final String uri, final String body) {
        this.uri = uri;
        this.body = body;
    }

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {
        final var contents = uri.split("/");
        if (contents.length > 2) {
            final var fileName = contents[2];
            final var filePath = Paths.get("/tmp/data/codecrafters.io/http-server-tester/" + fileName);
            try {
                final var fileChannel = AsynchronousFileChannel.open(filePath, StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE);
                final var fileBuffer = ByteBuffer.wrap(body.getBytes(StandardCharsets.UTF_8));
                fileBuffer.flip();
                fileChannel.write(fileBuffer, 0, null,
                        new FileWriteHandler(byteBuffer, clientChannel, fileChannel));
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
