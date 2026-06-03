package mainclub.network.core.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Folder {
    public void delete(String filePath) {
        Path path = Paths.get(filePath);
        if(!path.toFile().exists()) return;

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                //delete folders
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if(!dir.toString().contains("playerdata")) {
                        return FileVisitResult.CONTINUE;
                    }

                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                // delete files
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if(file.toString().contains("latest")) {
                        return FileVisitResult.CONTINUE;
                    }
                    if(filePath.equals("world") && !file.toString().contains("playerdata")) {
                        return FileVisitResult.CONTINUE;
                    }

                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(final String fileName) {
        new File(fileName).delete();
    }
}
