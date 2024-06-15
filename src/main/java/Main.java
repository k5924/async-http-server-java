import utils.BufferPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class Main {
  public static void main(final String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    final var bufferPool = new BufferPool(4, 1024);
    final var server = new Server(4221, bufferPool);
    try {
      server.startServer();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          server.stopServer();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
