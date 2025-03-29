package com.thaniel.calculator.controllers;

import com.thaniel.calculator.model.Calculator;
import com.thaniel.calculator.utils.Messages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HistoricalController {
    @FXML
    private Label titleLabel;
    @FXML
    private Button clearButton;

    public BorderPane historicalBorderPane;

    @FXML
    private ScrollPane historicalScrollPane;

    @FXML
    private VBox historicalContainer;

    private final static Calculator model = Calculator.getInstance();

    @FXML
    public void initialize() {
        if (historicalContainer == null || historicalScrollPane == null) {
            System.err.println("Error: FXML in HistoricalController was not initialized correctly.");
        }else {
            setText();
        }
    }

    /*
     * Clear Historical button --> "Clear"
     */
    public void onClickClearHistorical(MouseEvent mouseEvent) {
        model.getHistorical().clear();
        if (historicalContainer != null) {
            historicalContainer.getChildren().clear();
        }
    }

    public void updateHistory(String historyEntry) {
        if (historicalContainer != null && historicalScrollPane != null) {
            model.getHistorical().add(0, historyEntry);
            historicalContainer.getChildren().clear();

            for (String entry : model.getHistorical()) {
                Label label = new Label(entry);
                label.setStyle("-fx-font-size: 18px; -fx-text-fill: white; -fx-padding: 5px;");
                Region space = new Region();
                space.setMinHeight(10);

                historicalContainer.getChildren().addAll(label, space);
            }

            Platform.runLater(() -> historicalScrollPane.setVvalue(0.0));
        }
    }

    private void setText() {
        clearButton.setText(Messages.get("text.button.clear"));
        clearButton.setTooltip(new Tooltip(Messages.get("tooltip.button.clear")));

        titleLabel.setText(Messages.get("text.label.title"));
    }
}
