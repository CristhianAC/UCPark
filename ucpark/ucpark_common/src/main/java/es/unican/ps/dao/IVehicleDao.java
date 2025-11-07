package es.unican.ps.dao;

import java.util.List;

import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

public interface IVehicleDao {

    /**
     * Creates a new vehicle for a user.
     *
     * @param user just the owner
     * @param vehicle the vehicle to create
     * @return
     */
    public Vehicle createVehicle(User user, Vehicle vehicle);

    /**
     * Deletes a vehicle for a user.
     *
     * @param user just the owner
     * @param vehicle the vehicle to delete
     * @return
     */
    public Vehicle deleteVehicle(User user, Vehicle vehicle);

    /**
     * Gets a vehicle by its plate number.
     *
     * @param plate the plate number of the vehicle
     * @return
     */
    public Vehicle getVehicleByPlate(String plate);

    /**
     * Updates a vehicle for a user.
     *
     * @param user just the owner
     * @param vehicle the vehicle to update
     * @return
     */
    public Vehicle updateVehicle(User user, Vehicle vehicle);

    /**
     * Gets all vehicles owned by a user.
     *
     * @param user just the owner
     * @return
     */
    public List<Vehicle> getVehiclesByOwner(User user);

    /**
     * Gets all vehicles in the system.
     *
     * @return
     */
    public List<Vehicle> getVehiclesAll();

    /**
     * Creates a complaint for a vehicle identified by its plate number.
     *
     * @param plateNumber The plate number of the vehicle.
     * @param complaint The complaint to be created.
     * @return The vehicle associated with the created complaint.
     */
    public Vehicle createComplaint(String plateNumber, Complaint complaint);

}
