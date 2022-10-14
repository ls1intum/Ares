package de.tum.in.test.integration.testuser;

import de.tum.in.test.api.AddTrustedPackage;
import de.tum.in.test.api.StrictTimeout;
import de.tum.in.test.api.WhitelistPath;
import de.tum.in.test.api.ast.asserting.UnwantedNodesAssert;
import de.tum.in.test.api.ast.data.PathConfiguration;
import de.tum.in.test.api.ast.type.LoopType;
import de.tum.in.test.api.jupiter.Public;
import de.tum.in.test.api.localization.UseLocale;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

@AddTrustedPackage("com.github.javaparser.**")
@Public
@UseLocale("en")
@StrictTimeout(10)
@WhitelistPath("")
public class ASTTestUser {

    @Test
    void testHasBelowNoForLoop_Success() {
        new UnwantedNodesAssert(
                new PathConfiguration(
                        Path.of(
                                "src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithNoKindsOfLoops.java"
                        )
                )
        ).hasBelowNo(LoopType.FOR);

    }

    @Test
    void testHasBelowNoForLoop_Fail() {
        new UnwantedNodesAssert(
                new PathConfiguration(
                        Path.of(
                                "src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithAllKindsOfLoops.java"
                        )
                )
        ).hasBelowNo(LoopType.FOR);
    }

    @Test
    void testHasAtNoForLoop_Success() {
        new UnwantedNodesAssert(
                new PathConfiguration(
                        Path.of(
                                "src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithNoKindsOfLoops.java"
                        )
                )
        ).hasBelowNo(LoopType.FOR);
    }

    @Test
    void testHasAtNoForLoop_Fail() {
        new UnwantedNodesAssert(
                new PathConfiguration(
                        Path.of(
                                "src/test/java/de/tum/in/test/integration/testuser/subject/structural/ClassWithAllKindsOfLoops.java"
                        )
                )
        ).hasBelowNo(LoopType.FOR);
    }
}
