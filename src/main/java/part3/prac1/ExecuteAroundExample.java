package part3.prac1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ExecuteAroundExample {

    public static String processFile() throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(ExecuteAroundExample.class.getResourceAsStream("/data.txt")))) {
            return br.readLine();}
    }

    public static void main(String[] args) {
        try {
            String str = processFile();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
