import utils.BufferPool;

public final class Main {
  public static void main(final String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    final var bufferPool = new BufferPool(1024, 256);
    final var server = new Server(4221, bufferPool);
    try {
      server.startServer();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          server.stopServer();
        } catch (final Exception e) {
          System.err.println("Failed to stop the server because: " + e.getMessage());
        }
      }));
    } catch (final Exception e) {
      System.err.println("Failed to start the server because: " + e.getMessage());
    }
  }
}
