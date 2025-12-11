package es.unican.ps.business;

import java.util.List;

import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

import es.unican.ps.entities.Vehicle;
import jakarta.ejb.Local;

@Local
public interface ICarUserManagement {

    /**
     * Registers a vehicle for a user.
     *
     * @param user
     * @param vehicle
     * @return
     */
    public boolean registerVehicle(User user, Vehicle vehicle);

    /**
     * Deletes a vehicle for a user.
     *
     * @param user
     * @param vehicle
     * @return
     */
    public boolean deleteVehicle(User user, Vehicle vehicle);

    /**
     * Registers a new parking for a user and vehicle.
     *
     * @param user
     * @param vehicle
     * @return
     */
    public boolean newParking(User user, Vehicle vehicle);

    /**
     * Gets parking information for a user.
     *
     * @param user
     * @return
     */
    public Parking getParkingInformations(User user);

    /**
     * Increases parking time for a vehicle.
     *
     * @param vehicle
     * @param minutes
     * @return
     */
    public Parking increaseParkingTime(Vehicle vehicle, int minutes);

    /**
     * Retrieves the vehicles owned by a user.
     *
     * @param user The user whose vehicles are to be retrieved.
     * @return A list of vehicles owned by the user.
     */
    public List<Vehicle> getVehicles(User user);

}
