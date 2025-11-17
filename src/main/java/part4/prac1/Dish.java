package part4.prac1;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class Dish {
    @Getter
    private final String name;
    @Getter
    private final int calories;
    @Getter
    private final Type type;

    private final boolean vegetarian;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public enum Type {MEAT, FISH, OTHER}

    @Override
    public String toString() {
        return name;
    }
}

