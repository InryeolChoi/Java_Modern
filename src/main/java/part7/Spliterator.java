package part7;

import lombok.*;

@NoArgsConstructor
public class Spliterator {
    public int countWordsIteratively(String s) {
        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }

    public static void main(String[] args) {
        final String SENTENCE = "자바(Java)는 썬 마이크로시스템즈에서 프로그래머 제임스 고슬링이 1995년에 개발한 객체 지향 프로그래밍 언어이다.";
        Spliterator s = new Spliterator();
        int result = s.countWordsIteratively(SENTENCE);
        System.out.println("단어 수 : " + result);
    }
}
