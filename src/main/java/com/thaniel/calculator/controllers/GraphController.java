package com.thaniel.calculator.controllers;

import com.thaniel.calculator.model.Function;
import com.thaniel.calculator.model.Graph;
import com.thaniel.calculator.utils.GraphDrawer;
import com.thaniel.calculator.utils.Messages;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.License;


import java.util.Objects;

public class GraphController {
    @FXML
    private AnchorPane graphContainer;
    @FXML
    private Button centerGraphButton;
    @FXML
    private Button addFunctionButton;
    @FXML
    private Canvas graphCanvas;
    @FXML
    private TextField functionTextField;
    @FXML
    private VBox functionListContainer;

    private static final String VISIBLE_IMAGE_PATH = "/assets/buttons/visible-button.png";
    private static final String HIDDEN_IMAGE_PATH = "/assets/buttons/hidden-button.png";
    private static final String DELETE_IMAGE_PATH = "/assets/buttons/delete-button.png";
    private static final String STYLESHEET_PATH = "/calculator-stylesheet.css";

    private Graph graph;
    private double prevMouseX;
    private double prevMouseY;

    @FXML
    public void initialize() {
        if (graphCanvas == null) {
            System.err.println("Error: FXML in GraphController was not initialized correctly.");
        } else {
            System.out.println("Info: FXML in GraphController was initialized correctly.");

            License.iConfirmNonCommercialUse("Thaniel");
            setupMouseControls();
            graph = new Graph(1.0, 0, 0, graphCanvas.getWidth(), graphCanvas.getHeight());

            // Ajustar el tamaño del Canvas al AnchorPane
            graphCanvas.widthProperty().bind(graphContainer.widthProperty());
            graphCanvas.heightProperty().bind(graphContainer.heightProperty());

            // Redibujar el gráfico cada vez que el tamaño cambie
            graphCanvas.widthProperty().addListener((obs, oldVal, newVal) -> drawGraph());
            graphCanvas.heightProperty().addListener((obs, oldVal, newVal) -> drawGraph());


            drawGraph();
            setButtonTooltips();
        }
    }

    private void setupMouseControls() {
        graphCanvas.setOnScroll(this::handleZoom);
        graphCanvas.setOnMousePressed(this::handleMousePressed);
        graphCanvas.setOnMouseDragged(this::handleMouseDragged);
    }

    public void onClickCenterGraph(MouseEvent mouseEvent) {
        graph.setZoomFactor(1.0);
        graph.setOffsetX(0);
        graph.setOffsetY(0);
        drawGraph();
    }

    public void onClickAddFunction(){
        String expression = functionTextField.getText().trim();

        if (!expression.isEmpty()){
            addFunction(expression);
        }
    }

    public void onKeyPressAddFunction(KeyEvent keyEvent) {
        String expression = functionTextField.getText().trim();

        if (!expression.isEmpty() && keyEvent.getCode() == KeyCode.ENTER) {
            addFunction(expression);
        }
    }

    private void addFunction(String expression){
        Color color = getRandomLightColor();
        Function function = new Function(expression, true, color);
        graph.addFunction(function);

        addFunctionUI(function);
        drawGraph();
        functionTextField.setText("");
    }

    private void addFunctionUI(Function function) {
        HBox functionBox = new HBox();
        TextField funcTextField = createTextField(function);
        ColorPicker funcColorPicker = createColorPicker(function);
        Button visibilityBtn = createVisibilityButton(function);
        Button deleteBtn = createDeleteButton(function, functionBox);

        functionBox.getChildren().addAll(funcTextField, funcColorPicker, visibilityBtn, deleteBtn);
        VBox.setMargin(functionBox, new Insets(10, 10, 10, 10));
        functionListContainer.getChildren().add(functionBox);
    }

