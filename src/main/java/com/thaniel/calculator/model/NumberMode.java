package com.thaniel.calculator.model;

public enum NumberMode {
    DECIMAL(10),
    BINARY(2),
    HEXADECIMAL(16),
    OCTAL(8);

    private final int radix;

    NumberMode(int radix) {
        this.radix = radix;
    }

    public int getRadix() {
        return radix;
    }
}
