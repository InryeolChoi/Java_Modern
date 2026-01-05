package part7;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCounterTest {
    public static void main(String[] args) {
        final String SENTENCE = "자바(Java)는 썬 마이크로시스템즈에서 프로그래머 제임스 고슬링이 1995년에 개발한 객체 지향 프로그래밍 언어이다.";

        // 단순 반복으로 처리한 case
        WordCounter1 s1 = new WordCounter1();
        int result = s1.countWordsIteratively(SENTENCE);
        System.out.println("단어 수 : " + result);

        // 함수형으로 처리한 case
        WordCounter2 s2 = new WordCounter2();
        Stream<Character> stream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
        System.out.println("단어 수 : " + s2.countWords(stream));

        // 좀 더 빠른 처리를 위해 병렬처리를 넣은 case
        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);    // SENTENCE를 어떻게 쪼갤지를 규칙이 담긴 자료구조를 만듬.
        Stream<Character> stream2 = StreamSupport.stream(spliterator, false); // 그걸 가지고 실제 스트림을 만든다.
        System.out.println("단어 수 : " + s2.countWords(stream2.parallel()));          // 병렬로 스트림을 돌린다.
    }
}
