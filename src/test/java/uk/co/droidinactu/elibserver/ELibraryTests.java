package uk.co.droidinactu.elibserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ELibraryTests {
    ELibrary elib=new ELibrary();

    @Test
    public void shouldFindNewFilesWhenRescanning() throws IOException {
        elib.rescan("/");
    }
}
