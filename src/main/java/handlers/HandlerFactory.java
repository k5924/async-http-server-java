package handlers;

import parser.HttpRequest;
import utils.BufferPool;

import static utils.Constants.*;

public final class HandlerFactory {

    private final BufferPool bufferPool;

    public HandlerFactory(final BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    public ResponseHandler getHandler(final HttpRequest request) {
        if (request.method().equals("GET")) {
            if (request.uri().equals(LEADING_SLASH)) {
                return new DefaultResponseHandler(bufferPool);
            } else if (request.uri().startsWith(ECHO_ENDPOINT)) {
                return new EchoResponseHandler(request.uri(), request.headers().getOrDefault(ACCEPT_ENCODING_HEADER, EMPTY_STRING), bufferPool);
            } else if (request.uri().startsWith(USER_AGENT_ENDPOINT)) {
                return new UserAgentHandler(request.headers().getOrDefault(USER_AGENT_HEADER, EMPTY_STRING), bufferPool);
            } else if (request.uri().startsWith(FILES_ENDPOINT)) {
                return new FileReadResponseHandler(request.uri(), bufferPool);
            } else {
                return new NotFoundHandler(bufferPool);
            }
        } else if (request.method().equals("POST")) {
            if (request.uri().startsWith(FILES_ENDPOINT)) {
                return new FileCreateResponseHandler(request.uri(), request.body(), bufferPool);
            } else {
                return new NotFoundHandler(bufferPool);
            }
        } else {
            return new NotFoundHandler(bufferPool);
        }
    }
}
