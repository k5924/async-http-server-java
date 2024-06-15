package utils;

import java.nio.charset.StandardCharsets;

public final class Constants {

    private Constants() {

    }

    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String CRLF = "\r\n";
    public static final String END_OF_MESSAGE = CRLF + CRLF;
    public static final String CONTENT_TYPE = "Content-Type: ";
    public static final String TEXT_CONTENT = "text/plain" + CRLF;
    public static final String FILE_CONTENT = "application/octet-stream" + CRLF;
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_SEPARATOR = ": ";
    public static final String OK_RESPONSE = HTTP_VERSION + " 200 OK" + CRLF;
    public static final String OK_RESPONSE_TERMINATION = HTTP_VERSION + " 200 OK" + END_OF_MESSAGE;
    public static final String NOT_FOUND = HTTP_VERSION + " 404 Not Found" + END_OF_MESSAGE;
    public static final byte[] OK_RESPONSE_TERMINATION_BYTES = OK_RESPONSE_TERMINATION.getBytes(StandardCharsets.UTF_8);
    public static final byte[] NOT_FOUND_BYTES = NOT_FOUND.getBytes(StandardCharsets.UTF_8);
    public static final String LEADING_SLASH = "/";
    public static final String ECHO_ENDPOINT = LEADING_SLASH + "echo";
    public static final String USER_AGENT_ENDPOINT = LEADING_SLASH + "user-agent";
    public static final String FILES_ENDPOINT = LEADING_SLASH + "files";
    public static final String USER_AGENT_HEADER = "User-Agent";
    public static final String EMPTY_STRING = "";
    public static final String PLAIN_TEXT_RESPONSE = OK_RESPONSE + CONTENT_TYPE + TEXT_CONTENT + CONTENT_LENGTH + HEADER_SEPARATOR;
    public static final String FILE_RESPONSE = OK_RESPONSE + CONTENT_TYPE + FILE_CONTENT + CONTENT_LENGTH + HEADER_SEPARATOR;
    public static final String CREATED_RESPONSE = HTTP_VERSION + " 201 Created" + END_OF_MESSAGE;
}
