/*
 * Copyright 2019 (C) Andy Aspell-Clark
 *
 * Created on : 10/05/2019 10:55
 * Author     : aaspellc
 *
 */

package uk.co.droidinactu.elibraryserver.scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class FileUtils {

    public static List<String> getFileList(String rootdir) {
        try (Stream<Path> walk = Files.walk(Paths.get(rootdir))) {

            List<String> result = walk
                    .filter(f -> Files.isRegularFile((f)) && (f.toString().endsWith("pdf") || f.toString().endsWith("epub")))
                    .map(x -> x.toString()).collect(Collectors.toList());


//            List<String> result = walk.filter(Files::isRegularFile)
//                    .map(x -> x.toString()).collect(Collectors.toList());

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
