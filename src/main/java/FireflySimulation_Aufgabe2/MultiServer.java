package FireflySimulation_Aufgabe2;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

public class MultiServer {
    public static void main(String[] args) {
        int gridSize = 10;
        int numberOfServers = gridSize * gridSize;
        int startingPort = 1099;
        MyRemoteService[] services = new MyRemoteService[numberOfServers];
        Random random = new Random();

        try {
            for (int i = 0; i < numberOfServers; i++) {
                int port = startingPort + i;
                LocateRegistry.createRegistry(port);
                MyRemoteService service = new MyRemoteServiceImpl();
                Naming.rebind("rmi://localhost:" + port + "/MyRemoteService", service);
                services[i] = service;
                System.out.println("Server " + (i + 1) + " is running on port " + port + "...");
            }

            while (true) {
                for (int i = 0; i < numberOfServers; i++) {
                    double[] neighborPhases = new double[4];
                    int neighborCount = 0;

                    // Finde Nachbarn in einem 10x10 Raster
                    if (i - gridSize >= 0) neighborPhases[neighborCount++] = services[i - gridSize].getPhaseValue(System.currentTimeMillis());
                    if (i + gridSize < numberOfServers) neighborPhases[neighborCount++] = services[i + gridSize].getPhaseValue(System.currentTimeMillis());
                    if (i % gridSize != 0) neighborPhases[neighborCount++] = services[i - 1].getPhaseValue(System.currentTimeMillis());
                    if (i % gridSize != (gridSize - 1)) neighborPhases[neighborCount++] = services[i + 1].getPhaseValue(System.currentTimeMillis());

                    services[i].updatePhase();
                    services[i].updateNeighborPhases(neighborPhases);
                }
                Thread.sleep(100); // Aktualisierungsrate erhöhen
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
