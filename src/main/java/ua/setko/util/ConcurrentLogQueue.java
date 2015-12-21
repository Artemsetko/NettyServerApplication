package ua.setko.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @param <E>
 * @Author Artem Setko on 16.12.15.
 * <p/>
 * ConcurrentLinkedQueue implementation that holds only 16 values
 */
public final class ConcurrentLogQueue<E> implements Iterable<E> {
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
    private int counter = 0;

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    public synchronized boolean add(E e) {
        counter++;
        if (counter == 16) {
            queue.remove();
            counter--;
        }
        return queue.add(e);
    }

}