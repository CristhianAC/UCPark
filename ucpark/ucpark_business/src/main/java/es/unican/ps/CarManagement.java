package es.unican.ps;

import es.unican.ps.business.ICarUserManagement;
import es.unican.ps.business.IPay;
import es.unican.ps.business.ITaxManagement;
import es.unican.ps.business.ITimer;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

public class CarManagement implements ITaxManagement, ICarUserManagement, ITimer, IPay {

    // NOTE: The entity classes in `ucpark_common` are currently empty (no fields).
    // These implementations are basic placeholders so the project compiles and
    // can be extended later with real business logic once entities are fleshed out.
    @Override
    public boolean registerVehicle(User user, Vehicle vehicle) {
        // TODO: store the vehicle for the user in a persistent repository
        System.out.println("registerVehicle called (placeholder)");
        return true;
    }

    @Override
    public boolean deleteVehicle(User user, Vehicle vehicle) {
        // TODO: remove the vehicle from the user's list
        System.out.println("deleteVehicle called (placeholder)");
        return true;
    }

    @Override
    public boolean newParking(User user, Vehicle vehicle) {
        // TODO: create a new Parking instance, persist and start timer
        System.out.println("newParking called (placeholder)");
        return true;
    }

    @Override
    public Parking getParkingInformations(User user) {
        // TODO: return the Parking associated with the user
        System.out.println("getParkingInformations called (placeholder)");
        return null;
    }

    @Override
    public Parking increaseParkingTime(Vehicle vehicle, int minutes) {
        // TODO: find parking for vehicle and increase time
        System.out.println("increaseParkingTime called (placeholder) - minutes: " + minutes);
        return null;
    }

    @Override
    public boolean checkParking(String plateNumber) {
        // TODO: check if plateNumber has paid
        System.out.println("checkParking called (placeholder) - plate: " + plateNumber);
        return false;
    }

    @Override
    public boolean report(String plateNumber) {
        // TODO: report vehicle for not having paid
        System.out.println("report called (placeholder) - plate: " + plateNumber);
        return false;
    }

    @Override
    public boolean endParking(String plateNumber) {
        // TODO: end parking timer, compute charge and persist payment state
        System.out.println("endParking called (placeholder) - plate: " + plateNumber);
        return false;
    }

}
