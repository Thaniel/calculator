package com.thaniel.calculator.controllers;

import com.thaniel.calculator.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.special.Gamma;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


public class AdvancedCalculatorController extends CalculatorController {
    @FXML
    @Override
    public void initialize() {
        if (expressionLabel == null || resultLabel == null) {
            System.err.println("Error: FXML in AdvancedCalculatorController was not initialized correctly.");
        } else {
            System.out.println("Info: FXML in AdvancedCalculatorController was initialized correctly.");
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
                num2 = new BigDecimal(resultLabel.getText().replace(",", "."));

                switch (operation) {
                    case "+" -> result = num1.add(num2);
                    case "-" -> result = num1.subtract(num2);
                    case "×" -> result = num1.multiply(num2);
                    case "÷" -> result = num2.compareTo(BigDecimal.ZERO) != 0 ? num1.divide(num2, 10, RoundingMode.HALF_UP) : null;
                    case "%" -> result = num1.multiply(num2).divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
                    case "mod" -> result = num1.remainder(num2);
                    case "exp" -> result = num1.multiply(BigDecimal.TEN.pow(num2.intValue())); //TODO Round num2
                    case "xʸ" -> result = BigDecimal.valueOf(Math.pow(num1.doubleValue(), num2.doubleValue()));
                }
            } else {
                switch (operation) {
                    case "n!" -> result = BigDecimal.valueOf(factorial(num1.doubleValue()));
                    case "sin" -> result = BigDecimal.valueOf(Math.sin(Math.toRadians(num1.doubleValue())));
                    case "cos" -> result = BigDecimal.valueOf(Math.cos(Math.toRadians(num1.doubleValue())));
                    case "tan" -> result = BigDecimal.valueOf(Math.tan(Math.toRadians(num1.doubleValue())));
                    case "x²" -> result = num1.pow(2);
                    case "√" -> result = BigDecimal.valueOf(Math.sqrt(num1.doubleValue()));
                    case "log" -> result = BigDecimal.valueOf(Math.log10(num1.doubleValue()));
                    case "ln" -> result = BigDecimal.valueOf(Math.log(num1.doubleValue()));
                    case "|x|" -> result = BigDecimal.valueOf(Math.abs(num1.doubleValue()));
                    case "eˣ" -> result = BigDecimal.valueOf(Math.exp(num1.doubleValue()));
                    case "10ˣ" -> result = BigDecimal.valueOf(Math.pow(10, num1.doubleValue()));
                }
            }

            if (result != null) {
                //result = Utils.getInstance().round(result, 10);
                displayFormattedOperation();
            } else {
                manageErrorDividingBy0();
            }

            restartValues();
        }
    }

    @Override
    protected void displayFormattedOperation() {
        String resultFormatted = UTILS.formatNumber(result);
        resultLabel.setText(resultFormatted);

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
        if ((operation == null) && (expressionLabel.getText().isEmpty() || equalsButtonClicked) && !errorDividingBy0) {
            operation = operator;

            String result = resultLabel.getText().replace(".", "");
            num1 = new BigDecimal(result.replace(",", "."));

            String num1Formatted = num1.toString();
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
}