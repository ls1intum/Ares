package de.tum.in.test.api;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.tum.in.test.api.util.LruCache;

/**
 * Defines different path pattern matching notations which can be converted into
 * {@link PathMatcher}s.
 *
 * @author Christian Femers
 */
@API(status = Status.STABLE)
public enum PathType {
	/**
	 * Absolute path must match exactly; given path may be specified relative
	 */
	EXACT() {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			return Path.of(s).toAbsolutePath().normalize()::equals;
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// children cannot have an exactly equal path
			return false;
		}
	},
	/**
	 * The path must start with the given path; given path may be specified relative
	 */
	STARTS_WITH {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			Path begin = Path.of(s).toAbsolutePath().normalize();
			return p -> p.normalize().startsWith(begin);
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// children will start with the same path as well
			return true;
		}
	},
	/**
	 * applies relative GLOB for a relativized path
	 *
	 * @see FileSystem#getPathMatcher(String)
	 */
	GLOB {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			var normalizedGlob = GlobNormalizationResult.normalizeGlobPattern(s);
			var pathMatcher = DEFAULT_FS.getPathMatcher(GLOB_PREFIX + normalizedGlob.getNormalizedGlobPattern());
			return p -> pathMatcher.matches(relativizeSafe(p, normalizedGlob.getRelativeOffset()));
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// this is definitely safe
			return pathPattern.endsWith("**") && !pathPattern.endsWith("\\**"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	},
	/**
	 * applies relative RegEx for a relativized path
	 *
	 * @see FileSystem#getPathMatcher(String)
	 */
	REGEX {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			var pathMatcher = DEFAULT_FS.getPathMatcher(REGEX_PREFIX + s);
			return p -> pathMatcher.matches(relativizeSafe(p, NO_OFFSET));
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// no easy conclusions possible, because of lookahead and similar constructs
			return false;
		}
	},
	/**
	 * applies absolute GLOB for an absolute path
	 *
	 * @see FileSystem#getPathMatcher(String)
	 */
	GLOB_ABSOLUTE {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			var pathMatcher = DEFAULT_FS.getPathMatcher(GLOB_PREFIX + s);
			return p -> pathMatcher.matches(p.normalize());
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// this is definitely safe
			return pathPattern.endsWith("**") && !pathPattern.endsWith("\\**"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	},
	/**
	 * applies absolute RegEx for an absolute path
	 *
	 * @see FileSystem#getPathMatcher(String)
	 */
	REGEX_ABSOLUTE {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			var pathMatcher = DEFAULT_FS.getPathMatcher(REGEX_PREFIX + s);
			return p -> pathMatcher.matches(p.normalize());
		}

		@Override
		public boolean isPatternRecursive(String pathPattern) {
			// no easy conclusions possible, because of lookahead and similar constructs
			return false;
		}
	};

	private static final String GLOB_PREFIX = "glob:"; //$NON-NLS-1$
	private static final String REGEX_PREFIX = "regex:"; //$NON-NLS-1$

	/**
	 * @param s the format/pattern
	 * @return a {@link PathMatcher} that is configured to match absolute paths.
	 * @author Christian Femers
	 */
	public abstract PathMatcher convertToPathMatcher(String s);

	/**
	 * Returns <code>true</code> only for patterns that are recursive, meaning that
	 * any paths of children match as well. This method does not represent an "if
	 * and only if" statement, it may return false for some recursive patterns.
	 *
	 * @param pathPattern the path pattern used for matching
	 * @return true if the method can safely determine that the pattern will match
	 *         any children of a given path as well
	 * @author Christian Femers
	 */
	public abstract boolean isPatternRecursive(String pathPattern);

	static final FileSystem DEFAULT_FS = FileSystems.getDefault();
	static final Path CURRENT_PATH = Path.of("").toAbsolutePath(); //$NON-NLS-1$
	static final List<Path> CURRENT_PATH_HIERARCHY = Stream.iterate(CURRENT_PATH, Path::getParent)
			.takeWhile(Objects::nonNull).collect(Collectors.toUnmodifiableList());

	private static final int NO_OFFSET = 0;
	private static final Pattern GLOB_SINGLE_DOT_ELIMINATION;
	private static final Pattern GLOB_DOUBLE_DOT_ELIMINATION;

	static {
		String sepPat = "/".equals(File.separator) ? "/" : "[/\\\\]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String nonSepPat = "/".equals(File.separator) ? "(?!\\.\\./)[^/]+" : "(?!\\.\\.[/\\\\])(?:[^/\\\\])+"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		GLOB_SINGLE_DOT_ELIMINATION = Pattern
				.compile(String.format("^(?:\\.(?:%1$s|$))+|(?:%1$s\\.)(?=%1$s|$)|%1$s$", sepPat)); //$NON-NLS-1$
		GLOB_DOUBLE_DOT_ELIMINATION = Pattern
				.compile(String.format("^%2$s%1$s\\.\\.(?:%1$s|$)|%1$s%2$s%1$s\\.\\.(?=%1$s|$)", sepPat, nonSepPat)); //$NON-NLS-1$
	}

	private static Path relativizeSafe(Path any, int offset) {
		var path = any.normalize().toAbsolutePath();
		if (!Objects.equals(path.getRoot(), CURRENT_PATH.getRoot()))
			return path;
		return CURRENT_PATH_HIERARCHY.get(offset).relativize(path).normalize();
	}

	static final class GlobNormalizationResult {

		private static final int CACHE_SIZE = 256;
		private static final Map<String, GlobNormalizationResult> CACHE = new LruCache<>(CACHE_SIZE - 1);

		private final int relativeOffset;
		private final String normalizedGlobPattern;

		private GlobNormalizationResult(String globPattern) {
			String cleaned;
			cleaned = GLOB_SINGLE_DOT_ELIMINATION.matcher(globPattern).replaceAll(""); //$NON-NLS-1$
			Matcher m;
			while ((m = GLOB_DOUBLE_DOT_ELIMINATION.matcher(cleaned)).find())
				cleaned = m.replaceAll(""); //$NON-NLS-1$
			var offset = 0;
			while (cleaned.startsWith("..")) { //$NON-NLS-1$
				if (cleaned.length() == 2)
					cleaned = ""; //$NON-NLS-1$
				else
					cleaned = cleaned.substring(3);
				offset++;
			}
			if (offset >= CURRENT_PATH_HIERARCHY.size())
				throw new IllegalArgumentException("relative glob pattern for current path requires offset " + offset); //$NON-NLS-1$
			this.relativeOffset = offset;
			this.normalizedGlobPattern = cleaned;
		}

		int getRelativeOffset() {
			return relativeOffset;
		}

		String getNormalizedGlobPattern() {
			return normalizedGlobPattern;
		}

		static GlobNormalizationResult normalizeGlobPattern(String globPattern) {
			GlobNormalizationResult result;
			synchronized (CACHE) {
				result = CACHE.get(globPattern);
			}
			if (result == null) {
				result = new GlobNormalizationResult(globPattern);
				synchronized (CACHE) {
					CACHE.put(globPattern, result);
				}
			}
			return result;
		}
	}
}
