/**
 * Extensions and annotations for using Ares in combination with
 * <a href="https://jqwik.net">jqwik</a>.
 * <p>
 * <b>If jqwik {@link net.jqwik.api.Property Property}s require the test code to
 * be whitelisted due to the actions it performs,
 * {@link de.tum.in.test.api.TrustedThreads TrustedThreads} must be configured
 * to {@link de.tum.in.test.api.TrustedThreads.TrustScope#EXTENDED
 * EXTENDED}.</b> This comes with security implications and only changing the
 * scope is not sufficient. For more details, see
 * {@link de.tum.in.test.api.TrustedThreads TrustedThreads}. The reason here is
 * that jqwik uses the common pool for property execution and therefore, the
 * common pool must be whitelisted. Without the correct thread trust scope,
 * problems may show up in form of exceptions (such as
 * {@link java.util.concurrent.ExecutionException}) being thrown that are not
 * expected.
 * <p>
 * It might also be the case that
 * {@link de.tum.in.test.api.TrustedThreads.TrustScope#ALL_THREADS ALL_THREADS}
 * is needed if the common pool parallelism is only one, because the common pool
 * is not used in such a case. This can also be changed by the command line
 * argument
 * <code>-Djava.util.concurrent.ForkJoinPool.common.parallelism=2</code>, in
 * which case {@link de.tum.in.test.api.TrustedThreads.TrustScope#EXTENDED
 * EXTENDED} is sufficient again.
 *
 * @author Christian Femers
 */
package de.tum.in.test.api.jqwik;
