package FireflySimulation_Aufgabe2;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class FireflyClient {
    private static final int GRID_SIZE = 3; // Anzahl der Fireflies
    private static List<FireflyInterface> fireflies = new ArrayList<>();

    public static List<FireflyInterface> getFireflies() {
        return fireflies;
    }

    public static void main(String[] args) {
        initializeFireflies();
    }

    public static void initializeFireflies() {
        try {
            // Verbindung zu allen Firefly-Servern herstellen
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int port = 1099 + (i * GRID_SIZE + j);
                    FireflyInterface firefly = (FireflyInterface) Naming.lookup("rmi://localhost:" + port + "/Firefly" + (i * GRID_SIZE + j + 1));
                    fireflies.add(firefly);
                    System.out.println("Firefly" + (i * GRID_SIZE + j + 1) + " Client is running on port " + port);
                }
            }

            // Nachbarn für jede Firefly bestimmen und setzen
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    int index = i * GRID_SIZE + j;
                    FireflyInterface firefly = fireflies.get(index);
                    FireflyInterface[] neighbors = getNeighbors(index, fireflies.size());
                    firefly.setNeighbors(neighbors);
                    System.out.print("Nachbarn für Firefly" + (index + 1) + ": ");
                    for (FireflyInterface neighbor : neighbors) {
                        System.out.print("[" + neighbor.getX() + "," + neighbor.getY() + "] ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FireflyInterface[] getNeighbors(int index, int totalFireflies) {
        List<FireflyInterface> neighbors = new ArrayList<>();
        if (index > 0) {
            neighbors.add(fireflies.get(index - 1)); // Vorheriger Port
        }
        if (index < totalFireflies - 1) {
            neighbors.add(fireflies.get(index + 1)); // Nächster Port
        }
        return neighbors.toArray(new FireflyInterface[0]);
    }
}
