package de.SweetCode.e.utils.log;

import de.SweetCode.e.utils.Assert;
import de.SweetCode.e.utils.ToString.ToStringBuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * A LifeCycleQueue keeps only n items in its queue, if
 * new items are added than the queue will remove its head
 * and append the new item.
 * @param <E>
 */
public class LifeCycleQueue<E> implements Queue<E> {

    private final Queue<E> items = new LinkedTransferQueue<>();
    private final int capacity;

    public LifeCycleQueue(int capacity) {

        Assert.assertTrue("The capacity cannot be less than one.", (capacity > 0));
        this.capacity = capacity;

    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.items.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.items.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.items.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.items.toArray(a);
    }

    @Override
    public boolean add(E e) {

        if(this.size() >= this.capacity) {
            this.items.poll();
        }

        this.items.add(e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.items.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public void clear() {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public boolean offer(E e) {
        return this.add(e);
    }

    @Override
    public E remove() {
        throw new IllegalAccessError("This queue is write and read only.");
    }

    @Override
    public E poll() {
        return this.items.poll();
    }

    @Override
    public E element() {
        return this.items.element();
    }

    @Override
    public E peek() {
        return this.items.peek();
    }

    @Override
    public String toString() {
        return ToStringBuilder.create(this)
                .append("capacity", this.capacity)
                .append("items", this.items)
            .build();
    }

}
