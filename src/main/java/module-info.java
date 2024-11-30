module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;

    opens FireflySimulation_Aufgabe1 to javafx.fxml;
    exports FireflySimulation_Aufgabe1;

    opens FireflySimulation_Aufgabe2 to javafx.graphics;
    exports FireflySimulation_Aufgabe2;

    opens Test_fuer_A2 to java.rmi;
    exports Test_fuer_A2;
}