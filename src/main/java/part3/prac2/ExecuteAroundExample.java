package part3.prac2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecuteAroundExample {
    public static void main(String[] args) {
        try {
            String oneLine = processFile((BufferedReader br) -> br.readLine());
            System.out.println("oneLine의 결과 : " + oneLine);

            String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
            System.out.println("twoLine의 결과 : " + twoLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(part3.prac1.ExecuteAroundExample.class.getResourceAsStream("/data.txt"))
        )) {return p.process(br);}
    }
}
