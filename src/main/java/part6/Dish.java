package part6;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    enum Type { MEAT, FISH, OTHER }

    @Override
    public String toString() {
        return name;
    }
}
