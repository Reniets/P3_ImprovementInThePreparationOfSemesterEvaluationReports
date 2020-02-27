package LinearAlgebra.Types.Vectors;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VectorImportExport {


    @Test
    void testExportAndImportOfMatrix() throws IOException {

        Vector testVector000 = VectorBuilder.qBuild(1,2,3,4,5);

        Path path = Paths.get("tests/LinearAlgebra/Types/Vector/data/testPath.txt");

        Files.createDirectories(path.getParent());

        // Export the matrix
        testVector000.export(path);

        //Import it
        Vector testVector001 = VectorBuilder.importVector(path);

        assertEquals(testVector000, testVector001);

        Files.delete(path);

    }

}
