package com.thaniel.calculator.controllers;


import com.thaniel.calculator.model.NumberMode;
import com.thaniel.calculator.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;


public class ProgrammerController {
    @FXML
    protected Label expressionLabel, resultLabel;

    @FXML
    private Label decimalLabel, binaryLabel, octalLabel, hexadecimalLabel;

    @FXML
    private Label decLabel, binLabel, octLabel, hexLabel;

    @FXML
    private GridPane buttonGrid;

    private static final Utils UTILS = Utils.getInstance();
    private static final Color SELECTED_COLOR = Color.web("#43BBFB");
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private static final List<String> ALWAYS_ENABLED_BUTTONS = List.of("+", "-", "×", "÷", "=", "AC", "DEL");
    protected static MainViewController mainViewController;

    private NumberMode mode = NumberMode.DECIMAL;
    protected String operation = null;
    protected String num1 = "0";
    protected String num2 = "0";
    protected String result = null;
    protected boolean equalsButtonClicked = false;

    @FXML
    public void initialize() {
        if (decimalLabel == null || binaryLabel == null || octalLabel == null || hexadecimalLabel == null || expressionLabel == null || resultLabel == null || buttonGrid == null) {
            System.err.println("Error: FXML in ProgrammerController was not initialized correctly.");
        } else {
            System.out.println("Info: FXML in ProgrammerController was initialized correctly.");

            updateValues();
            updateModeUI();
        }
    }

    public void setMainViewController(MainViewController controller) {
        mainViewController = controller;
    }

    @FXML
    private void onModeChange(MouseEvent event) {
        Label clickedLabel = (Label) event.getSource();
        NumberMode previousMode = mode;

        switch (clickedLabel.getId()) {
            case "decLabel", "decimalLabel" -> mode = NumberMode.DECIMAL;
            case "binLabel", "binaryLabel" -> mode = NumberMode.BINARY;
            case "hexLabel", "hexadecimalLabel" -> mode = NumberMode.HEXADECIMAL;
            case "octLabel", "octalLabel" -> mode = NumberMode.OCTAL;
        }

        updateModeUI();
        updateLabels(previousMode);
    }

    /*
     * Number Button --> "1", "2", "3", "4", "5", "6", "7" , "8", "9"
     */
    public void onClickNumber(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        insertDigit(button.getText());
    }

    /*
     * Operation Button --> "+", "-" , "x" and "/"
     */
    public void onClickOperation(MouseEvent mouseEvent) {
        String operator = ((Button) mouseEvent.getSource()).getText();
        handleOperation(operator);

        updateValues();
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
        if (operation == null) {
            num1 = UTILS.removeLastDigit(num1);
        } else {
            num2 = UTILS.removeLastDigit(num2);
        }
        updateValues();
    }

    /*
     * Clear button --> "AC"
     */
    public void onClickClear(MouseEvent mouseEvent) {
        expressionLabel.setText("");
        restartValues();
        updateValues();
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        String text = keyEvent.getText();

        switch (keyCode) {
            case ADD -> handleOperation("+");
            case SUBTRACT -> handleOperation("-");
            case MULTIPLY -> handleOperation("×");
            case DIVIDE -> handleOperation("÷");
            case ENTER -> handleCalculate();
            case BACK_SPACE -> onClickDelete(null);
            case A, B, C, D, E, F -> insertDigit(keyCode.getName());
            default -> insertDigit(text);
        }
    }

    private void updateLabels(NumberMode previousMode) {
        Long longNumber1 = convertNumberToLong(num1, previousMode);
        num1 = convertLongToString(longNumber1, mode);

        if (operation == null) {
            resultLabel.setText(num1);
        } else{
            Long longNumber2 = convertNumberToLong(num2, previousMode);
            num2 = convertLongToString(longNumber2, mode);

            if (!equalsButtonClicked) {
                resultLabel.setText(num2);
                expressionLabel.setText(num1 + " " + operation);
            } else {
                Long longResult = convertNumberToLong(result, previousMode);
                result = convertLongToString(longResult, mode);

                resultLabel.setText(num1 + " " + operation + " " + num2 + " =");
                expressionLabel.setText(result);
            }
        }
    }

    private void updateModeUI() {
        disableButtons();
        resetLabelColors();

        switch (mode) {
            case DECIMAL -> highlightLabels(decLabel, decimalLabel);
            case BINARY -> highlightLabels(binLabel, binaryLabel);
            case HEXADECIMAL -> highlightLabels(hexLabel, hexadecimalLabel);
            case OCTAL -> highlightLabels(octLabel, octalLabel);
        }
    }

    private void disableButtons() {
        for (Node node : buttonGrid.getChildren()) {
            if (node instanceof Button button) {
                String text = button.getText().toUpperCase();
                button.setDisable(!ALWAYS_ENABLED_BUTTONS.contains(text) && !isValidForMode(text));
            }
        }
    }

    private void highlightLabels(Label modeLabel, Label valueLabel) {
        modeLabel.setTextFill(SELECTED_COLOR);
        valueLabel.setTextFill(SELECTED_COLOR);
    }

