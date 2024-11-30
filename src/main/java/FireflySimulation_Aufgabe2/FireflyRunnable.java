package FireflySimulation_Aufgabe2;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

import java.rmi.RemoteException;

public class FireflyRunnable implements Runnable {
    private final FireflyInterface firefly;
    private final Rectangle rect;

    public FireflyRunnable(FireflyInterface firefly, Rectangle rect) {
        this.firefly = firefly;
        this.rect = rect;
    }

    @Override
    public void run() {
        System.out.println("Thread started for Firefly at position (" + rect.getX() + ", " + rect.getY() + ")");
        try {
            while (true) {
                firefly.updatePhase();
                firefly.syncWithNeighbors();

                Platform.runLater(() -> {
                    try {
                        double brightness = firefly.getBrightness();
                        rect.setFill(javafx.scene.paint.Color.gray(brightness));
                        System.out.println("Brightness updated for Firefly at position (" + rect.getX() + ", " + rect.getY() + "): " + brightness);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });

                Thread.sleep((long) (Firefly.TIME_STEP * 1000));
            }
        } catch (InterruptedException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
