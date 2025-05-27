package com.thaniel.calculator.utils;


import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static final List<String> BINARY_OPERATIONS = List.of("+", "-", "×", "÷", "%", "mod", "exp", "xʸ");
    private static final BigDecimal UPPER_LIMIT = new BigDecimal("1000000000");
    private static final BigDecimal LOWER_LIMIT = new BigDecimal("0.0000001");
    private static Utils instance;

    public static Utils getInstance() {
        if (instance == null) {
            instance = new Utils();
        }

        return instance;
    }

    public String formatNumberOld(BigDecimal number) {
        String numberFormatted;

        if (number.stripTrailingZeros().scale() <= 0) {
            numberFormatted = formatNumberWithSpaces(number.stripTrailingZeros().toPlainString(), 3, ".");
        } else {
            String[] parts = String.format(Locale.US, "%.15f", number).replace(".", ",").split(",");
            String intPart = formatNumberWithSpaces(parts[0], 3, ".");
            String decimals = parts[1].replaceAll("0+$", "");

            numberFormatted = intPart + (decimals.isEmpty() ? "" : "," + decimals);
        }

        return numberFormatted;
    }

    /*
     * Format a Number:
     *  - Use scientific notation if very large or very small (but ≠ 0)
     *  - Separate the number every 3 digits with a "."
     *  - Usa "," to separate decimal part
     */
    public String formatNumber(BigDecimal number) {
        BigDecimal absNumber = number.abs();
        String numberFormatted;

        if ((absNumber.compareTo(UPPER_LIMIT) >= 0 || absNumber.compareTo(LOWER_LIMIT) < 0) && number.compareTo(BigDecimal.ZERO) != 0) {
            numberFormatted = number.stripTrailingZeros().toEngineeringString().replace(".", ",");
        } else {
            if (number.stripTrailingZeros().scale() <= 0) {
                numberFormatted = formatNumberWithSpaces(number.stripTrailingZeros().toPlainString(), 3, ".");
            } else {
                String[] parts = String.format(Locale.US, "%.15f", number).replace(".", ",").split(",");
                String intPart = formatNumberWithSpaces(parts[0], 3, ".");
                String decimals = parts[1].replaceAll("0+$", "");

                numberFormatted = intPart + (decimals.isEmpty() ? "" : "," + decimals);
            }
        }

        return numberFormatted;
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
    public String formatNumberWithSpaces(String value, int groupSize, String separator) {
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
