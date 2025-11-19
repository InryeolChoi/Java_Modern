package part5.prac1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Dish {
    private String name;
    private boolean vegetarian;
    private int calories;

    public boolean isVegetarian() {
        return vegetarian;
    }

    @Override
    public String toString() {
        return name + "(" + calories + ")";
    }
}
