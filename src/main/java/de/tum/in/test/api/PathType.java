package de.tum.in.test.api;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.tum.in.test.api.util.LruCache;

public enum PathType {
	/**
	 * Absolute path must match exactly; given path may be specified relative
	 */
	EXACT() {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			return Path.of(s).toAbsolutePath().normalize()::equals;
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
	},
	/**
	 * applies relative GLOB for a relativized path
	 *
	 * @see FileSystem#getPathMatcher(String)
	 */
	GLOB {
		@Override
		public PathMatcher convertToPathMatcher(String s) {
			GlobNormalizationResult normalizedGlob = GlobNormalizationResult.normalizeGlobPattern(s);
			PathMatcher pm = DEFAULT_FS.getPathMatcher("glob:" + normalizedGlob.getNormalizedGlobPattern());
			return p -> pm.matches(relativizeSafe(p, normalizedGlob.getRelativeOffset()));
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
			PathMatcher pm = DEFAULT_FS.getPathMatcher("regex:" + s);
			return p -> pm.matches(relativizeSafe(p, NO_OFFSET));
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
			PathMatcher pm = DEFAULT_FS.getPathMatcher("glob:" + s);
			return p -> pm.matches(p.normalize());
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
			PathMatcher pm = DEFAULT_FS.getPathMatcher("regex:" + s);
			return p -> pm.matches(p.normalize());
		}
	};

	/**
	 * @param s the format/pattern
	 * @return a {@link PathMatcher} that is configured to match absolute paths.
	 * @author Christian Femers
	 */
	public abstract PathMatcher convertToPathMatcher(String s);

	static final FileSystem DEFAULT_FS = FileSystems.getDefault();
	static final Path CURRENT_PATH = Path.of("").toAbsolutePath();
	static final List<Path> CURRENT_PATH_HIERARCHY = Stream.iterate(CURRENT_PATH, Path::getParent)
			.takeWhile(Objects::nonNull).collect(Collectors.toUnmodifiableList());

	private static final int NO_OFFSET = 0;
	private static final Pattern GLOB_SINGLE_DOT_ELIMINATION;
	private static final Pattern GLOB_DOUBLE_DOT_ELIMINATION;

	static {
		String sepPat = "/".equals(File.separator) ? "/" : "[/\\\\]";
		String nonSepPat = "/".equals(File.separator) ? "(?!\\.\\./)[^/]+" : "(?!\\.\\.[/\\\\])(?:[^/\\\\])+";
		GLOB_SINGLE_DOT_ELIMINATION = Pattern
				.compile(String.format("^(?:\\.(?:%1$s|$))+|(?:%1$s\\.)(?=%1$s|$)|%1$s$", sepPat));
		GLOB_DOUBLE_DOT_ELIMINATION = Pattern
				.compile(String.format("^%2$s%1$s\\.\\.(?:%1$s|$)|%1$s%2$s%1$s\\.\\.(?=%1$s|$)", sepPat, nonSepPat));
	}

	private static Path relativizeSafe(Path any, int offset) {
		Path p = any.normalize().toAbsolutePath();
		if (!Objects.equals(p.getRoot(), CURRENT_PATH.getRoot()))
			return p;
		return CURRENT_PATH_HIERARCHY.get(offset).relativize(p).normalize();
	}

	static final class GlobNormalizationResult {

		private static final int CACHE_SIZE = 256;
		private static final Map<String, GlobNormalizationResult> CACHE = new LruCache<>(CACHE_SIZE - 1);

		private final int relativeOffset;
		private final String normalizedGlobPattern;

		private GlobNormalizationResult(String globPattern) {
			String cleaned;
			cleaned = GLOB_SINGLE_DOT_ELIMINATION.matcher(globPattern).replaceAll("");
			Matcher m;
			while ((m = GLOB_DOUBLE_DOT_ELIMINATION.matcher(cleaned)).find()) {
				cleaned = m.replaceAll("");
			}
			int offset = 0;
			while (cleaned.startsWith("..")) {
				if (cleaned.length() == 2)
					cleaned = "";
				else
					cleaned = cleaned.substring(3);
				offset++;
			}
			if (offset >= CURRENT_PATH_HIERARCHY.size())
				throw new IllegalArgumentException("relative glob pattern for current path requires offset " + offset);
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
