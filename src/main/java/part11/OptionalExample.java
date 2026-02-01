package part11;

import java.util.Optional;

class Insurance {
    private final String name;
    public Insurance(String name) { this.name = name; }
    public String getName() { return name; }
}

class Car {
    private final Optional<Insurance> insurance;
    public Car(Optional<Insurance> insurance) {
        this.insurance = insurance;
    }
    public Optional<Insurance> getInsurance() {
        return insurance;
    }
}

class Person {
    private final Optional<Car> car;
    public Person(Optional<Car> car) {
        this.car = car;
    }
    public Optional<Car> getCar() {
        return car;
    }
}

public class OptionalExample {
    public static void main(String[] args) {
        Optional<Insurance> optInsurance = Optional.of(new Insurance("Allianz"));
        Optional<String> name = optInsurance.map(Insurance::getName);

        Optional<Car> car = Optional.of(
                new Car(Optional.of(new Insurance("AXA")))
        );

        Optional<Optional<Insurance>> result = car.map(Car::getInsurance);
        Optional<Insurance> insurance = car.flatMap(Car::getInsurance);
    }
}