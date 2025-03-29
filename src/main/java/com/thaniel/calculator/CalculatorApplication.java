package com.thaniel.calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CalculatorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CalculatorApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        stage.setMinWidth(920);
        stage.setMinHeight(640);

        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.show();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/app-icon.png"))));
    }

    public static void main(String[] args) {
        launch();
    }
}