package part4.prac2;

import lombok.Getter;

public class Product {
    @Getter
    private String name;

    @Getter
    private String category;

    @Getter
    private int price;

    @Getter
    private double rating;

    @Getter
    private int sales;

    public Product(String name, String category, int price, double rating, int sales) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
    }
}
