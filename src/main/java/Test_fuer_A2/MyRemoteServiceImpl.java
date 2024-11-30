package Test_fuer_A2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MyRemoteServiceImpl extends UnicastRemoteObject implements MyRemoteService {
    private long startTime;
    private double phaseShift;
    private double phase;
    private double[] neighborPhases;

    protected MyRemoteServiceImpl(double phaseShift) throws RemoteException {
        super();
        this.startTime = System.currentTimeMillis();
        this.phaseShift = phaseShift;
        this.phase = 0;
        this.neighborPhases = new double[0];
    }

    @Override
    public double getPhaseValue(long currentTime) throws RemoteException {
        double elapsedTime = (currentTime - startTime) / 1000.0;
        this.phase = (elapsedTime + phaseShift) % (2 * Math.PI);
        return 0.5 * (1 + Math.sin(this.phase));
    }

    @Override
    public void updateNeighborPhases(double[] neighborPhases) throws RemoteException {
        this.neighborPhases = neighborPhases;
    }

    public void synchronizePhase() {
        double sumSin = 0.0;
        double sumCos = 0.0;
        for (double neighborPhase : neighborPhases) {
            sumSin += Math.sin(neighborPhase);
            sumCos += Math.cos(neighborPhase);
        }
        double averagePhase = Math.atan2(sumSin, sumCos);
        this.phase += 0.25 * Math.sin(averagePhase - this.phase); // Synchronisierungsstärke anpassen
    }
}