    private void handleZoom(ScrollEvent event) {
        double width = graphCanvas.getWidth();
        double height = graphCanvas.getHeight();
        double zoomFactor = graph.getZoomFactor();
        double offsetX = graph.getOffsetX();
        double offsetY = graph.getOffsetY();

        // Calculate relative cursor position
        double mouseX = event.getX();
        double mouseY = event.getY();
        double graphX = (mouseX - width / 2 - offsetX) / zoomFactor;
        double graphY = (mouseY - height / 2 - offsetY) / zoomFactor;

        zoomFactor *= (event.getDeltaY() > 0) ? 1.1 : 0.9;

        // Adjust translation to keep point under cursor fixed
        offsetX = mouseX - (graphX * zoomFactor + width / 2);
        offsetY = mouseY - (graphY * zoomFactor + height / 2);

        graph.setZoomFactor(zoomFactor);
        graph.setOffsetX(offsetX);
        graph.setOffsetY(offsetY);
        drawGraph();
    }

    private void handleMousePressed(MouseEvent event) {
        prevMouseX = event.getX();
        prevMouseY = event.getY();
    }

    private void handleMouseDragged(MouseEvent event) {
        double newOffsetX = graph.getOffsetX() + (event.getX() - prevMouseX);
        double newOffsetY = graph.getOffsetY() + (event.getY() - prevMouseY);

        prevMouseX = event.getX();
        prevMouseY = event.getY();

        graph.setOffsetX(newOffsetX);
        graph.setOffsetY(newOffsetY);
        drawGraph();
    }

    private void drawGraph() {
        graph.setWidth(graphCanvas.getWidth());
        graph.setHeight(graphCanvas.getHeight());
        GraphDrawer.getInstance().drawGraph(graphCanvas, graph);
    }

    private TextField createTextField(Function function) {
        TextField textField = new TextField(function.getExpression());
        textField.setMaxHeight(Double.MAX_VALUE);
        textField.getStyleClass().add("background-grey-dark");
        textField.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                function.setExpression(textField.getText());
                drawGraph();
            }
        });

        return textField;
    }

    private ColorPicker createColorPicker(Function function){
        ColorPicker colorPicker = new ColorPicker(function.getColor());
        colorPicker.setMaxHeight(Double.MAX_VALUE);
        colorPicker.setMaxWidth(34);
        colorPicker.getStyleClass().add("menu-button");
        colorPicker.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        colorPicker.setOnAction(event -> {
            function.setColor(colorPicker.getValue());
            drawGraph();
        });

        colorPicker.setTooltip(new Tooltip(Messages.get("tooltip.button.changeColor")));

        /*
           colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                function.setColor(newValue);
                drawGraph();
            });
         */

        return colorPicker;
    }

    private Button createVisibilityButton(Function function){
        ImageView visibilityImageView = createImageView(HIDDEN_IMAGE_PATH);

        Button button = new Button();
        button.getStyleClass().add("menu-button");
        button.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
        button.setGraphic(visibilityImageView);
        button.setPadding(new Insets(5, 5, 5, 5));

        button.setOnMouseClicked(event -> {
            String imagePath = function.isVisible() ? VISIBLE_IMAGE_PATH : HIDDEN_IMAGE_PATH;
            visibilityImageView.setImage(new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm()));
            function.toggleVisibility();
            drawGraph();
        });

        button.setTooltip(new Tooltip(Messages.get("tooltip.button.changeVisibility")));

        return button;
    }

    private Button createDeleteButton(Function function, HBox functionBox){
        ImageView deleteImageView = createImageView(DELETE_IMAGE_PATH);

        Button button = new Button();
        button.getStyleClass().add("menu-button");
        button.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());
        button.setGraphic(deleteImageView);
        button.setPadding(new Insets(5, 5, 5, 5));

        button.setOnMouseClicked(event -> {
            functionListContainer.getChildren().remove(functionBox);
            graph.removeFunction(function);
            drawGraph();
        });

        button.setTooltip(new Tooltip(Messages.get("tooltip.button.deleteFunction")));

        return button;
    }

    private ImageView createImageView(String imagePath){
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm()));
        imageView.setFitWidth(24);
        imageView.setFitHeight(24);

        return imageView;
    }

    private Color getRandomLightColor() {
        double red = 0.1 + Math.random() * 0.9;
        double green = 0.1 + Math.random() * 0.9;
        double blue = 0.1 + Math.random() * 0.9;
        return new Color(red, green, blue, 1.0);
    }

    private void setButtonTooltips() {
        centerGraphButton.setTooltip(new Tooltip(Messages.get("tooltip.button.centerGraph")));
        addFunctionButton.setTooltip(new Tooltip(Messages.get("tooltip.button.addFunction")));
    }
}