package amo.lib.linq.delegate;

public interface Aggregator<T, R> {
    R aggregate(R r, T t);
}
