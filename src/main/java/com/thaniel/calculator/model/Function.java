package com.thaniel.calculator.model;


import javafx.scene.paint.Color;

public class Function {
    private String expression;
    private boolean visible;
    private Color color;

    public Function() {
    }

    public Function(String expression, boolean visible, Color color) {
        this.expression = expression;
        this.visible = visible;
        this.color = color;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void toggleVisibility() {
        this.visible = !this.visible;
    }
}
