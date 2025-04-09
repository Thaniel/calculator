package com.thaniel.calculator.controllers;


import com.thaniel.calculator.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public abstract class CalculatorController {
    @FXML
    protected Label expressionLabel;
    @FXML
    protected Label resultLabel;

    protected static final Utils UTILS = Utils.getInstance();

    protected String operation;
    protected Double num1 = null;
    protected Double num2 = null;
    protected Double result = null;
    protected boolean equalsButtonClicked;
    protected static MainViewController mainViewController;

    @FXML
    public abstract void initialize();

    public void setMainViewController(MainViewController controller) {
        mainViewController = controller;
    }

    /*
     * Number Button --> "1", "2", "3", "4", "5", "6", "7" , "8", "9" and ","
     */
    public void onClickNumber(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        insertNumberToResult(button.getText());
    }

    /*
     * Operation Button --> "+", "-" , "x" , "/" and "%"
     */
    public void onClickOperation(MouseEvent mouseEvent) {
        String operator = ((Button) mouseEvent.getSource()).getText();
        handleOperation(operator);
    }

    /*
     * Calculate button --> "="
     */
    public void onClickCalculate(MouseEvent mouseEvent) {
        handleCalculate();
    }

    /*
     * Delete button --> "DEL"
     */
    public void onClickDelete(MouseEvent mouseEvent) {
        String text = resultLabel.getText();
        resultLabel.setText(UTILS.removeLastDigit(text));
    }

    /*
     * Clear button --> "AC"
     */
    public void onClickClear(MouseEvent mouseEvent) {
        resultLabel.setText("0");
        expressionLabel.setText("");
        restartValues();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent != null){
            KeyCode keyCode = keyEvent.getCode();

            String text = (keyCode == KeyCode.DECIMAL) ? "," : keyEvent.getText();

            System.out.println(keyCode);

            switch (keyCode) {
                case ADD -> handleOperation("+");
                case SUBTRACT -> handleOperation("-");
                case MULTIPLY -> handleOperation("×");
                case DIVIDE -> handleOperation("÷");
                case ENTER -> handleCalculate();
                case BACK_SPACE -> onClickDelete(null);
                case DELETE -> onClickClear(null);
                default -> insertNumberToResult(text);
            }
        }
    }


    protected void insertNumberToResult(String number) {
        if (number.equals(",")) {
            String text = UTILS.handleDecimals(resultLabel.getText(), number);
            resultLabel.setText(text);
        } else if (!number.isEmpty() && Character.isDigit(number.charAt(0))) {
            if (equalsButtonClicked) {
                expressionLabel.setText("");
                restartValues();
                equalsButtonClicked = false;
                resultLabel.setText(number);
            } else {
                String currentText = resultLabel.getText();
                resultLabel.setText(currentText.equals("0") ? number : currentText + number);
            }
        }
    }

    protected void restartValues() {
        num1 = null;
        num2 = null;
        result = null;
        operation = null;
    }

    protected void handleCalculate() {
        if (operation != null && num1 != null && !resultLabel.getText().isEmpty()) {
            equalsButtonClicked = true;
            num2 = Double.parseDouble(resultLabel.getText().replace(",", "."));

            switch (operation) {
                case "+" -> result = num1 + num2;
                case "-" -> result = num1 - num2;
                case "×" -> result = num1 * num2;
                case "÷" -> result = (num2 != 0) ? num1 / num2 : null;
                case "%" -> result = num2 * num1 / 100;
            }

            displayFormattedOperation();
            restartValues();
        }
    }

    protected void displayFormattedOperation() {
        String resultFormatted = UTILS.formatNumber(result);
        resultLabel.setText(resultFormatted.replace(".", ","));

        String num1Formatted = UTILS.formatNumber(num1);
        String num2Formatted = UTILS.formatNumber(num2);
        expressionLabel.setText(num1Formatted + " " + operation + " " + num2Formatted + " = ");

        String historyEntry = expressionLabel.getText() + resultLabel.getText();

        if (mainViewController != null) {
            mainViewController.addHistoryEntry(historyEntry);
        }
    }

    protected void handleOperation(String operator) {
        if ((operation == null) && (expressionLabel.getText().isEmpty() || equalsButtonClicked)) {
            operation = operator;

            num1 = Double.parseDouble(resultLabel.getText().replace(",", "."));

            String num1Formatted = UTILS.formatNumber(num1);
            expressionLabel.setText(num1Formatted + " " + operator);
            resultLabel.setText("");

            equalsButtonClicked = false;
        }
    }
}
