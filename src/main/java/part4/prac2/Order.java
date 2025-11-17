package part4.prac2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class Order {
    int id;
    String user;
    LocalDate orderDate;   // 주문일
    int deliveryDays;      // 배송까지 걸리는 일수
};
