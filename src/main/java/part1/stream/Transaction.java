package part1.stream;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Transaction {
    private int amount;
    private String currency;
}