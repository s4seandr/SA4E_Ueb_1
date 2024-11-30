package Test_fuer_A2;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class MultiServer {
    public static void main(String[] args) {
        int gridSize = 10;
        int numberOfServers = gridSize * gridSize;
        int startingPort = 1099;
        MyRemoteService[] services = new MyRemoteService[numberOfServers];

        try {
            for (int i = 0; i < numberOfServers; i++) {
                int port = startingPort + i;
                double phaseShift = i * Math.PI / (2 * gridSize); // Unterschiedliche Phasenverschiebungen für jeden Server
                LocateRegistry.createRegistry(port);
                MyRemoteService service = new MyRemoteServiceImpl(phaseShift);
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

                    services[i].updateNeighborPhases(neighborPhases);
                }
                // Synchronisierungsberechnung für jeden Server
                for (int i = 0; i < numberOfServers; i++) {
                    ((MyRemoteServiceImpl) services[i]).synchronizePhase();
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
