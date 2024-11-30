package FireflySimulation_Aufgabe1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FireflySimulation extends Application {

    private static final int GRID_SIZE = 10;  // Größe des Rasters 10x10
    private List<Firefly> fireflies = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 600, 600);
        primaryStage.setTitle("Firefly Synchronization");
        primaryStage.setScene(scene);
        primaryStage.show();

        createFireflies(pane);
        startFireflies();
    }

    private void createFireflies(Pane pane) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                double x = 50 + i * 50;
                double y = 50 + j * 50;
                Firefly firefly = new Firefly(x, y);
                fireflies.add(firefly);
                pane.getChildren().add(firefly.getRect());
            }
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Firefly firefly = fireflies.get(i * GRID_SIZE + j);
                Firefly[] neighbors = getNeighbors(i, j);
                firefly.setNeighbors(neighbors);
            }
        }
    }

    private Firefly[] getNeighbors(int i, int j) {
        List<Firefly> neighbors = new ArrayList<>();
        int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} };
        for (int[] dir : directions) {
            int ni = (i + dir[0] + GRID_SIZE) % GRID_SIZE;
            int nj = (j + dir[1] + GRID_SIZE) % GRID_SIZE;
            neighbors.add(fireflies.get(ni * GRID_SIZE + nj));
        }
        return neighbors.toArray(new Firefly[0]);
    }

    private void startFireflies() {
        for (Firefly firefly : fireflies) {
            Thread thread = new Thread(firefly);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void stop() {
        for (Firefly firefly : fireflies) {
            firefly.stop();
        }
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
