package part5.prac8;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.*;

public class Example2 {
    public static final String file_path = "/Users/inchoi/modernjava/src/main/resources/data.txt";

    public static void main(String[] args) {
        long uniqueword = 0;
        try(Stream<String> lines = Files.lines(Paths.get(file_path), Charset.defaultCharset())) {
            uniqueword = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct().count();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(uniqueword);
    }
}
