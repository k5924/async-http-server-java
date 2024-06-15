package utils;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public final class BufferPool {
    private final BlockingQueue<ByteBuffer> pool;
    private final int bufferSize;

    public BufferPool(final int poolSize, final int bufferSize) {
        this.bufferSize = bufferSize;
        this.pool = new ArrayBlockingQueue<>(poolSize);
        initializeBufferPool(poolSize);
    }

    private void initializeBufferPool(final int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            pool.add(ByteBuffer.allocateDirect(bufferSize));
        }
    }

    public ByteBuffer acquireBuffer() throws InterruptedException {
        return pool.take();
    }

    public void releaseBuffer(final ByteBuffer buffer) throws InterruptedException {
        pool.put(buffer);
    }
}
