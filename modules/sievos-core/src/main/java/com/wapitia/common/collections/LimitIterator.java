/*
 * Copyright 2016-2018 wapitia.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * Neither the name of wapitia.com or the names of contributors may be used to
 * endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 * WAPITIA.COM ("WAPITIA") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL WAPITIA OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * WAPITIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.wapitia.common.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Simple wrapping around another iterator imposing
 * some limit to how many elements are returned.
 * <p>
 * The client provides an {@link Iterator} that will be wrapped, and
 * a {link Predicate} whose job it is to cut off the iteration at a certain
 * index point. The predicate is provided the 0-based count of the
 * wrapped iterator's item that have been delivered so far,
 * and the predicate's job is to return false once a threshold is met.
 *
 * <p>
 * Example:
 * </p>
 * <pre>
 *
 *  {@code Iterator<String> } inputIter ...
 *  {@code Predicate<Long>  first10 = (Long ix) -> ix < 10L; }
 *  {@code Iterator<String> limIter = LimitIterator.<String> apply(inputIter, first10); }
 *
 * </pre>
 *
 * @param <T> iterator element item
 */
public class LimitIterator<T> implements Iterator<T> {

    private final Iterator<T> sourceIter;
    private final Predicate<Long> limitPredicate;

    // mutable current index, starts at zero and increments on every next()
    private long current;

    /**
     * Constructor takes a source iterator to wrap and a predicate
     * that checks against the current 0-based iterator index
     * to tell whether to advance.
     * Constructor is protected as clients should use the
     * {@link #apply(Iterator, Predicate)} factory method.
     *
     * @param sourceIter wrapped iterator must not be null.
     * @param limitPredicate the predicate checks against current index.
     */
    protected LimitIterator(final Iterator<T> sourceIter,
        final Predicate<Long> limitPredicate)
    {
        this.sourceIter = sourceIter;
        this.limitPredicate = limitPredicate;
        this.current = 0L;
    }

    /**
     * Return true only if the limitPredicate passes and the source iterator
     * hasNext is also true
     */
    @Override
    public boolean hasNext() {
        return limitPredicate.test(current) && sourceIter.hasNext();
    }

    /**
     * Return the next item from the source iterator.
     *
     * @return next item after having incremented our current count
     * @see java.util.Iterator#next()
     */
    @Override
    public T next() {
        this.current += 1L;
        return sourceIter.next();
    }

    /**
     * Static factory constructor method creates a LimitIterator.
     *
     * @param <TS> iterator item type
     *
     * @param sourceIter the Iterator wrapped and limited.
     * @param limitPredicate the condition on which to end iteration
     *        after a certain count
     * @return an {@code Iterator<TS>}
     */
    public static <TS> LimitIterator<TS> apply(final Iterator<TS> sourceIter,
            final Predicate<Long> limitPredicate)
    {
        Objects.requireNonNull(sourceIter);
        Objects.requireNonNull(limitPredicate);
        return new LimitIterator<TS>(sourceIter, limitPredicate);
    }

}