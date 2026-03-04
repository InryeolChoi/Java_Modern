package part19.example1;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TrainJourney {
    public int price;
    public TrainJourney onward;

    @Override
    public String toString() {
        return String.format("TrainJourney[%d] -> %s", price, onward);
    }
}
