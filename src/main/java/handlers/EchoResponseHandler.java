package handlers;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

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
            final String response_to_encode;
            if (encoding.equals("gzip")) {
                response_to_encode = GZIP_ENCODING_RESPONSE + response.length() + END_OF_MESSAGE + response;
            } else {
                response_to_encode = PLAIN_TEXT_RESPONSE + response.length() + END_OF_MESSAGE + response;
            }
            byteBuffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
        } else {
            byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        }
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }
}
