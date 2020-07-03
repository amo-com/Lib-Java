package amo.lib.linq.delegate;

public interface Predicate<T> {
    boolean apply(T value);
}