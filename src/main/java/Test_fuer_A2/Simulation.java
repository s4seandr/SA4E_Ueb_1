package Test_fuer_A2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.rmi.Naming;

public class Simulation extends Application {
    private static final int gridSize = 10;
    private static final int numberOfServers = gridSize * gridSize;
    private static final int startingPort = 1099;
    private Rectangle[] rectangles = new Rectangle[numberOfServers];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        for (int i = 0; i < numberOfServers; i++) {
            Label label = new Label("Server " + (i + 1));
            Rectangle rectangle = new Rectangle(50, 50, Color.YELLOW); // Kleinere Rechtecke für mehr Platz
            rectangles[i] = rectangle;
            grid.add(label, i % gridSize, i / gridSize * 2); // Label in GridPane hinzufügen
            grid.add(rectangle, i % gridSize, i / gridSize * 2 + 1); // Rechteck in GridPane hinzufügen
        }

        Scene scene = new Scene(grid, 600, 1000); // Anpassen der Scene-Größe
        primaryStage.setScene(scene);
        primaryStage.setTitle("Multi Server Monitor");
        primaryStage.show();

        new Thread(this::updateValues).start();
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
                    javafx.application.Platform.runLater(() -> rectangles[finalI].setOpacity(value));
                }
                // Pause für 100 Millisekunden
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
