package es.unican.ps.dao;

import java.util.List;

import es.unican.ps.entities.Parking;
import es.unican.ps.entities.Vehicle;

public interface IParkingDao {

    /**
     * Creates a new parking record.
     * 
     * @param parking
     * @return
     */
    public Parking createParking(Parking parking);

    /**
     * Updates an existing parking record.
     * 
     * @param parking
     * @return
     */
    public Parking updateParking(Parking parking);

    /**
     * Gets all parking records for a vehicle.
     * 
     * @param vehicle
     * @return
     */
    public List<Parking> getParkingsByVehicle(Vehicle vehicle);

}
