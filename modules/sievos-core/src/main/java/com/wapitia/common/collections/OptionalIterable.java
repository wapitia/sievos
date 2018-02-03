package com.wapitia.common.collections;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Provides an iterator for an Optional chain pattern where one link (or node)
 * in a chain holds an optional reference to the next node in the chain.
 *
 * <p>
 * Usage:
 * <pre>
 *  class Node implements {@code Iterable<String>} {
 *
 *      String value;
 *      {@code Optional<Node> next;      }
 *
 *      public Node(final String value, {@code final Optional<Node> next}) {
 *          this.value = value;
 *          this.next = next;
 *      }
 *
 *      public String getValue() {
 *          return value;
 *      }
 *
 *      public {@code Optional<Node> Optional<Node> getNext()} {
 *          return next;
 *      }
 *
 *      {@code @Override }
 *      public {@code Iterator<String>} iterator() {
 *          return <b>new {@code OptionalIterable<String,Node>}(Node::getValue, Node::getNext)</b>
 *              .iterator(this);
 *      }
 * }
 *
 *  ...
 *      void printIterNode(Node n) {
 *          for (String s : n)
 *              System.out.println(s);
 *      }
 * </pre>
 *
 * @param <T> Iteration item type
 * @param <LINK> Link node type in the chain
 */
public class OptionalIterable<T,LINK> {

	private final Function<LINK,T> getLinkValue;
	private final Function<LINK,Optional<LINK>> nextInChain;

	public OptionalIterable(
		final Function<LINK,T> getLinkValue,
		final Function<LINK,Optional<LINK>> nextInChain)
	{
		this.getLinkValue = Objects.requireNonNull(getLinkValue);
		this.nextInChain = Objects.requireNonNull(nextInChain);
	}

	class OnDeckIter implements Iterator<T> {

		// Mutuble iteration state.
		// while linkOnDeck is present the iterator has more items.
		private Optional<LINK> linkOnDeck;

		OnDeckIter(final LINK headLink) {
			this.linkOnDeck = Optional.<LINK> of(headLink);
		}

		@Override
		public boolean hasNext() {
			return linkOnDeck.isPresent();
		}

		@Override
		public T next() {
			final LINK curLink = linkOnDeck.get();
			final T result = OptionalIterable.this.getLinkValue.apply(curLink);
			// advance to next
			this.linkOnDeck = OptionalIterable.this.nextInChain.apply(curLink);
			return result;
		}
	}

	public  Iterator<T> iterator(final LINK initContainer) {
		return new OnDeckIter(initContainer);
	}

}