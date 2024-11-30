package FireflySimulation_Aufgabe2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MyRemoteServiceImpl extends UnicastRemoteObject implements MyRemoteService {
    private double phase;
    private double[] neighborPhases;
    private static final double COUPLING_STRENGTH = 0.05; // Synchronisierungsstärke anpassen
    private static final double TIME_STEP = 0.1; // Zeitschritt für die Phase aktualisierung
    private static double period = 2.0;  // Erhöhte Periode für langsamere Synchronisation

    protected MyRemoteServiceImpl() throws RemoteException {
        super();
        this.phase = Math.random() * 2 * Math.PI;
        this.neighborPhases = new double[0];
    }

    @Override
    public double getPhaseValue(long currentTime) throws RemoteException {
        return 0.5 * (1 + Math.sin(this.phase));
    }

    @Override
    public void updateNeighborPhases(double[] neighborPhases) throws RemoteException {
        this.neighborPhases = neighborPhases;
        syncWithNeighbors();
    }

    @Override
    public void updatePhase() {
        phase += (2 * Math.PI / period) * TIME_STEP;
        if (phase >= 2 * Math.PI) {
            phase -= 2 * Math.PI;
        }
    }

    private void syncWithNeighbors() {
        for (double neighborPhase : neighborPhases) {
            double phaseDifference = neighborPhase - this.phase;
            this.phase += COUPLING_STRENGTH * Math.sin(phaseDifference);
        }
    }
}

