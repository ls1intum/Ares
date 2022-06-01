package de.tum.in.test.api.internal;

import static java.lang.invoke.MethodType.methodType;

import java.lang.annotation.AnnotationFormatError;
import java.lang.invoke.*;
import java.util.*;
import java.util.function.Supplier;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.WithIOManager;
import de.tum.in.test.api.context.*;
import de.tum.in.test.api.io.*;
import de.tum.in.test.api.security.ArtemisSecurityManager;

@API(status = Status.INTERNAL)
public final class IOExtensionUtils {

	private static final Class<IOTesterManager> DEFAULT_IO_MANAGER = IOTesterManager.class;

	static {
		/*
		 * Initialize SecurityManager when we are still in the main thread
		 */
		ArtemisSecurityManager.isInstalled();
	}

	private static final HashMap<Class<? extends IOManager<?>>, Supplier<? extends IOManager<?>>> ioManagerCache = new HashMap<>();

	private final AresIOContext context;
	private final IOManager<?> ioManager;
	private final Class<?> controllerClass;

	public IOExtensionUtils(TestContext testContext) {
		context = AresIOContext.from(testContext);
		ioManager = createIOManagerFor(testContext);
		controllerClass = ioManager.getControllerClass();
	}

	public void beforeTestExecution() {
		ioManager.beforeTestExecution(context);
	}

	public void afterTestExecution() {
		ioManager.afterTestExecution(context);
	}

	public boolean providesController() {
		return controllerClass != null;
	}

	public Object getControllerInstance() {
		return providesController() ? ioManager.getControllerInstance(context) : null;
	}

	public boolean canProvideControllerFor(Class<?> targetType) {
		return providesController() && Objects.class != targetType && targetType.isAssignableFrom(controllerClass);
	}

	private static IOManager<?> createIOManagerFor(TestContext testContext) {
		var ioManagerClass = TestContextUtils.findAnnotationIn(testContext, WithIOManager.class)
				.<Class<? extends IOManager<?>>>map(WithIOManager::value).orElse(DEFAULT_IO_MANAGER);
		return ioManagerCache.computeIfAbsent(ioManagerClass, IOExtensionUtils::generateIOManagerSupplier).get();
	}

	private static Supplier<IOManager<?>> generateIOManagerSupplier(Class<? extends IOManager<?>> ioManagerClass) {
		try {
			var lookup = MethodHandles.lookup();
			var contructor = lookup.findConstructor(ioManagerClass, methodType(void.class));
			var factory = LambdaMetafactory
					.metafactory(lookup, "get", methodType(Supplier.class), contructor.type().generic(), contructor, //$NON-NLS-1$
							contructor.type())
					.getTarget();
			return (Supplier<IOManager<?>>) factory.invokeExact();
		} catch (Throwable e) {
			throw new AnnotationFormatError("Could not create IOManager Supplier for type " //$NON-NLS-1$
					+ ioManagerClass.getCanonicalName() + ". Make sure a public no-args constructor is available.", e); //$NON-NLS-1$
		}
	}
}
