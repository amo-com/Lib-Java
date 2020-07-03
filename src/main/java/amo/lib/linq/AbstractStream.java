package amo.lib.linq;

import amo.lib.linq.delegate.*;
import amo.lib.linq.delegate.Comparator;

import java.util.*;

public abstract class AbstractStream<T> implements Stream<T> {

    protected Iterator<T> reverseIterator() {
        Deque<T> deque = new LinkedList<>();
        for (T t : this) deque.addFirst(t);
        return deque.iterator();
    }

    @Override
    public Stream<T> where(Predicate<T> predicate) {
        return new WhereStream<>(this, predicate);
    }

    @Override
    public <R> Stream<R> select(Selector<T, R> selector) {
        return new SelectStream<>(this, selector);
    }

    @Override
    public T find(Predicate<T> predicate)
    {
        return new FindStream<>(this, predicate).first();
        // return where(predicate).first();
    }

    @Override
    public List<T> findAll(Predicate<T> predicate)
    {
        return where(predicate).toList();
    }

    @Override
    public <K, E> Stream<Grouping<K, E>> groupBy(Selector<T, K> keySelector, Selector<T, E> elementSelector) {
        return new GroupByStream<>(this, keySelector, elementSelector);
    }

    @Override
    public <K> Stream<Grouping<K, T>> groupBy(Selector<T, K> keySelector) {
        return groupBy(keySelector, new Selector<T, T>() {
            @Override
            public T select(T t) {
                return t;
            }
        });
    }

    @Override
    public <R> OrderedStream<T> orderBy(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createAscending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderBy(Selector<T, R> keySelector) {
        return orderBy(keySelector, new Comparator<R>() {
            @Override
            public int compare(R r, R o) {
                return r.compareTo(o);
            }
        });
    }

    @Override
    public <R> OrderedStream<T> orderByDescending(Selector<T, R> keySelector, Comparator<R> comparator) {
        return OrderByStream.createDescending(this, keySelector, comparator);
    }

    @Override
    public <R extends Comparable<R>> OrderedStream<T> orderByDescending(final Selector<T, R> keySelector) {
        return orderByDescending(keySelector, new Comparator<R>() {
            @Override
            public int compare(R r, R o) {
                return r.compareTo(o);
            }
        });
    }

    @Override
    public <R> R aggregate(R seed, Aggregator<T, R> aggregator) {
        for (T t : this)
            seed = aggregator.aggregate(seed, t);
        return seed;
    }

    @Override
    public Stream<T> take(int count) {
        return new TakeStream<>(this, count);
    }

    @Override
    public Stream<T> skip(int count) {
        return new SkipStream<>(this, count);
    }

    @Override
    public Stream<T> distinct() {
        final HashSet<T> set = new HashSet<>();
        return where(new Predicate<T>() {
            @Override
            public boolean apply(T e) {
                return set.add(e);
            }
        });
    }

    @Override
    public int count() {
        return aggregate(0, new Aggregator<T, Integer>() {
            @Override
            public Integer aggregate(Integer a, T t) {
                return a + 1;
            }
        });
    }

    @Override
    public T first() {
        return iterator().next();
    }

    @Override
    public T first(Predicate<T> predicate) {
        return where(predicate).first();
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (T entry : this)
            list.add(entry);
        return list;
    }
}
