package part11;

import java.util.Optional;

public class OptionalExample3 {
    public static void main(String[] args) {
        Person person = new Person(Optional.of(new Car(Optional.empty())));

        // 1️⃣ 디폴트 값 사용 (orElseGet)
        String nameWithDefault =
                person.getCar()
                        .flatMap(Car::getInsurance)
                        .map(Insurance::getName)
                        .orElseGet(() -> "Unknown");

        System.out.println("보험 이름 (기본값): " + nameWithDefault);

        // 2️⃣ 없으면 실패 (orElseThrow)
        try {
            String requiredName =
                    person.getCar()
                            .flatMap(Car::getInsurance)
                            .map(Insurance::getName)
                            .orElseThrow(() ->
                                    new IllegalStateException("보험이 반드시 있어야 합니다")
                            );

            System.out.println(requiredName);
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        // 3️⃣ 값이 있을 때만 행동 (ifPresent)
        person.getCar()
                .flatMap(Car::getInsurance)
                .map(Insurance::getName)
                .ifPresent(name ->
                        System.out.println("보험 로그 출력: " + name)
                );
    }
}
