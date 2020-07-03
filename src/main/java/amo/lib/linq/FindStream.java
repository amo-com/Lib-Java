package amo.lib.linq;

import amo.lib.linq.delegate.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * copy from wherestream
 * the diff is evaluateNext() method
 * @param <T>
 */
class FindStream<T> extends AbstractStream<T> {
    private final AbstractStream<T> stream;
    private final Predicate<T> predicate;

    FindStream(AbstractStream<T> stream, Predicate<T> predicate) {
        this.stream = stream;
        this.predicate = predicate;
    }

    @Override
    public Iterator<T> iterator() {
        return new FindIterator<>(stream.iterator(), predicate);
    }

    @Override
    protected Iterator<T> reverseIterator() {
        return new FindIterator<>(stream.reverseIterator(), predicate);
    }

    private static class FindIterator<T> implements Iterator<T> {
        private final Iterator<T> wrapped;
        private final Predicate<T> predicate;

        private T next;
        private boolean nextEvaluated;
        private Boolean hasFind;

        public FindIterator(Iterator<T> wrapped, Predicate<T> predicate) {
            this.wrapped = wrapped;
            this.predicate = predicate;
            this.hasFind = false;
        }

        @Override
        public boolean hasNext() {
            /**
             * only match once
             */
            if (hasFind)
                return false;

            evaluateNext();

            hasFind = next != null;
            return hasFind;
        }

        @Override
        public T next() {
            evaluateNext();

            nextEvaluated = false;

            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        private void evaluateNext() {
            if (nextEvaluated)
                return;

            next = null;
            while (wrapped.hasNext() && next == null) {
                T entry = wrapped.next();
                if (predicate.apply(entry))
                    next = entry;
            }

            nextEvaluated = true;
        }
    }
}
