package com.thaniel.calculator.utils;


import java.util.List;

public class Utils {

    public static final List<String> BINARY_OPERATIONS = List.of("+", "-", "×", "÷", "%", "mod", "exp", "xʸ");

    private static Utils instance;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }

        return instance;
    }

    public String formatNumber(double number) {
        if (number == (long) number) {
            return String.format("%d", (long) number);
        } else {
            return String.format("%s", number).replace(".", ",");
        }
    }

    /*
     * Formats the entered operand
     */
    public String formatOperation(String operation) {
        String formatted = operation;

        switch (operation) {
            case "x²", "xʸ", "eˣ", "10ˣ" -> formatted = "^";
            case "n!" -> formatted = "fact";
            case "|x|" -> formatted = "abs";
        }

        return formatted;
    }

    /*
     * Manages the introduction of commas in the number entered by the user
     */
    public String handleDecimals(String number, String buttonText) {
        return number.contains(",") ? number : (number.isEmpty() ? "0" : number) + buttonText;
    }

    /*
     * Format numbers in hexadecimal, octal, binary and decimal with spaces or points
     */
    public String formatNumber(String value, int groupSize, String separator) {
        StringBuilder formatted = new StringBuilder();
        int length = value.length();
        int remainder = length % groupSize;

        for (int i = 0; i < length; i++) {
            if (i > 0 && (i - remainder) % groupSize == 0) {
                formatted.append(separator);
            }
            formatted.append(value.charAt(i));
        }

        return formatted.toString();
    }

    /*
     * Removes the last digit from a number represented by a string
     */
    public String removeLastDigit(String number) {
        String result = number;

        if (!number.isEmpty() && !number.equals("0")) {
            result = (number.length() == 1) ? "0" : number.substring(0, number.length() - 1);
        }

        return result;
    }
}
