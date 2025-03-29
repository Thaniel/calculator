package com.thaniel.calculator.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Function> functions = new ArrayList<>();
    private double zoomFactor;
    private double offsetX;
    private double offsetY;
    private double width;
    private double height;

    public Graph() {
    }

    public Graph(double zoomFactor, double offsetX, double offsetY, double width, double height) {
        this.zoomFactor = zoomFactor;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public void addFunction(Function function) {
        if (function != null){
            this.functions.add(function);
        }
    }

    public void removeFunction(Function function) {
        if (function != null){
            this.functions.remove(function);
        }
    }
}
