package part17.Example2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PriceEvent {
    private final double price;
    private final double changePct;
}