    private void resetLabelColors() {
        decLabel.setTextFill(DEFAULT_COLOR);
        binLabel.setTextFill(DEFAULT_COLOR);
        hexLabel.setTextFill(DEFAULT_COLOR);
        octLabel.setTextFill(DEFAULT_COLOR);

        decimalLabel.setTextFill(DEFAULT_COLOR);
        binaryLabel.setTextFill(DEFAULT_COLOR);
        hexadecimalLabel.setTextFill(DEFAULT_COLOR);
        octalLabel.setTextFill(DEFAULT_COLOR);
    }

    private void insertDigit(String digit) {
        if (isValidForMode(digit)) {
            if (operation == null) {
                num1 = (Objects.equals(num1, "0")) ? digit.toUpperCase() : num1 + digit.toUpperCase();
            } else {
                num2 = (Objects.equals(num2, "0")) ? digit.toUpperCase() : num2 + digit.toUpperCase();
            }

            updateValues();
        }
    }

    private boolean isValidForMode(String text) {
        return switch (mode) {
            case DECIMAL -> text.matches("[0-9]");
            case BINARY -> text.matches("[01]");
            case HEXADECIMAL -> text.matches("[0-9A-F]");
            case OCTAL -> text.matches("[0-7]");
        };
    }

    private void updateValues() {
        String numberToConvert = (operation == null) ? num1 : num2;

        //if (!isGreaterThanLongMax(numberToConvert)) {
        try {
            long longNumber = convertNumberToLong(numberToConvert, mode);

            decimalLabel.setText(UTILS.formatNumber(String.valueOf(longNumber), 3, "."));
            binaryLabel.setText(UTILS.formatNumber(Long.toBinaryString(longNumber), 4, " "));
            hexadecimalLabel.setText(UTILS.formatNumber(Long.toHexString(longNumber).toUpperCase(), 4, " "));
            octalLabel.setText(UTILS.formatNumber(Long.toOctalString(longNumber), 3, " "));
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format (" + numberToConvert + ")");
        }

        if (operation == null) {
            resultLabel.setText(numberToConvert);
        } else {
            resultLabel.setText(numberToConvert);
            expressionLabel.setText(num1 + " " + operation);
        }
        //}
    }

    /*private boolean isGreaterThanLongMax(String number) {
        try {
            Long.parseLong(number, 16);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }*/

    protected void handleOperation(String operator) {
        if (operation == null || equalsButtonClicked) {
            operation = operator;

            updateValues();
            resultLabel.setText("");

            equalsButtonClicked = false;
        }
    }

    protected void handleCalculate() {
        if (operation != null && num1 != null) {
            equalsButtonClicked = true;

            Long longNumber1 = convertNumberToLong(num1, mode);
            Long longNumber2 = convertNumberToLong(num2, mode);
            Long longResult = 0L;

            switch (operation) {
                case "+" -> longResult = longNumber1 + longNumber2;
                case "-" -> longResult = longNumber1 - longNumber2;
                case "×" -> longResult = longNumber1 * longNumber2;
                case "÷" -> longResult = (longNumber2 != 0) ? longNumber1 / longNumber2 : null;
            }

            result = convertLongToString(longResult, mode);

            displayOperationAndResult();
        }
    }

    private void displayOperationAndResult() {
        resultLabel.setText(result);
        expressionLabel.setText(num1 + " " + operation + " " + num2 + " = ");

        String historyEntry = expressionLabel.getText() + resultLabel.getText() + " (" + mode.toString().substring(0, 3) + ")";

        if (mainViewController != null) {
            mainViewController.addHistoryEntry(historyEntry);
        }

        restartValues();

        num1 = resultLabel.getText();
        updateValues();
    }

    private void restartValues() {
        num1 = "0";
        num2 = "0";
        result = null;
        operation = null;
    }

    private Long convertNumberToLong(String number, NumberMode mode) {
        long longNumber = 0;

        number = removeSpacesAndPoints(number);

        switch (mode) {
            case DECIMAL -> longNumber = Long.parseLong(number);
            case BINARY -> longNumber = Long.parseLong(number, 2);
            case HEXADECIMAL -> longNumber = Long.parseLong(number, 16);
            case OCTAL -> longNumber = Long.parseLong(number, 8);
        }

        return longNumber;
    }

    private String convertLongToString(Long longNumber, NumberMode mode) {
        String number = "0";

        switch (mode) {
            case DECIMAL -> number = UTILS.formatNumber(String.valueOf(longNumber), 3, ".");
            case BINARY -> number = UTILS.formatNumber(Long.toBinaryString(longNumber), 4, " ");
            case HEXADECIMAL -> number = UTILS.formatNumber(Long.toHexString(longNumber).toUpperCase(), 4, " ");
            case OCTAL -> number = UTILS.formatNumber(Long.toOctalString(longNumber), 3, " ");
        }

        return number;
    }

    private static String removeSpacesAndPoints(String number) {
        return number.replaceAll("\\s|\\.", "");
    }
}
