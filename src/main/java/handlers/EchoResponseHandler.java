package handlers;

import parser.HttpRequest;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;

import static utils.Constants.*;
import static utils.Constants.OK_RESPONSE_TERMINATION_BYTES;

public final class EchoResponseHandler implements ResponseHandler{

    private final String uri;

    public EchoResponseHandler(final String uri) {
        this.uri = uri;
    }

    @Override
    public void writeResponse(final AsynchronousSocketChannel clientChannel,
                              final ByteBuffer byteBuffer) {

        final var content = uri.split(LEADING_SLASH);
        if (content.length > 2) {
            final var response = content[2];
            final var response_to_encode = PLAIN_TEXT_RESPONSE + response.length() + END_OF_MESSAGE + response;
            byteBuffer.put(response_to_encode.getBytes(StandardCharsets.UTF_8));
        } else {
            byteBuffer.put(OK_RESPONSE_TERMINATION_BYTES);
        }
        byteBuffer.flip();
        clientChannel.write(byteBuffer, null, new FinishedHandler(clientChannel, byteBuffer));
    }
}
