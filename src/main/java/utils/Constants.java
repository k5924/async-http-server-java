package utils;

import java.nio.charset.StandardCharsets;

public final class Constants {

    private Constants() {

    }

    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String CRLF = "\r\n";
    public static final String END_OF_MESSAGE = CRLF + CRLF;
    public static final String OK_RESPONSE = HTTP_VERSION + " 200 OK" + END_OF_MESSAGE;
    public static final String NOT_FOUND = HTTP_VERSION + " 404 Not Found" + END_OF_MESSAGE;
    public static final byte[] OK_RESPONSE_BYTES = OK_RESPONSE.getBytes(StandardCharsets.UTF_8);
    public static final byte[] NOT_FOUND_BYTES = NOT_FOUND.getBytes(StandardCharsets.UTF_8);
    public static final String LEADING_SLASH = "/";
}
