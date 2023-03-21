package Utilis;

public interface Observable {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyAllObs();
}
