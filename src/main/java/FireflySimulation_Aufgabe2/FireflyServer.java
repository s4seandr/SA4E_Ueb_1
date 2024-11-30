package FireflySimulation_Aufgabe2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class FireflyServer {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 9; i++) { // Beispielhaft 9 Fireflies erstellen
                int port = 1099 + i;
                LocateRegistry.createRegistry(port); // Startet die RMI-Registry
                Firefly firefly = new Firefly(100 * (i % 3), 100 * (i / 3));
                Naming.rebind("rmi://localhost:" + port + "/Firefly" + (i + 1), firefly);
                System.out.println("Firefly" + (i + 1) + " Server is running on port " + port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

