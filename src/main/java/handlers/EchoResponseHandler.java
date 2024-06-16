package handlers;

import utils.HandlerTools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

import static utils.Constants.*;
import static utils.Constants.OK_RESPONSE_TERMINATION_BYTES;

public final class EchoResponseHandler implements ResponseHandler{

    private final String uri;
    private final String encoding;

    public EchoResponseHandler(final String uri, final String encoding) {
        this.uri = uri;
        this.encoding = encoding;
    }

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {

        final var content = uri.split(LEADING_SLASH);
        if (content.length > 2) {
            final var response = content[2];
            if (encoding.contains("gzip")) {
                final var outputStream = new ByteArrayOutputStream();
                try {
                    final var gzipOutputStream = new GZIPOutputStream(outputStream);
                    gzipOutputStream.write(response.getBytes(StandardCharsets.UTF_8));
                    gzipOutputStream.close();
                    final var encodedBytes = outputStream.toByteArray();
                    final var response_to_encode = GZIP_ENCODING_RESPONSE + encodedBytes.length + END_OF_MESSAGE;
                    byteBuffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
                    byteBuffer.put(encodedBytes);
                } catch (final IOException e) {
                    System.err.println("Unable to encode data with gzip encoder: " + e.getMessage());
                    encodeMessageNormally(response, byteBuffer);
                }
            } else {
                encodeMessageNormally(response, byteBuffer);
            }
        } else {
            byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        }
        HandlerTools.cleanupConnection(clientChannel, byteBuffer);
    }

    private void encodeMessageNormally(final String response, final ByteBuffer buffer) {
        final var response_to_encode = PLAIN_TEXT_RESPONSE + response.length() + END_OF_MESSAGE + response;
        buffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
    }
}
