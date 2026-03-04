package part19.example2;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Tree {
    private String key;
    private int val;
    private Tree left, right;
}