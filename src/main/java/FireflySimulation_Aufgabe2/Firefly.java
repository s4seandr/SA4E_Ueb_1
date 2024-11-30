package FireflySimulation_Aufgabe2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.ReentrantLock;

public class Firefly extends UnicastRemoteObject implements FireflyInterface, Runnable {
    private Rectangle rect;
    private double phase;
    private final double period;
    private FireflyInterface[] neighbors;
    private boolean running = true;
    private static final double COUPLING_STRENGTH = 0.025;
    protected static final double TIME_STEP = 0.05;
    private final ReentrantLock lock = new ReentrantLock();

    public Firefly(double x, double y) throws RemoteException {
        rect = new Rectangle(x, y, 40, 40);
        phase = Math.random() * 2 * Math.PI;
        period = 2.0;
        rect.setFill(Color.BLACK);
        System.out.println("Firefly created at position (" + x + ", " + y + ")");
    }

    public double getX() {
        return rect.getX();
    }

    public double getY() {
        return rect.getY();
    }

    public double getBrightness() {
        lock.lock();
        try {
            return (Math.sin(phase) + 1) / 2;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setNeighbors(FireflyInterface[] neighbors) throws RemoteException {
        lock.lock();
        try {
            this.neighbors = neighbors;
            System.out.println("Neighbors set for Firefly at position (" + getX() + ", " + getY() + ")");
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void updatePhase() throws RemoteException {
        lock.lock();
        try {
            phase += (2 * Math.PI / period) * TIME_STEP;
            if (phase >= 2 * Math.PI) {
                phase -= 2 * Math.PI;
            }
            System.out.println("Phase updated for Firefly at position (" + getX() + ", " + getY() + "): " + phase);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized void syncWithNeighbors() throws RemoteException {
        lock.lock();
        try {
            for (FireflyInterface neighbor : neighbors) {
                double neighborPhase = neighbor.getPhase(); // Abrufen der Phase vom entsprechenden Server
                System.out.println("Fetching phase from neighbor at position (" + neighbor.getX() + ", " + neighbor.getY() + "): " + neighborPhase);
                double phaseDifference = neighborPhase - this.phase;
                this.phase += COUPLING_STRENGTH * Math.sin(phaseDifference);
                System.out.println("Synced with neighbor for Firefly at position (" + getX() + ", " + getY() + "), new phase: " + this.phase);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized double getPhase() throws RemoteException {
        lock.lock();
        try {
            System.out.println("Getting phase for Firefly at position (" + getX() + ", " + getY() + "): " + this.phase);
            return this.phase;
        } finally {
            lock.unlock();
        }
    }

    private void updateColor() {
        lock.lock();
        try {
            double brightness = (Math.sin(phase) + 1) / 2;
            javafx.application.Platform.runLater(() -> {
                rect.setFill(Color.gray(brightness));
                System.out.println("Color updated for Firefly at position (" + getX() + ", " + getY() + ")");
            });
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                System.out.println("Running phase update and synchronization for Firefly at position (" + getX() + ", " + getY() + ")");
                updatePhase();
                syncWithNeighbors();
                updateColor();
                Thread.sleep((long) (TIME_STEP * 1000));
            } catch (InterruptedException | RemoteException e) {
                running = false;
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
