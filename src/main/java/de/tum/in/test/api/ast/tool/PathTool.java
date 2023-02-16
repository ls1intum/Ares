package de.tum.in.test.api.ast.tool;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.github.javaparser.utils.Pair;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Helps to create the correct path for UnwantedNodesAssert by querying the
 * source directory of the assignment (assignmentSrcDir) and the package
 * definition (packageDef) from the Path.properties file. The Path.properties
 * file needs to look like this:
 *
 * <pre>
 * {@code
 *      packageDef=#packageDef#
 *      assignmentSrcDir=#assignmentSrcDir#
 * }
 * </pre>
 * <p>
 * The Path.properties file needs to be stored in ./
 */
@API(status = Status.MAINTAINED)
public class PathTool {

    /**
     * Adds #packageDef#/#assignmentSrcDir# to a relative path in order to reach a
     * file
     *
     * @param pathInsideThePackage Relative string-path beginning from
     *                             #packageDef#/#assignmentSrcDir# (working
     *                             directory)
     * @return Full path
     */
    public static Path getPath(String pathInsideThePackage) {
        try (InputStream fis = Files.newInputStream(Path.of(".", "Path.properties"))) {
            Properties props = new Properties();
            props.load(fis);
            return Path.of(
                    (props.get("assignmentSrcDir") != null ? (String) props.get("assignmentSrcDir")
                            : "assignments/src"),
                    (props.get("packageDef") != null ? (String) props.get("packageDef") : "assignments.src")
                            .replace(".", FileSystems.getDefault().getSeparator()),
                    FileSystems.getDefault().getSeparator(), pathInsideThePackage);
        } catch (IOException e) {
            throw new UncheckedIOException("Exception on path: " + pathInsideThePackage, e);
        }
    }

    public static Path extractPath(Path pathInsideThePackage, String bias) throws FileNotFoundException {
        try (Stream<Path> paths = Files.walk(bias != null ? Path.of(bias) : Path.of("./"))) {
            Optional<Pair<String, String>> buildPath = paths
                    .filter(path -> path.endsWith("pom.xml") || path.endsWith("build.gradle"))
                    .min(Comparator.comparing(path -> path.toString().length()).thenComparing(Object::toString))
                    .map(PathTool::extractBuildPathFromBuildFile);
            if(buildPath.isPresent()) {
                if(buildPath.get().a != null) {
                    return Path.of(buildPath.get().a);
                } else {
                    throw new RuntimeException(buildPath.get().b);
                }
            } else {
                throw new RuntimeException("No pom.xml or build.gradle file was found.");
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Exception on path: " + pathInsideThePackage, e);
        }
    }

    private static Pair<String, String> extractBuildPathFromBuildFile(Path buildFile) {
        String pattern;
        if (buildFile.endsWith("pom.xml")) {
            pattern = "<sourceDirectory>(?:\\$\\{.*\\})?(?<dir>.*)<\\/sourceDirectory>";
        } else {
            pattern = "def\\s+assignmentSrcDir\\s*=\\s*\"(?<dir>.+)\"";
        }
        try {
            var matcher = Pattern.compile(pattern).matcher(Files.readString(buildFile));
            if (matcher.find()) {
                return new Pair<>(matcher.group("dir"), null);
            } else {
                return new Pair<>(
                        null,
                        buildFile.endsWith("pom.xml")
                                ? "'<sourceDirectory></sourceDirectory>' is missing in pom.xml"
                                : "'def assignmentSrcDir =' is missing in build.gradle"
                );
            }
        } catch (IOException e) {
            return new Pair<>(null, e.getMessage());
        }
    }
}
