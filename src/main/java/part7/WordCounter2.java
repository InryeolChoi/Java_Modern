package part7;

import lombok.*;

import java.util.stream.Stream;

@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class WordCounter2 {
    @Getter
    private final int counter;
    private final boolean lastSpace;

    // 단어 수를 세는 메소드
    // reduce : 1st param을 가지고 2nd param의 연산을 한 뒤, 그 결과로 3rd param의 연산 수행
    public int countWords(Stream<Character> s) {
        WordCounter2 wordCounter = s.reduce(
                new WordCounter2(0, true),
                WordCounter2::accumulate,
                WordCounter2::combine
        );
        return wordCounter.getCounter();
    }

    // 문자열을 하나씩 탐색하다 공백을 만나면 단어수를 증가
    public WordCounter2 accumulate(Character c) {
        if (Character.isWhitespace(c)) {
            return lastSpace ? this : new WordCounter2(counter, true);
        } else {
            return lastSpace ? new WordCounter2(counter + 1, false) : this;
        }
    }

    // 두 WordCounter의 counter 값을 더함.
    public WordCounter2 combine(WordCounter2 wordCounter) {
        return new WordCounter2(counter + wordCounter.counter, wordCounter.lastSpace);
    }

}