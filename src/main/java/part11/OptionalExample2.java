package part11;

import part11.OptionalExample.*;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class OptionalExample2 {
    public Set<String> getCarInsuranceNames(List<Person> persons) {
        return persons.stream()
                .map(Person::getCar)
                .map(optCar -> optCar.flatMap(Car::getInsurance))
                .map(optIns -> optIns.map(Insurance::getName))
                .flatMap(Optional::stream)
                .collect(toSet());
    }
}
