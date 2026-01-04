package part7;

import lombok.*;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
public class WordCounter {
    @Getter
    private final int counter;
    private final boolean lastSpace;

    public WordCounter accumulate(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter(counter, true);
        } else {
            return lastSpace ? new WordCounter(counter + 1, false) : this;
        }
    }

    public WordCounter combine(WordCounter wordCounter) {
        return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
    }

    public static int countWords(Stream<Character> s) {
        WordCounter wordCounter = s.reduce(new WordCounter(0, true),
                WordCounter::accumulate, WordCounter::combine);
        return wordCounter.getCounter();
    }

    public static void main(String[] args) {
        final String SENTENCE = "자바(Java)는 썬 마이크로시스템즈에서 프로그래머 제임스 고슬링이 1995년에 개발한 객체 지향 프로그래밍 언어이다.";

        Stream<Character> s = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
        System.out.println("총 단어 수 : " + countWords(s));
    }

}