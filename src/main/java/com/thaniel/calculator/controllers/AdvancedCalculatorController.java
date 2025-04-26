package com.thaniel.calculator.controllers;

import com.thaniel.calculator.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.commons.math3.special.Gamma;

import java.util.Objects;


public class AdvancedCalculatorController extends CalculatorController {
    @FXML
    private BorderPane mainPane;

    @FXML
    @Override
    public void initialize() {
        if (expressionLabel == null || resultLabel == null) {
            System.err.println("Error: FXML in AdvancedCalculatorController was not initialized correctly.");
        } else {
            System.out.println("Info: FXML in AdvancedCalculatorController was initialized correctly.");

            //setupListeners();
        }
    }

    /*
     * Number e button --> "e"
     */
    public void onClickEuler(MouseEvent mouseEvent) {
        resultLabel.setText(String.valueOf(Math.E));
    }

    /*
     * Pi button --> "π"
     */
    public void onClickPi(MouseEvent mouseEvent) {
        resultLabel.setText(String.valueOf(Math.PI));
    }

    /*
     * Negative/Positive number button --> "+/-"
     */
    public void onClickNegativePositive(MouseEvent mouseEvent) {
        String resultText = resultLabel.getText();

        if (!Objects.equals(resultText, "0")) {
            if (resultText.contains("-")) {
                resultLabel.setText(resultText.replace("-", ""));
            } else {
                resultLabel.setText("-".concat(resultText));
            }
        }
    }

    @Override
    protected void handleCalculate() {
        if (operation != null && num1 != null) {
            equalsButtonClicked = true;

            if (!resultLabel.getText().isEmpty()) {
                num2 = Double.parseDouble(resultLabel.getText().replace(",", "."));

                switch (operation) {
                    case "+" -> result = num1 + num2;
                    case "-" -> result = num1 - num2;
                    case "×" -> result = num1 * num2;
                    case "÷" -> result = (num2 != 0) ? num1 / num2 : null;
                    case "%" -> result = num2 * num1 / 100;
                    case "mod" -> result = num1 % num2;
                    case "exp" -> result = (num1 * Math.pow(10, num2));
                    case "xʸ" -> result = Math.pow(num1, num2);
                }
            } else {
                switch (operation) {
                    case "n!" -> result = factorial(num1);
                    case "sin" -> result = Math.sin(Math.toRadians(num1));
                    case "cos" -> result = Math.cos(Math.toRadians(num1));
                    case "tan" -> result = Math.tan(Math.toRadians(num1));
                    case "x²" -> result = Math.pow(num1, num2 = 2.0);
                    case "√" -> result = Math.sqrt(num1);
                    case "log" -> result = Math.log10(num1);
                    case "ln" -> result = Math.log(num1);
                    case "|x|" -> result = Math.abs(num1);
                    case "eˣ" -> result = Math.exp(num1);
                    case "10ˣ" -> result = Math.pow(10, num1);
                }
            }

            displayFormattedOperation();
            restartValues();
        }
    }

    @Override
    protected void displayFormattedOperation() {
        String resultFormatted = UTILS.formatNumber(result);
        resultLabel.setText(String.valueOf(resultFormatted).replace(".", ","));

        operation = UTILS.formatOperation(operation);
        String num1Formatted = UTILS.formatNumber(num1);

        if (num2 != null) {
            String num2Formatted = UTILS.formatNumber(num2);
            expressionLabel.setText(num1Formatted + " " + operation + " " + num2Formatted + " = ");
        }

        String historyEntry = expressionLabel.getText() + resultLabel.getText();

        if (mainViewController != null) {
            mainViewController.addHistoryEntry(historyEntry);
        }
    }

    @Override
    protected void handleOperation(String operator) {
        if ((operation == null) && (expressionLabel.getText().isEmpty() || equalsButtonClicked)) {
            operation = operator;

            num1 = Double.parseDouble(resultLabel.getText().replace(",", "."));

            String num1Formatted = UTILS.formatNumber(num1);
            String formattedOperator = UTILS.formatOperation(operator);

            resultLabel.setText("");
            equalsButtonClicked = false;

            if (Utils.BINARY_OPERATIONS.contains(operator)) {
                expressionLabel.setText(num1Formatted + " " + formattedOperator);
            } else {
                switch (operator) {
                    case "x²" -> expressionLabel.setText(num1Formatted + " ^ 2 = ");
                    case "eˣ" -> expressionLabel.setText("e ^ " + num1Formatted + " = ");
                    case "10ˣ" -> expressionLabel.setText("10 ^ " + num1Formatted + " = ");
                    default -> expressionLabel.setText(formattedOperator + " (" + num1Formatted + ") = ");
                }
                equalsButtonClicked = true;
                handleCalculate();
            }
        }
    }

    private double factorial(double x) {
        double result = 0;

        if (x >= 0) {
            if (x == Math.floor(x)) {
                result = factorialInt((int) x);
            } else {
                result = Gamma.gamma(x + 1);
            }
        }

        return result;
    }

    private double factorialInt(int n) {
        if (n == 0 || n == 1) {
            return 1;
        } else {
            double fact = 1;
            for (int i = 2; i <= n; i++) {
                fact *= i;
            }
            return fact;
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