package parser;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static utils.Constants.*;

public final class HttpRequestParser {

    private static final String SPACE_CHARACTER = " ";

    public static HttpRequest parse(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        final var request = StandardCharsets.UTF_8.decode(byteBuffer).toString();
        final var lines = request.split(CRLF);

        final var requestLine = lines[0].split(SPACE_CHARACTER);
        final var method = requestLine[0];
        final var uri = requestLine[1];

        int i = 1;
        final var headers = new HashMap<String, String>();
        while (i < lines.length && !lines[i].isEmpty()) {
            final var header = lines[i].split(HEADER_SEPARATOR);
            headers.put(header[0], header[1]);
            i++;
        }

        String body = EMPTY_STRING;
        if (headers.containsKey(CONTENT_LENGTH)) {
            final var contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
            final var bodyBuilder = new StringBuilder();
            while (bodyBuilder.length() < contentLength && i < lines.length - 1) {
                bodyBuilder.append(lines[++i]);
            }
            body = bodyBuilder.toString();
        }
        return new HttpRequest(method, uri, headers, body);
    }
}
