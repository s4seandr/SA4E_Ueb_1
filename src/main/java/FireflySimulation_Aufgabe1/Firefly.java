package FireflySimulation_Aufgabe1;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Firefly implements Runnable {
    private Rectangle rect;
    private double phase;
    private final double period;
    private Firefly[] neighbors;
    private boolean running = true;
    private static final double COUPLING_STRENGTH = 0.025;  // Weniger Kopplung
    private static final double TIME_STEP = 0.05;  // Langsamere Phasenaktualisierung

    public Firefly(double x, double y) {
        rect = new Rectangle(x, y, 40, 40);
        phase = Math.random() * 2 * Math.PI;
        period = 2.0;  // Erhöhte Periode für langsamere Synchronisation
        rect.setFill(Color.BLACK);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setNeighbors(Firefly[] neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public void run() {
        while (running) {
            updatePhase();
            syncWithNeighbors();
            updateColor();
            try {
                Thread.sleep((long) (TIME_STEP * 1000));  // Erhöhte Schlafdauer
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }

    private void updatePhase() {
        phase += (2 * Math.PI / period) * TIME_STEP;
        if (phase >= 2 * Math.PI) {
            phase -= 2 * Math.PI;
        }
    }

    private void syncWithNeighbors() {
        for (Firefly neighbor : neighbors) {
            double phaseDifference = neighbor.phase - this.phase;
            this.phase += COUPLING_STRENGTH * Math.sin(phaseDifference);
        }
    }

    private void updateColor() {
        double brightness = (Math.sin(phase) + 1) / 2;
        javafx.application.Platform.runLater(() -> {
            rect.setFill(Color.gray(brightness));
        });
    }

    public void stop() {
        running = false;
    }
}
