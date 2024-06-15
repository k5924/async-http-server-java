package handlers;

import parser.HttpRequest;

import static utils.Constants.*;

public final class HandlerFactory {

    public ResponseHandler getHandler(final HttpRequest request) {
        if (request.uri().equals(LEADING_SLASH)) {
            return new DefaultResponseHandler();
        } else if (request.uri().startsWith(ECHO_ENDPOINT)){
            return new EchoResponseHandler(request.uri());
        } else if (request.uri().startsWith(USER_AGENT_ENDPOINT)) {
            return new UserAgentHandler(request.headers().getOrDefault(USER_AGENT_HEADER, EMPTY_STRING));
        } else if (request.uri().startsWith(FILES_ENDPOINT)) {
            return new FileResponseHandler(request.uri());
        } else {
            return new NotFoundHandler();
        }
    }
}
