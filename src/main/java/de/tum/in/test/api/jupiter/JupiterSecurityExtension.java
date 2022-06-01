package de.tum.in.test.api.jupiter;

import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.*;

import de.tum.in.test.api.internal.ConfigurationUtils;
import de.tum.in.test.api.security.ArtemisSecurityManager;

@API(status = Status.INTERNAL)
public final class JupiterSecurityExtension implements UnifiedInvocationInterceptor {

	@Override
	public <T> T interceptGenericInvocation(Invocation<T> invocation, ExtensionContext extensionContext,
			Optional<ReflectiveInvocationContext<?>> invocationContext) throws Throwable {
		var testContext = JupiterContext.of(extensionContext);
		var configuration = ConfigurationUtils.generateConfiguration(testContext);
		var accessToken = ArtemisSecurityManager.install(configuration);
		Throwable failure = null;
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			failure = t;
		} finally {
			try {
				ArtemisSecurityManager.uninstall(accessToken);
			} catch (Exception e) {
				if (failure == null)
					failure = e;
				else
					failure.addSuppressed(e);
			}
		}
		throw failure;
	}
}
