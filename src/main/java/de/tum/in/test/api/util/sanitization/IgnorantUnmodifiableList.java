package de.tum.in.test.api.util.sanitization;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.BiConsumer;

public final class IgnorantUnmodifiableList<E> extends AbstractList<E> implements RandomAccess {
	private final List<E> list;
	private final BiConsumer<String, Object> onModification;

	public IgnorantUnmodifiableList(List<E> original) {
		this(original, (method, object) -> {
			// no on modification action
		});
	}

	public IgnorantUnmodifiableList(List<E> original, BiConsumer<String, Object> onModification) {
		this.list = Objects.requireNonNull(original);
		this.onModification = Objects.requireNonNull(onModification);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		final Iterator<E> it = list.iterator();
		return new Iterator<>() {
			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public E next() {
				return it.next();
			}
		};
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean add(E e) {
		onModification.accept("add(E)", e);
		return false;
	}

	@Override
	public boolean remove(Object o) {
		onModification.accept("remove(Object)", o);
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		onModification.accept("addAll(Collection<? extends E>)", c);
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		onModification.accept("addAll(int, Collection<? extends E>)", c);
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		onModification.accept("removeAll(Collection<?>)", c);
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		onModification.accept("retainAll(Collection<?>)", c);
		return false;
	}

	@Override
	public void clear() {
		onModification.accept("clear()", null);
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		onModification.accept("set(int, E)", element);
		return null;
	}

	@Override
	public void add(int index, E element) {
		onModification.accept("add(int, E)", element);
	}

	@Override
	public E remove(int index) {
		onModification.accept("remove(int)", index);
		return null;
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		final ListIterator<E> it = list.listIterator(index);
		return new ListIterator<>() {

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public E next() {
				return it.next();
			}

			@Override
			public boolean hasPrevious() {
				return it.hasPrevious();
			}

			@Override
			public E previous() {
				return it.previous();
			}

			@Override
			public int nextIndex() {
				return it.nextIndex();
			}

			@Override
			public int previousIndex() {
				return it.previousIndex();
			}

			@Override
			public void remove() {
				IgnorantUnmodifiableList.this.remove(0);
			}

			@Override
			public void set(E e) {
				IgnorantUnmodifiableList.this.set(0, e);
			}

			@Override
			public void add(E e) {
				IgnorantUnmodifiableList.this.add(e);
			}
		};
	}

	@Override
	public String toString() {
		return list.toString();
	}

	public static <E> List<E> wrap(List<E> original) {
		return new IgnorantUnmodifiableList<>(original);
	}

	public static <E> List<E> wrapWith(List<E> original, BiConsumer<String, Object> onModification) {
		return new IgnorantUnmodifiableList<>(original, onModification);
	}
}
