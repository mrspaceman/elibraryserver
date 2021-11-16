package uk.co.droidinactu.elibserver;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

public class ELibrary {
  public void rescan(String rootPath) throws IOException {
    Set<Path> ebooksList = new HashSet<>();
    Files.walkFileTree(
        Paths.get(rootPath),
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (!Files.isDirectory(file)
                && (file.toString().toLowerCase().endsWith(".epub")
                    || file.toString().toLowerCase().endsWith(".pdf")
                    || file.toString().toLowerCase().endsWith(".mobi"))) {
              processBook(file);
            }
            return FileVisitResult.CONTINUE;
          }
        });
  }

  private void processBook(Path file) {}
}
