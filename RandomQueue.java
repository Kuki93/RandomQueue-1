package RandomQueue;

import java.util.*;

/**
 * A data structure called a “random queue”, which supports insertion, deletion of a uniformly random element,
 * and iteration in random order.
 *
 * This is basically exercises 1.3.35 and 1.3.36 in the book:
 *
 * "Hint: use an array representation (with resizing).
 * To remove an item, swap one at a random position (indexed 0 through N-1)
 * with the one at the last position (index N-1).
 * Then delete and return the last object, as in ResizingArrayStack."
 *
 */
public class RandomQueue<E> implements Iterable<E> {

    E[] a;  // internal array
    int N;  // pointer to current index

    public RandomQueue() {
        a = (E[]) new Object[10];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void enqueue(E item) {
        // double array when required
        if (N == a.length) {
            resize(a.length * 2);
        }

        a[N] = item;  // add item at current pointer
        N++;  // then increment the pointer
    }

    private void resize(int size) {
        // copy into temporary array
        E[] temp = (E[]) new Object[size];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }

        // de-reference current array in favour of new array
        a = temp;
    }

    public E sample() {
        // random item from index within current bounds
        int r = StdRandom.uniform(N);
        return a[r];
    }

    public E dequeue() {
        // random item from index within current bounds
        int r = StdRandom.uniform(N);
        E randomItem = a[r];

        // decrement the pointer
        N--;

        // move the current last item to the random position
        a[r] = a[N];

        // remove reference to allow for garbage collection
        a[N] = null;

        // cut array in half when required (at 25% full in this case)
        if (N > 0 && N == a.length/4) {
            resize(a.length / 2);
        }

        return randomItem;
    }

    public Iterator<E> iterator() {
        return new RandomIterator<E>();
    }

    public String toString() {
        return Arrays.toString(a);
    }

    /*
        It is legal for a client to have two (or a thousand) iterators over the same RandomQueue;
        each iterator should use its own random order.
        You may assume that whenever the data structure is changed (with enqueue or dequeue),
        any iterators that were created before the change are discarded.
    */
    private class RandomIterator<E> implements Iterator<E> {
        RandomQueue<Integer> indexQueue;

        public RandomIterator() {
            // an internal RandomQueue keeps track of remaining indexes
            indexQueue = new RandomQueue<Integer>();

            // since all enqueue() operations are constant time, this operation takes linear time N
            for (int i = 0; i < N; i++) {
                indexQueue.enqueue(i);
            }
        }

        public boolean hasNext() {
            return !indexQueue.isEmpty();
        }

        public E next() {
            return (E) a[indexQueue.dequeue()];  // the cast to E is apparently necessary
        }

        public void remove() {}  // N/A
    }
}
