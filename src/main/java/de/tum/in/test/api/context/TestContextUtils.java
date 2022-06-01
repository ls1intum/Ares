package de.tum.in.test.api.context;

import static org.junit.platform.commons.support.AnnotationSupport.*;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.platform.commons.support.AnnotationSupport;

/**
 * Static utility methods for analyzing {@link TestContext}s and finding
 * Annotations.
 *
 * @author Christian Femers
 */
@API(status = Status.MAINTAINED)
public final class TestContextUtils {

	private TestContextUtils() {
	}

	/**
	 * Finds the first occurrence of A in the {@link TestContext} in the following
	 * order:
	 * <ul>
	 * <li>test method, if applicable</li>
	 * <li>the test class enclosing the method, if applicable (and superclasses, if
	 * {@link Inherited})</li>
	 * <li>recursively: the classes enclosing the test class, if applicable (and
	 * superclasses, if {@link Inherited})</li>
	 * </ul>
	 *
	 * @param <A>        The type of the annotation
	 * @param context    the {@link TestContext} to search in
	 * @param annotation the annotation to look for
	 * @return the first occurrence of <code>A</code> in the described order as
	 *         Optional, never null
	 * @see AnnotationSupport#findAnnotation(AnnotatedElement, Class)
	 * @author Christian Femers
	 */
	public static <A extends Annotation> Optional<A> findAnnotationIn(TestContext context, Class<A> annotation) {
		return getAnnotatedElementsInnermostFirst(context).map(e -> findAnnotation(e, annotation))
				.filter(Optional::isPresent).map(Optional::get).findFirst();
	}

	/**
	 * Finds all occurrences of A in the {@link TestContext} and returns them as
	 * {@link Stream} in the following order:
	 * <ul>
	 * <li>test method, if applicable</li>
	 * <li>the test class enclosing the method, if applicable (and superclasses, if
	 * {@link Inherited})</li>
	 * <li>recursively: the classes enclosing the test class, if applicable (and
	 * superclasses, if {@link Inherited})</li>
	 * </ul>
	 *
	 * @param <A>        The type of the annotation
	 * @param context    the {@link TestContext} to search in
	 * @param annotation the annotation to look for
	 * @return all occurrences of <code>A</code> in the described order as Stream,
	 *         never null
	 * @see AnnotationSupport#findRepeatableAnnotations(AnnotatedElement, Class)
	 * @author Christian Femers
	 */
	public static <A extends Annotation> Stream<A> findRepeatableAnnotationsIn(TestContext context,
			Class<A> annotation) {
		return getAnnotatedElementsInnermostFirst(context)
				.flatMap(e -> findRepeatableAnnotations(e, annotation).stream());
	}

	/**
	 * Returns a {@link Stream} of all classes enclosing the test class of the
	 * {@link TestContext}, innermost first.
	 *
	 * @param context the {@link TestContext} to search in
	 * @return a stream containing all nested classes in order, or an empty stream
	 *         if no test class is available
	 * @author Christian Femers
	 */
	public static Stream<Class<?>> getClassNestingInnermostFirst(TestContext context) {
		return Stream.<Class<?>>iterate(context.testClass().orElse(null), Class::getEnclosingClass)
				.takeWhile(Objects::nonNull);
	}

	/**
	 * Returns a {@link Stream} of the test method (if applicable) and all classes
	 * enclosing the test class of the {@link TestContext}, innermost first.
	 *
	 * @param context the {@link TestContext} to search in
	 * @return a stream containing all {@link AnnotatedElement}s in order, or an
	 *         empty stream if no test method/class is available
	 * @author Christian Femers
	 */
	public static Stream<AnnotatedElement> getAnnotatedElementsInnermostFirst(TestContext context) {
		return Stream.concat(context.testMethod().stream(), getClassNestingInnermostFirst(context));
	}
}
