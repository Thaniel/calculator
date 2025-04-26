package com.thaniel.calculator.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class BasicCalculatorController extends CalculatorController{
    @FXML
    private BorderPane mainPane;

    @FXML
    @Override
    public void initialize() {
        if (expressionLabel == null || resultLabel == null) {
            System.err.println("Error: FXML in BasicCalculatorController was not initialized correctly.");
        }else {
            System.out.println("Info: FXML in BasicCalculatorController was initialized correctly.");

            setupListeners();
        }
    }

    public void setupListeners(){
        Platform.runLater(() -> {
            Scene scene = mainPane.getScene();
            if (scene != null) {
                scene.setOnKeyPressed(null);
                scene.setOnKeyPressed(this::onKeyPressed);
            }
        });
    }
}