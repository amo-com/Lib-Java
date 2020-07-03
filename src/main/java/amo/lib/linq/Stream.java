package amo.lib.linq;

import amo.lib.linq.delegate.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Decorates {@code Iterable<T>} objects to enable use of Linq like expressions.
 *
 * @param <T> the type of the wrapped {@code Iterable<T>}
 */
public interface Stream<T> extends Iterable<T> {

    /**
     * Filters a sequence of values based on a predicate.
     *
     * @param predicate A function to test each element for a condition.
     * @return An Stream of type T that contains elements from the input sequence that satisfy the condition.
     */
    Stream<T> where(Predicate<T> predicate);

    /**
     * Projects each element of a sequence into a new form.
     *
     * @param selector  A transform function to apply to each element.
     * @param <TResult> The type of the value returned by selector.
     * @return An Stream of type TResult whose elements are the result of invoking the transform function on each element of source.
     */
    <TResult> Stream<TResult> select(Selector<T, TResult> selector);

    /**
     * find目前使用的where,可以为find添加独立的Stream支持,找到一个equ时就终止next,修改where,在next时记录是否已经有匹配的,如果有了,hasnext返回
     * false,反正find就只返回一个
     * @param predicate
     * @return
     */
    T find(Predicate<T> predicate);

    List<T> findAll(Predicate<T> predicate);

    /**
     * Groups the elements of a sequence according to a specified key selector function.
     *
     * @param keySelector     A function to extract the key for each source element.
     * @param elementSelector A function to map each source element to an element into a Grouping.
     * @param <TKey>          The type of the key returned by keySelector.
     * @param <TElement>      The type of the element returned by elementSelector.
     * @return An Stream of type Grouping where each Grouping object contains a sequence of objects and a key (TKey, TResult respectively).
     */
    <TKey, TElement> Stream<Grouping<TKey, TElement>> groupBy(Selector<T, TKey> keySelector, Selector<T, TElement> elementSelector);

    /**
     * Groups the elements of a sequence according to a specified key selector function.
     *
     * @param keySelector A function to extract the key for each source element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type Grouping where each Grouping object contains a sequence of objects and a key (TKey, TResult respectively).
     */
    <TKey> Stream<Grouping<TKey, T>> groupBy(Selector<T, TKey> keySelector);

    /**
     * Sorts the elements of a sequence in ascending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    <TKey> OrderedStream<T> orderBy(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Sorts the elements of a sequence in ascending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    <TKey extends Comparable<TKey>> OrderedStream<T> orderBy(Selector<T, TKey> keySelector);

    /**
     * Sorts the elements of a sequence in descending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param comparator  An Comparator to compare keys.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    <TKey> OrderedStream<T> orderByDescending(Selector<T, TKey> keySelector, Comparator<TKey> comparator);

    /**
     * Sorts the elements of a sequence in descending order according to a key.
     *
     * @param keySelector A function to extract a key from an element.
     * @param <TKey>      The type of the key returned by keySelector.
     * @return An Stream of type T whose elements are sorted according to a key.
     */
    <TKey extends Comparable<TKey>> OrderedStream<T> orderByDescending(Selector<T, TKey> keySelector);

    /**
     * Applies an accumulator function over a sequence.
     * The specified seed value is used as the initial accumulator value,
     * and the specified function is used to select the result value.
     *
     * @param seed          The initial accumulator value.
     * @param aggregator    An accumulator function to be invoked on each element.
     * @param <TAccumulate> The type of the accumulator value.
     * @return The transformed final accumulator value.
     */
    <TAccumulate> TAccumulate aggregate(TAccumulate seed, Aggregator<T, TAccumulate> aggregator);

    /**
     * Returns a specified number of contiguous elements from the start of a sequence.
     *
     * @param count The number of elements to return.
     * @return An Stream of type T that contains the specified number of elements from the start of the input sequence.
     */
    Stream<T> take(int count);

    /**
     * Skips a specified number of contiguous elements from the start of a sequence then returns remaining elements.
     *
     * @param count The number of elements to skip.
     * @return An Stream of type T that contains the elements after the number of elements skipped from the start of the input sequence.
     */
    Stream<T> skip(int count);

    /**
     * Returns distinct elements from a sequence by using the object {@code equals()} to compare values.
     *
     * @return An Stream of type T that contains distinct elements from the source sequence.
     */
    Stream<T> distinct();

    /**
     * Returns the number of elements in a sequence.
     *
     * @return The number of elements in the input sequence.
     */
    int count();

    /**
     * Creates a List from a Stream.
     *
     * @return A List of type T that contains elements from the input sequence.
     */
    List<T> toList();

    /**
     * Returns the first element of a sequence; this method throws an exception if the sequence is empty.
     *
     * @return The first element in the specified sequence.
     * @throws NoSuchElementException if the sequence is empty.
     */
    T first();

    /**
     * Returns the first element in a sequence that satisfies the specified condition; this method throws
     * an exception if no element in the sequence satisfies the condition.
     *
     * @param predicate A function to test each element for a condition.
     * @return The first element in the sequence that passes the test in the specified predicate function.
     * @throws NoSuchElementException if no element in the sequence satisfies the condition.
     */
    T first(Predicate<T> predicate);

}
