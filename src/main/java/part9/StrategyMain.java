package part9;

// 인터페이스
interface ValidationStrategy {
    boolean execute(String s);
}

// 전략1
class IsAllLowerCase implements ValidationStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("[a-z]+");
    }

}

// 전략2
class IsNumeric implements ValidationStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("\\d+");
    }
}

// Validator 클래스
class Validator {
    private final ValidationStrategy strategy;

    // 생성자
    public Validator(ValidationStrategy v) {
        strategy = v;
    }

    // 메소드
    public boolean validate(String s) {
        return strategy.execute(s);
    }
}

// main()
public class StrategyMain {
    public static void main(String[] args) {
        // 전통적인 방법
        Validator v1 = new Validator(new IsNumeric());
        System.out.println(v1.validate("aaaa"));
        Validator v2 = new Validator(new IsAllLowerCase());
        System.out.println(v2.validate("bbbb"));

        // 람다식 : 람다식을 사용하면 굳이 IsNumeric, IsAllLowerCase 등을 만들 필요가 없어짐.
        Validator v3 = new Validator((String s) -> s.matches("\\d+"));
        System.out.println(v3.validate("aaaa"));
        Validator v4 = new Validator((String s) -> s.matches("[a-z]+"));
        System.out.println(v4.validate("bbbb"));

    }
}
