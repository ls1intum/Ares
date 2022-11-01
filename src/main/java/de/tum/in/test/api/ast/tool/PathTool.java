package de.tum.in.test.api.ast.tool;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Helps creating the correct path for UnwantedNodesAssert by querying the source directory of the assignment (assignmentSrcDir) and the package definition (packageDef) from the Path.xml file.
 * The Path.xml file needs to look like this:
 *      <?xml version="1.0" encoding="UTF-8"?>
 *      <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
 *      <properties>
 *      <comment>Path-Data</comment>
 *      <entry key="packageDef">#packageDef#</entry>
 *      <entry key="assignmentSrcDir">#assignmentSrcDir#</entry>
 *      </properties>
 * The Path.xml file needs to be stored in ./
 */
@SuppressWarnings("JavadocLinkAsPlainText")
public class PathTool {
    /**
     * Adds #packageDef#/#assignmentSrcDir# to a relative path in order to reach a file
     * @param pathInsideThePackage Relative string-path beginning from #packageDef#/#assignmentSrcDir# (working directory)
     * @return Full path
     */
    public static Path getPath(String pathInsideThePackage) {
        try (InputStream fis = Files.newInputStream(Path.of(".",FileSystems.getDefault().getSeparator(), "Path.xml"))) {
            Properties props = new Properties();
            props.loadFromXML(fis);
            return Path.of(
                    (props.get("assignmentSrcDir") != null ? (String) props.get("assignmentSrcDir") : "assignments/src"),
                    (props.get("packageDef") != null ? (String) props.get("packageDef") : "assignments.src")
                            .replace(".", FileSystems.getDefault().getSeparator()), FileSystems.getDefault().getSeparator(),
                    pathInsideThePackage
            );
        } catch (IOException e) {
            throw new RuntimeException("IOException in PathTool.getPath(" + pathInsideThePackage + "): " + e.getMessage());
        }
    }
}