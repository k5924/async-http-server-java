package utils;

import java.nio.charset.StandardCharsets;

public final class Constants {

    private Constants() {

    }

    public static final String OK= "HTTP/1.1 200 OK";
    public static final String END_OF_LINE = "\r\n";
    public static final String OK_RESPONSE = OK + END_OF_LINE + END_OF_LINE;
    public static final byte[] OK_RESPONSE_BYTES = OK_RESPONSE.getBytes(StandardCharsets.UTF_8);
}
