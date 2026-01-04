package part7;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCounterSpliterator implements Spliterator<Character> {
    private final String string;
    private int currentChar = 0;
    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        return currentChar < string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;
        if (currentSize < 10) {
            return null;
        }
        for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
            if (Character.isWhitespace(string.charAt(splitPos))) {
                Spliterator<Character> spliterator =
                        new WordCounterSpliterator(string.substring(currentChar, splitPos));
                currentChar = splitPos;
                return spliterator;
            }
        }
        return null;
    }

    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + IMMUTABLE;
    }


    public static void main(String[] args) {
        final String SENTENCE = "자바(Java)는 썬 마이크로시스템즈에서 프로그래머 제임스 고슬링이 1995년에 개발한 객체 지향 프로그래밍 언어이다.";

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> s = StreamSupport.stream(spliterator, true);

        System.out.println("총 단어 수 : " + WordCounter.countWords(s));
    }
}
