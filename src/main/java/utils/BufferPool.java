package utils;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class BufferPool {

    private final BlockingQueue<ByteBuffer> bufferPool;

    public BufferPool(final int bufferSize, final int numberOfBuffers) {
        this.bufferPool = new ArrayBlockingQueue<>(numberOfBuffers);
        for (int i = 0; i < numberOfBuffers; i++) {
            bufferPool.add(ByteBuffer.allocateDirect(bufferSize));
        }
    }

    public void returnToPool(final ByteBuffer byteBuffer) throws InterruptedException {
        byteBuffer.clear();
        bufferPool.put(byteBuffer);
    }

    public ByteBuffer getBuffer() throws InterruptedException {
        return bufferPool.take();
    }
}
