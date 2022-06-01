package de.tum.in.test.api.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.Test;

class IgnorantUnmodifiableListTest {

	private static final String OBJ_A = "A";
	private static final String OBJ_B = "B";
	private static final String OBJ_C = "C";

	private List<String> source = List.of(OBJ_A, OBJ_B);
	private List<String> original = new ArrayList<>(source);
	private List<String> list = IgnorantUnmodifiableList.wrapWith(original, (s, o) -> changeDetected = true);

	private boolean changeDetected;

	@Test
	void testWrap() {
		list = IgnorantUnmodifiableList.wrap(original);
		assertThat(original).containsExactlyElementsOf(source);
	}

	@Test
	void testHashCode() {
		assertThat(list).hasSameHashCodeAs(source);
		assertFalse(changeDetected);
	}

	@Test
	void testSize() {
		assertThat(list).hasSameSizeAs(source);
		assertFalse(changeDetected);
	}

	@Test
	void testIsEmpty() {
		assertThat(list.isEmpty()).isEqualTo(source.isEmpty());
		assertFalse(changeDetected);
	}

	@Test
	void testClear() {
		list.clear();
		assertThat(original).containsExactlyElementsOf(source);
		assertTrue(changeDetected);
	}

	@Test
	void testContainsObject() {
		assertThat(list.contains(OBJ_A)).isTrue();
		assertThat(list.contains(OBJ_B)).isTrue();
		assertThat(list.contains(OBJ_C)).isFalse();
		assertFalse(changeDetected);
	}

	@Test
	void testIterator() {
		var it = list.iterator();
		assertThat(it).toIterable().containsExactlyElementsOf(source);
		assertFalse(changeDetected);
	}

	@Test
	void testToArray() {
		assertThat(list.toArray()).containsExactly(source.toArray());
		assertFalse(changeDetected);
	}

	@Test
	void testToArrayTArray() {
		assertThat(list.toArray(String[]::new)).containsExactly(source.toArray(String[]::new));
		assertFalse(changeDetected);
	}

	@Test
	void testAddE() {
		list.add(OBJ_C);
		assertThat(list).doesNotContain(OBJ_C);
		assertThat(source).doesNotContain(OBJ_C);
		assertTrue(changeDetected);
	}

	@Test
	void testRemoveObject() {
		list.remove(OBJ_A);
		assertThat(list).contains(OBJ_A);
		assertThat(source).contains(OBJ_A);
		assertTrue(changeDetected);
	}

	@Test
	void testContainsAllCollectionOfQ() {
		assertTrue(list.containsAll(source));
		assertFalse(changeDetected);
	}

	@Test
	void testAddAllCollectionOfQextendsE() {
		list.addAll(List.of(OBJ_C));
		assertThat(list).doesNotContain(OBJ_C);
		assertThat(source).doesNotContain(OBJ_C);
		assertTrue(changeDetected);
	}

	@Test
	void testAddAllIntCollectionOfQextendsE() {
		list.addAll(0, List.of(OBJ_C));
		assertThat(list).doesNotContain(OBJ_C);
		assertThat(source).doesNotContain(OBJ_C);
		assertTrue(changeDetected);
	}

	@Test
	void testRemoveAllCollectionOfQ() {
		list.removeAll(List.of(OBJ_A));
		assertThat(list).contains(OBJ_A);
		assertThat(source).contains(OBJ_A);
		assertTrue(changeDetected);
	}

	@Test
	void testRetainAllCollectionOfQ() {
		list.retainAll(List.of(OBJ_B));
		assertThat(list).contains(OBJ_A);
		assertThat(source).contains(OBJ_A);
		assertTrue(changeDetected);
	}

	@Test
	void testGetInt() {
		assertThat(list.get(0)).isEqualTo(OBJ_A);
		assertThat(list.get(1)).isEqualTo(OBJ_B);
		assertFalse(changeDetected);
	}

	@Test
	void testSetIntE() {
		list.set(1, OBJ_C);
		assertThat(list).doesNotContain(OBJ_C);
		assertThat(source).doesNotContain(OBJ_C);
		assertTrue(changeDetected);
	}

	@Test
	void testAddIntE() {
		list.add(1, OBJ_C);
		assertThat(list).doesNotContain(OBJ_C);
		assertThat(source).doesNotContain(OBJ_C);
		assertTrue(changeDetected);
	}

	@Test
	void testRemoveInt() {
		list.remove(0);
		assertThat(list).contains(OBJ_A);
		assertThat(source).contains(OBJ_A);
		assertTrue(changeDetected);
	}

	@Test
	void testIndexOfObject() {
		assertThat(list.indexOf(OBJ_A)).isEqualTo(source.indexOf(OBJ_A));
		assertThat(list.indexOf(OBJ_B)).isEqualTo(source.indexOf(OBJ_B));
		assertThat(list.indexOf(OBJ_C)).isEqualTo(source.indexOf(OBJ_C));
		assertFalse(changeDetected);
	}

	@Test
	void testLastIndexOfObject() {
		assertThat(list.lastIndexOf(OBJ_A)).isEqualTo(source.lastIndexOf(OBJ_A));
		assertThat(list.lastIndexOf(OBJ_B)).isEqualTo(source.lastIndexOf(OBJ_B));
		assertThat(list.lastIndexOf(OBJ_C)).isEqualTo(source.lastIndexOf(OBJ_C));
		assertFalse(changeDetected);
	}

	@Test
	void testListIterator() {
		var lit = list.listIterator();
		assertThat(lit.hasNext()).isTrue();
		assertThat(lit.nextIndex()).isZero();
		assertThat(lit.next()).isEqualTo(OBJ_A);
		lit.remove();
		assertTrue(changeDetected);

		changeDetected = false;
		assertThat(lit.hasNext()).isTrue();
		assertThat(lit.nextIndex()).isEqualTo(1);
		assertThat(lit.next()).isEqualTo(OBJ_B);
		lit.add(OBJ_C);
		assertTrue(changeDetected);

		changeDetected = false;
		assertThatThrownBy(() -> lit.set(OBJ_C)).isInstanceOf(IllegalStateException.class);
		assertThat(lit.hasNext()).isFalse();
		assertThat(lit.hasPrevious()).isTrue();
		assertThat(lit.previousIndex()).isEqualTo(1);
		assertThat(lit.previous()).isEqualTo(OBJ_B);
		assertThat(lit.previousIndex()).isZero();
		lit.set(OBJ_C);
		assertTrue(changeDetected);

		assertThat(list).containsExactlyElementsOf(source);
		assertThat(original).containsExactlyElementsOf(source);
	}

	@Test
	void testEqualsObject() {
		assertThat(list).isEqualTo(source);
		assertFalse(changeDetected);
	}

	@Test
	void testToString() {
		assertThat(list).hasToString(source.toString());
		assertFalse(changeDetected);
	}
}
