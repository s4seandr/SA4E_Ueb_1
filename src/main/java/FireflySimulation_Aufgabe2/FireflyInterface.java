package FireflySimulation_Aufgabe2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FireflyInterface extends Remote {
    void updatePhase() throws RemoteException;
    void syncWithNeighbors() throws RemoteException;
    double getPhase() throws RemoteException;
    void setNeighbors(FireflyInterface[] neighbors) throws RemoteException;
    double getX() throws RemoteException;
    double getY() throws RemoteException;
    double getBrightness() throws RemoteException;
}
