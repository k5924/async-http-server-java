package parser;

import java.util.HashMap;

public record HttpRequest(String method, String uri, HashMap<String, String> headers, String body) {
}
