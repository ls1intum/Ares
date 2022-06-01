package de.tum.in.test.api.util;

import java.util.*;
import java.util.function.BiConsumer;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * This is a proxy for {@linkplain List}s to prevent modification silently.
 * While the lists obtained from {@linkplain List#copyOf(Collection)} or
 * {@linkplain Collections#unmodifiableList(List)} throw
 * {@linkplain UnsupportedOperationException}s on method calls that would change
 * the lists state, this class will intentionally <b>not</b> do this.
 * <p>
 * It is however possible to supply an <code>IgnorantUnmodifiableList</code>
 * with a {@linkplain BiConsumer} that gets supplied with the signature of the
 * called method and, if present, the (most important) argument passed to the
 * method.
 * <p>
 * Please note that this class was created for security reasons to prevent
 * modifications without causing exceptions. For creating regular unmodifiable
 * lists, {@link List#copyOf(Collection)} or
 * {@link Collections#unmodifiableList(List)} should always be preferred.
 *
 * @author Christian Femers
 * @see List#of(Object...)
 * @see List#copyOf(Collection)
 * @see Collections#unmodifiableList(List)
 * @param <E> Type of the elements in the List.
 */
@API(status = Status.INTERNAL)
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
		onModification.accept("add(E)", e); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean remove(Object o) {
		onModification.accept("remove(Object)", o); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		onModification.accept("addAll(Collection<? extends E>)", c); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		onModification.accept("addAll(int, Collection<? extends E>)", c); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		onModification.accept("removeAll(Collection<?>)", c); //$NON-NLS-1$
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		onModification.accept("retainAll(Collection<?>)", c); //$NON-NLS-1$
		return false;
	}

	@Override
	public void clear() {
		onModification.accept("clear()", null); //$NON-NLS-1$
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		onModification.accept("set(int, E)", element); //$NON-NLS-1$
		return null;
	}

	@Override
	public void add(int index, E element) {
		onModification.accept("add(int, E)", element); //$NON-NLS-1$
	}

	@Override
	public E remove(int index) {
		onModification.accept("remove(int)", index); //$NON-NLS-1$
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
			private boolean changeAllowed;

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public E next() {
				changeAllowed = true;
				return it.next();
			}

			@Override
			public boolean hasPrevious() {
				return it.hasPrevious();
			}

			@Override
			public E previous() {
				changeAllowed = true;
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
				checkChangeAction();
				changeAllowed = false;
				IgnorantUnmodifiableList.this.remove(it.nextIndex() - 1);
			}

			@Override
			public void set(E e) {
				checkChangeAction();
				IgnorantUnmodifiableList.this.set(0, e);
			}

			@Override
			public void add(E e) {
				changeAllowed = false;
				IgnorantUnmodifiableList.this.add(e);
			}

			private void checkChangeAction() {
				if (!changeAllowed)
					throw new IllegalStateException();
			}
		};
	}

	@Override
	public boolean equals(Object o) {
		return list.equals(o);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
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
