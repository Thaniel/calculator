package com.thaniel.calculator.controllers;

import com.thaniel.calculator.utils.Messages;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainViewController {
    @FXML
    private Button menuButton;
    @FXML
    private Button basicModeButton;
    @FXML
    private Button advancedModeButton;
    @FXML
    private Button graphModeButton;
    @FXML
    private Button programmerModeButton;

    @FXML
    private VBox menuPane;
    private boolean menuVisible = false;

    @FXML
    private StackPane historicalPane;
    private HistoricalController historicalController;

    @FXML
    private StackPane basicCalculatorPane;
    private BasicCalculatorController basicCalculatorController;

    @FXML
    private StackPane advancedCalculatorPane;
    private AdvancedCalculatorController advancedCalculatorController;

    @FXML
    private StackPane graphPane;
    private GraphController graphController;

    @FXML
    private StackPane programmerPane;
    private ProgrammerController programmerController;

    @FXML
    public void initialize() {
        loadFXMLToPane("/com/thaniel/calculator/historical-pane.fxml", historicalPane);
        loadFXMLToPane("/com/thaniel/calculator/basic-calculator-pane.fxml", basicCalculatorPane);
        switchCalculatorMode(basicCalculatorPane);
        setButtonTooltips();
    }

    private void loadFXMLToPane(String fxmlPath, StackPane pane) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (loader.getController() instanceof BasicCalculatorController) {
                basicCalculatorController = loader.getController();
                basicCalculatorController.setMainViewController(this);
            } else if (loader.getController() instanceof AdvancedCalculatorController) {
                advancedCalculatorController = loader.getController();
                advancedCalculatorController.setMainViewController(this);
            } else if (loader.getController() instanceof GraphController) {
                graphController = loader.getController();
            } else if (loader.getController() instanceof ProgrammerController) {
                programmerController = loader.getController();
                programmerController.setMainViewController(this);
            } else if (loader.getController() instanceof HistoricalController) {
                historicalController = loader.getController();
            }

            pane.getChildren().add(root);
        } catch (IOException e) {
            System.err.println("Error: Failed to load controllers");
        }
    }

    @FXML
    private void toggleMenu() {
        double targetX = menuVisible ? -200 : 0;

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), menuPane);
        transition.setToX(targetX);
        transition.play();

        menuVisible = !menuVisible;
    }

    @FXML
    private void handleBasicMode() {
        switchCalculatorMode(basicCalculatorPane);
        toggleMenu();
    }

    @FXML
    private void handleAdvancedMode() {
        if (advancedCalculatorController == null) {
            loadFXMLToPane("/com/thaniel/calculator/advanced-calculator-pane.fxml", advancedCalculatorPane);
        }

        switchCalculatorMode(advancedCalculatorPane);
        toggleMenu();
    }

    @FXML
    private void handleGraphMode() {
        if (graphController == null) {
            loadFXMLToPane("/com/thaniel/calculator/graph-pane.fxml", graphPane);
        }

        Scene scene = graphPane.getScene();
        graphController.setScene(scene);

        switchCalculatorMode(graphPane);
        toggleMenu();

    }

    @FXML
    private void handleProgrammerMode() {
        if (programmerController == null) {
            loadFXMLToPane("/com/thaniel/calculator/programmer-pane.fxml", programmerPane);
        }

        switchCalculatorMode(programmerPane);
        toggleMenu();
    }

    public void addHistoryEntry(String historyEntry) {
        if (historicalController != null) {
            historicalController.updateHistory(historyEntry);
        }
    }

    private void switchCalculatorMode(StackPane modeToShow) {
        basicCalculatorPane.setVisible(modeToShow == basicCalculatorPane);
        basicCalculatorPane.setManaged(modeToShow == basicCalculatorPane);

        advancedCalculatorPane.setVisible(modeToShow == advancedCalculatorPane);
        advancedCalculatorPane.setManaged(modeToShow == advancedCalculatorPane);

        graphPane.setVisible(modeToShow == graphPane);
        graphPane.setManaged(modeToShow == graphPane);

        programmerPane.setVisible(modeToShow == programmerPane);
        programmerPane.setManaged(modeToShow == programmerPane);

        historicalPane.setVisible(modeToShow == basicCalculatorPane || modeToShow == advancedCalculatorPane || modeToShow == programmerPane);
        historicalPane.setManaged(modeToShow == basicCalculatorPane || modeToShow == advancedCalculatorPane || modeToShow == programmerPane);
    }

    private void setButtonTooltips() {
        basicModeButton.setText(" " + Messages.get("text.label.basic"));
        advancedModeButton.setText(" " + Messages.get("text.label.advanced"));
        graphModeButton.setText(" " + Messages.get("text.label.graph"));
        programmerModeButton.setText(" " + Messages.get("text.label.programmer"));

        menuButton.setTooltip(new Tooltip(Messages.get("tooltip.button.menu")));
        basicModeButton.setTooltip(new Tooltip(Messages.get("tooltip.button.basic")));
        advancedModeButton.setTooltip(new Tooltip(Messages.get("tooltip.button.advanced")));
        graphModeButton.setTooltip(new Tooltip(Messages.get("tooltip.button.graph")));
        programmerModeButton.setTooltip(new Tooltip(Messages.get("tooltip.button.programmer")));
    }
}