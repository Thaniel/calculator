package com.thaniel.calculator.controllers;

import javafx.fxml.FXML;

public class BasicCalculatorController extends CalculatorController{

    @FXML
    @Override
    public void initialize() {
        if (expressionLabel == null || resultLabel == null) {
            System.err.println("Error: FXML in BasicCalculatorController was not initialized correctly.");
        }else {
            System.out.println("Info: FXML in BasicCalculatorController was initialized correctly.");
        }
    }
}