package com.thaniel.calculator.utils;


import com.thaniel.calculator.model.Function;
import com.thaniel.calculator.model.Graph;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

public class GraphDrawer {
    private double minX, maxX, minY, maxY;

    private static GraphDrawer instance;

    public static GraphDrawer getInstance() {
        if (instance == null) {
            instance = new GraphDrawer();
        }

        return instance;
    }

    public void drawGraph(Canvas canvas, Graph graph) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, graph.getWidth(), graph.getHeight());

        updateBounds(graph);

        drawAxes(gc, graph);
        drawGrid(gc, graph);
        drawLabels(gc, graph);
        drawFunctions(gc, graph);
    }

    private void drawAxes(GraphicsContext gc, Graph graph) {
        double height = graph.getHeight();
        double width = graph.getWidth();
        double offsetX = graph.getOffsetX();
        double offsetY = graph.getOffsetY();

        gc.setStroke(Color.GRAY);
        gc.setLineWidth(3);

        double y = height / 2 + offsetY;
        double x = width / 2 + offsetX;

        // X and Y axis adjusted by zoom and scroll
        drawLineIfVisible(gc, graph, 0, y, width, y);
        drawLineIfVisible(gc, graph, x, 0, x, height);
    }

    private void drawGrid(GraphicsContext gc, Graph graph) {
        double height = graph.getHeight();
        double width = graph.getWidth();

        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(0.5);

        double scaleX = getScaleX(graph);
        double scaleY = getScaleY(graph);

        // Vertical lines and labels (X-axis)
        for (double x = Math.ceil(minX); x <= maxX; x++) {
            double screenX = coordXToScreenX(graph, x, scaleX);

            if (x != 0) {
                drawLineIfVisible(gc, graph, screenX, 0, screenX, height);
            }
        }

        // Horizontal lines and labels (Y-axis)
        for (double y = Math.ceil(minY); y <= maxY; y++) {
            double screenY = coordYtoScreenY(graph, y, scaleY);

            if (y != 0) {
                drawLineIfVisible(gc, graph, 0, screenY, width, screenY);
            }
        }
    }

    private void drawLabels(GraphicsContext gc, Graph graph) {
        double height = graph.getHeight();
        double width = graph.getWidth();
        double offsetX = graph.getOffsetX();
        double offsetY = graph.getOffsetY();

        gc.setFill(Color.GRAY);
        gc.setFont(new Font(12));

        double scaleX = getScaleX(graph);
        double scaleY = getScaleY(graph);

        // Vertical labels (X-axis)
        for (double x = Math.ceil(minX); x <= maxX; x++) {
            double screenX = coordXToScreenX(graph, x, scaleX);
            if (x != 0) {
                gc.fillText(String.format("%.0f", x), screenX + 2, height / 2 + offsetY - 5);
            }
        }

        // Horizontal labels (Y-axis)
        for (double y = Math.ceil(minY); y <= maxY; y++) {
            double screenY = coordYtoScreenY(graph, y, scaleY);
            if (y != 0) {
                gc.fillText(String.format("%.0f", y), width / 2 + offsetX + 5, screenY - 2);
            }
        }
    }

    private void drawFunctions(GraphicsContext gc, Graph graph) {
        for (Function f : graph.getFunctions()) {
            if (!f.getExpression().isEmpty() && f.isVisible()) {
                plotFunction(gc, f, graph);
            }
        }
    }

    private void plotFunction(GraphicsContext gc, Function function, Graph graph) {
        Argument xArg = new Argument("x");
        Expression expression = new Expression(function.getExpression(), xArg);

        gc.setStroke(function.getColor());
        gc.setLineWidth(2);

        double prevX = Double.NaN;
        double prevY = Double.NaN;
        double scaleX = getScaleX(graph);
        double scaleY = getScaleY(graph);

        for (double x = minX; x <= maxX; x += 0.1) {
            xArg.setArgumentValue(x);
            double y = expression.calculate();

            if (Double.isNaN(y) || Double.isInfinite(y)) continue;

            double screenX = coordXToScreenX(graph, x, scaleX);
            double screenY = coordYtoScreenY(graph, y, scaleY); // Invert Y axis to grow upwards

            if (!Double.isNaN(prevX) && !Double.isNaN(prevY)) {
                drawLineIfVisible(gc, graph, prevX, prevY, screenX, screenY); // Draw a line between consecutive points
            }

            prevX = screenX;
            prevY = screenY;
        }
    }

    private void drawLineIfVisible(GraphicsContext gc, Graph graph, double x1, double y1, double x2, double y2) {
        double width = graph.getWidth();
        double height = graph.getHeight();

        boolean isVisibleX = (x1 >= 0 && x1 <= width) || (x2 >= 0 && x2 <= width);
        boolean isVisibleY = (y1 >= 0 && y1 <= height) || (y2 >= 0 && y2 <= height);

        if (isVisibleX && isVisibleY) {
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    private void updateBounds(Graph g) {
        double height = g.getHeight();
        double width = g.getWidth();
        double offsetX = g.getOffsetX();
        double offsetY = g.getOffsetY();

        double scaleX = getScaleX(g);
        double scaleY = getScaleY(g);

        // The visible size of the graphic in units of the coordinate system
        double halfWidth = (width / 2) / scaleX;
        double halfHeight = (height / 2) / scaleY;

        // Adjust the limits
        minX = (-halfWidth - (offsetX / scaleX));
        maxX = (halfWidth - (offsetX / scaleX));
        minY = (-halfHeight + (offsetY / scaleY));
        maxY = (halfHeight + (offsetY / scaleY));
    }

    private double coordXToScreenX(Graph g, double x, double scaleX) {
        return g.getWidth() / 2 + (x * scaleX) + g.getOffsetX();
    }

    private double coordYtoScreenY(Graph g, double y, double scaleY) {
        return g.getHeight() / 2 - (y * scaleY) + g.getOffsetY();
    }

    private double getScaleX(Graph g) {
        return g.getWidth() / 20.0 * g.getZoomFactor();
    }

    private double getScaleY(Graph g) {
        return g.getHeight() / 20.0 * g.getZoomFactor();
    }
}