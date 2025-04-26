module com.thaniel.calculator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires commons.math3;
    requires MathParser.org.mXparser;
    requires java.desktop;

    opens com.thaniel.calculator to javafx.fxml;
    exports com.thaniel.calculator;
    exports com.thaniel.calculator.controllers;
    opens com.thaniel.calculator.controllers to javafx.fxml;
}