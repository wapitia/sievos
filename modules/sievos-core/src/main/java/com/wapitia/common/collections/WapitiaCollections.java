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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Collection of collection patterns.
 */
public interface WapitiaCollections {

    /**
     * Put a key/value pair into a Map-Collection structure having the
     * common usage pattern:  {@code Map<K,List<V>> }
     *
     * This puts a new value into that collection in the map, creating a new
     * {@link ArrayList} for the key's list, if needed.
     * This prevents the developer from having to deal with {@code null}s,
     * since this function does it.
     *
     * @param <K> map's key type
     * @param <LV> list's value's type
     *
     * @param collectionMap mutable  Map of Lists: {@code Map<K,List<LV>> }
     * @param key key into the Map
     * @param newItem item to add to the key entry's list value.
     * @see #add(Map, Object, Object, Supplier)
     */
    static <K,LV> void add(final Map<K,Collection<LV>> collectionMap,
        final K key, final LV newItem)
    {
        add(collectionMap, key, newItem, ArrayList<LV>::new);
    }

    /**
     * Put a key/value pair into a Map-List structure having the
     * common usage pattern:  {@code Map<K,Collection<V>> }
     *
     * <p>
     * This is just like {@link #add(Map, Object, Object)} above,
     * but this method also takes a List constructor when ArrayList::new
     * just won't do.
     * </p>
     *
     * @param <K> The Map's key's type
     * @param <LV> The Map's Value's Collection type
     *
     * @param collectionMap a Map of Collections
     * @param key the key into mapOfCollections re
     * @param listItem item to append to the collection at the
     *        map's key entry
     * @param listCtor Collection construction action, taken once if necessary.
     * @see #add(Map, Object, Object)
     */
    static <K,LV> void add(final Map<K,Collection<LV>> collectionMap,
            final K key, final LV listItem,
            final Supplier<Collection<LV>> listCtor)
    {
        mapGetOrCreate(collectionMap, key, listCtor).add(listItem);
    }

    /**
     * Apply some action consumer on every item in a Map-List's entry.
     * If that entry does not exist, nothing happens.
     *
     * @param <K> map's key type
     * @param <LV> list's value's type
     *
     * @param collectionMap a Map of Collections
     * @param key the key into mapOfCollections re
     * @param action Each item is given by this consumer for action
     */
    static <K,LV> void forEach(final Map<K,Collection<LV>> collectionMap,
            final K key, final Consumer<LV> action)
    {
        Optional.<Collection<LV>> ofNullable(collectionMap.get(key))
                .ifPresent(list -> list.forEach(action));
    }

    /**
     * Fetches a value from the supplied map, creating a value on the fly
     * if the map hadn't already contained it.
     *
     * <p>
     * The attempt is first made to get the value out of the map and return it.
     * If the value already exists, the supplied map is not changed and the
     * value supplier never gets called.
     *
     * If the value does not yet contain this value (that is when
     * {@link Map#get} returns null), then the supplied value supplier is
     * invoked, and the value from that supplier is both added to the map
     * at the K entry and then returned.
     * .
     *
     * @param <K> Map's key type
     * @param <V> Map's value type
     *
     * @param map Mutable map. The key entry will be added to the map
     *            if it does not yet exist
     * @param key s key to the value's entry in the map
     * @param newValueSupplier provider of the default value if it already
     *        doesn't exist in the map
     * @return the value from the map, which may have been newly inserted
     *         here
     */
    static <K,V> V mapGetOrCreate(final Map<K,V> map, final K key,
        final Supplier<? extends V> newValueSupplier)
    {
        return Optional.<V> ofNullable(map.get(key))
            .orElseGet(() -> mapPut(map, key, newValueSupplier.get()));
    }

    /**
     * Put a new value '{@code item}' into the supplied map at the entry
     * given by {@code key}.
     * Any existing value in the map is replaced and discarded.
     * The new value is returned.
     *
     * @param <K> Map's key type
     * @param <V> Map's value type
     *
     * @param map the container to alter
     * @param key the key of the map entry to change or add.
     * @param item new key's value for the map
     * @return item
     */
    static <K,V> V mapPut(final Map<K,V> map, final K key, final V item) {
        map.put(key, item);  // ignore results of map.put, the old value
        return item;
    }

    /**
     * An iterator of the bits in the supplied BitSet, counting from bit 0
     * and working upward, no limit
     * @param bitSet Iterator of this BitSet, which is not changed
     * @return an {@code Iterator<Boolean>}
     */
    static BitSetIterator bitSetIterator(final BitSet bitSet) {
        return new BitSetIterator(bitSet);
    }

    /**
     * Limit the size of the BitSet iterator of the given BitSet
     *
     * @param bitSet BitSet through whose bits of which we shall iterate
     * @param size limit BitSet size
     * @return Iterator of Boolean
     */
    static LimitIterator<Boolean> bitSetIterator(final BitSet bitSet,
        final int size)
    {
        final LimitIterator<Boolean> result =
            WapitiaCollections.<Boolean> limitIterator(
                new BitSetIterator(bitSet), (long) size);
        return result;
    }

    /**
     * Create and return an iterator with a hard upper limit on the number
     * of items it will return.
     *
     * @param <T> The type of the items delivered by this and the source
     *            iterator
     * @param sourceIter Iterator that will be wrapped and limited.
     * @param limit Count of maximum times {@link Iterator#next next()} may
     *        be called using this newly generated iterator
     * @return an {@code Iterator<T>}
     */
    static <T> LimitIterator<T> limitIterator(final Iterator<T> sourceIter,
        final long limit)
    {
        return LimitIterator.<T> apply(sourceIter, index -> index < limit);
    }

}
