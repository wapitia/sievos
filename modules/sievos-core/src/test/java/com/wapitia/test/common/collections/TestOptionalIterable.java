/**
 *
 */
package com.wapitia.test.common.collections;

import java.util.Iterator;
import java.util.Optional;

import org.junit.Test;

import com.wapitia.common.collections.OptionalIterable;

/**
 *
 */
public class TestOptionalIterable {

    class Node implements Iterable<String> {

         String value;
         Optional<Node> next;

         public Node(final String value, final Optional<Node> next) {
           this.value = value;
           this.next = next;
         }

         public String getValue() { return value; }

         public Optional<Node> getNext() { return next; }

         @Override
         public Iterator<String> iterator() {
             final OptionalIterable<String,Node> iterable =
                 new OptionalIterable<String,Node> (
                     Node::getValue,
                     Node::getNext);
             return iterable.iterator(this);
         }

    }
    void printIterNode(final Node n) {
        for (final String l : n) {
            System.out.println(l);
        }
    }

    @Test
    public void callMakeIter() {
        final Node n = new Node("A",
            Optional.of(new Node("B",
                Optional.of(new Node("C",
                    Optional.<Node> empty())))));

         printIterNode(n);

    }

}
