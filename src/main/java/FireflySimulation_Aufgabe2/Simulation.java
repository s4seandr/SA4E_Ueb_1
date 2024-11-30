package FireflySimulation_Aufgabe2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class Simulation extends Application {
    private static final int gridSize = 10;
    private static final int numberOfServers = gridSize * gridSize;
    private static final int startingPort = 1099;
    private List<Rectangle> rectangles = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        createGrid(pane);

        Scene scene = new Scene(pane, 650, 650); // Anpassen der Scene-Größe
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi Server Monitor");
        primaryStage.show();

        new Thread(this::updateValues).start();
    }

    private void createGrid(Pane pane) {
        int rectSize = 50; // Größe der Rechtecke
        int spacing = 10; // Abstand zwischen den Rechtecken

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double x = spacing + i * (rectSize + spacing);
                double y = spacing + j * (rectSize + spacing);
                Rectangle rectangle = new Rectangle(x, y, rectSize, rectSize);
                rectangle.setFill(Color.BLACK);
                rectangles.add(rectangle);
                pane.getChildren().add(rectangle);
            }
        }
    }

    private void updateValues() {
        try {
            while (true) {
                long currentTime = System.currentTimeMillis();
                for (int i = 0; i < numberOfServers; i++) {
                    int port = startingPort + i;
                    MyRemoteService service = (MyRemoteService) Naming.lookup("rmi://localhost:" + port + "/MyRemoteService");
                    double value = service.getPhaseValue(currentTime);
                    int finalI = i;
                    javafx.application.Platform.runLater(() -> rectangles.get(finalI).setOpacity(value));
                }
                // Pause für 100 Millisekunden (0,1 Sekunden)
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

