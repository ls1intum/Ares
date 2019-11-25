package de.tum.in.test.api.util;

import java.lang.annotation.Annotation;

import de.tum.in.test.api.TestType;

public interface AnnotationProcessingAdapter<A extends Annotation> {
	Class<A> getTestTypeAnnotation();

	TestType getTestTypeOf(A annoation);
}
