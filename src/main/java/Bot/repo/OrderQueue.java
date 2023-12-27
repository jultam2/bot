package Bot.repo;

import java.util.LinkedList;
import java.util.Queue;

public class OrderQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int maxSize;

    public OrderQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void offer(T element) {
        if (queue.size() >= maxSize) {
            queue.poll();
        }
        queue.offer(element);
    }

    public T poll() {
        return queue.poll();
    }
}