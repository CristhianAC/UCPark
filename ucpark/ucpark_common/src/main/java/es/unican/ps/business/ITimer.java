package es.unican.ps.business;

public interface ITimer {

    /**
     * Starts parking time tracking for a vehicle with the given plate number.
     *
     * @param plateNumber The plate number of the vehicle.
     * @return true if the parking time tracking was started successfully, false
     * otherwise.
     */
    public boolean endParking(String plateNumber);
}
