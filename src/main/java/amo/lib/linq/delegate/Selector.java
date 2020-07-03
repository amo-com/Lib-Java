package amo.lib.linq.delegate;

public interface Selector<T, R> {
    R select(T value);
}
