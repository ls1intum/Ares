package de.tum.in.test.api.ast.tool;


import de.tum.in.test.api.structural.testutils.ClassNameScanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Helps to create the correct path for UnwantedNodesAssert by querying the source directory of the assignment (assignmentSrcDir) and the package definition (packageDef) from the Path.properties file.
 * The Path.properties file needs to look like this:
 * <pre>
 * {@code
 *      packageDef=#packageDef#
 *      assignmentSrcDir=#assignmentSrcDir#
 * }
 * </pre>
 * The Path.properties file needs to be stored in ./
 */
@SuppressWarnings("JavadocLinkAsPlainText")
public class PathTool {

    /**
     * Adds #packageDef#/#assignmentSrcDir# to a relative path in order to reach a file
     * @param pathInsideThePackage Relative string-path beginning from #packageDef#/#assignmentSrcDir# (working directory)
     * @return Full path
     */
    public static Path getPath(String pathInsideThePackage) {
        try (InputStream fis = Files.newInputStream(Path.of(".", "Path.properties"))) {
            Properties props = new Properties();
            props.load(fis);
            return Path.of(
                    (props.get("assignmentSrcDir") != null ? (String) props.get("assignmentSrcDir") : "assignments/src"),
                    (props.get("packageDef") != null ? (String) props.get("packageDef") : "assignments.src")
                            .replace(".", FileSystems.getDefault().getSeparator()), FileSystems.getDefault().getSeparator(),
                    pathInsideThePackage
            );
        } catch (IOException e) {
            throw new UncheckedIOException("IOException in PathTool.getPath(" + pathInsideThePackage + ").", e);
        }
    }
}
