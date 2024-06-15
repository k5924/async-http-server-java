package utils;

import java.nio.charset.StandardCharsets;

public final class Constants {

    private Constants() {

    }

    public static final String OK_RESPONSE = "HTTP/1.1 200 OK\r\n\r\n";
    public static final byte[] OK_RESPONSE_BYTES = OK_RESPONSE.getBytes(StandardCharsets.UTF_8);
}
