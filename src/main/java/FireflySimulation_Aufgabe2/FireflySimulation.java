package FireflySimulation_Aufgabe2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FireflySimulation extends Application {
    private static final int GRID_SIZE = 3; // Anzahl der Fireflies
    private List<FireflyInterface> fireflies = FireflyClient.getFireflies();
    private List<Rectangle> rectangles = new ArrayList<>();

    public static void main(String[] args) {
        FireflyClient.initializeFireflies(); // Fireflies initialisieren, bevor die GUI startet
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setTitle("Firefly Synchronization");
        primaryStage.setScene(scene);
        primaryStage.show();

        createFireflyRectangles(pane);
        startUpdatingFireflies();
    }

    private void createFireflyRectangles(Pane pane) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                FireflyInterface firefly = fireflies.get(i * GRID_SIZE + j);

                // Log-Ausgabe für das Hinzufügen von Fireflies
                System.out.println("Firefly" + (i * GRID_SIZE + j + 1) + " hinzugefügt.");

                // Create a new Rectangle for each firefly
                Rectangle rect = new Rectangle(100 + 60 * j, 100 + 60 * i, 40, 40);
                rect.setFill(Color.BLACK);
                rectangles.add(rect);
                pane.getChildren().add(rect);
            }
        }
    }

    private void startUpdatingFireflies() {
        Thread updateThread = new Thread(() -> {
            while (true) {
                try {
                    for (int i = 0; i < fireflies.size(); i++) {
                        FireflyInterface firefly = fireflies.get(i);
                        Rectangle rect = rectangles.get(i);

                        double brightness = firefly.getBrightness();
                        javafx.application.Platform.runLater(() -> rect.setFill(Color.gray(brightness)));

                        // Log-Ausgabe zur Überprüfung der Helligkeit
                        System.out.println("Brightness for Firefly" + (i + 1) + ": " + brightness);
                    }
                    Thread.sleep((long) (Firefly.TIME_STEP * 500)); // Reduzierte `sleep`-Time
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        updateThread.setDaemon(true); // Hintergrundthread
        updateThread.start();
    }
}
