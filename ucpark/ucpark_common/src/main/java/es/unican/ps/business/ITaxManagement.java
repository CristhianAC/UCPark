package es.unican.ps.business;

import es.unican.ps.entities.Complaint;

public interface ITaxManagement {

    /**
     * Checks if a vehicle with the given plate number has paid the parking tax.
     *
     * @param plateNumber The plate number of the vehicle to check.
     * @return true if the vehicle has paid the parking tax, false otherwise.
     */
    public boolean checkParking(String plateNumber);

    /**
     * Reports a vehicle with the given plate number for not having paid the
     * parking tax.
     *
     * @param plateNumber The plate number of the vehicle to report.
     * @param complaint The complaint details.
     * @return true if the report was successful, false otherwise.
     */
    public boolean report(String plateNumber, Complaint complaint);
}
