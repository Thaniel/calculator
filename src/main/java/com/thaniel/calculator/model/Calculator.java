package com.thaniel.calculator.model;


import java.util.ArrayList;
import java.util.List;

public class Calculator {

    private static Calculator instance;

    private List<String> historical = new ArrayList<>();
    /*private List<String> historical = new ArrayList<>(Arrays.asList(
            "10 + 5 = 15",
            "20 - 3 = 17",
            "8 * 2 = 16",
            "100 / 4 = 25",
            "7 + 3 = 10",
            "50 - 20 = 30",
            "9 * 9 = 81",
            "40 / 5 = 8",
            "12 + 4 = 16",
            "50 - 20 = 30",
            "9 * 9 = 81",
            "40 / 5 = 8",
            "12 + 4 = 16",
            "30 - 10 = 20"
    ));*/

    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }

        return instance;
    }

    public List<String> getHistorical() {
        return historical;
    }

    public void setHistorical(List<String> historical) {
        this.historical = historical;
    }
}
