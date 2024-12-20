package FireflySimulation_Aufgabe2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MyRemoteService extends Remote {
    double getPhaseValue(long currentTime) throws RemoteException;
    void updateNeighborPhases(double[] neighborPhases) throws RemoteException;
    void updatePhase() throws RemoteException;
}
