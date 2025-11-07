package es.unican.ps;

import es.unican.ps.business.ICarUserManagement;
import es.unican.ps.business.IPay;
import es.unican.ps.business.ITaxManagement;
import es.unican.ps.business.ITimer;
import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

public class CarManagement implements ITaxManagement, ICarUserManagement, ITimer, IPay {

    private IVehicleDao vehicleDao;

    public CarManagement() {
    }

    public CarManagement(IVehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    public void setVehicleDao(IVehicleDao vehicleDao) {
        this.vehicleDao = vehicleDao;
    }

    @Override
    public boolean registerVehicle(User user, Vehicle vehicle) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (user == null || vehicle == null) {
            return false;
        }
        Vehicle created = vehicleDao.createVehicle(user, vehicle);
        return created != null;
    }

    @Override
    public boolean deleteVehicle(User user, Vehicle vehicle) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (user == null || vehicle == null) {
            return false;
        }
        Vehicle deleted = vehicleDao.deleteVehicle(user, vehicle);
        return deleted != null;
    }

    @Override
    public boolean newParking(User user, Vehicle vehicle) {
        // Basic business logic: ensure vehicle exists (registered) before creating parking.
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (user == null || vehicle == null) {
            return false;
        }
        // Try to update vehicle to reflect parking start (if DAO supports it).
        // If update returns non-null, consider parking created. Real implementation
        // should involve a Parking DAO/entity with timestamps.
        Vehicle updated = vehicleDao.updateVehicle(user, vehicle);
        return updated != null;
    }

    @Override
    public Parking getParkingInformations(User user) {
        // No Parking DAO is provided in the current project; return null.
        // TODO: implement when a Parking repository/DAO is available.
        return null;
    }

    @Override
    public Parking increaseParkingTime(Vehicle vehicle, int minutes) {
        // Without a Parking DAO we cannot modify parking time. A possible
        // partial action is to update the vehicle (if it stores remaining time).
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (vehicle == null || minutes <= 0) {
            return null;
        }
        // Caller should provide the owner if update is required; we attempt
        // a best-effort update by leaving user null (some DAO implementations
        // may ignore the user parameter). This is a placeholder.
        try {
            vehicleDao.updateVehicle(null, vehicle);
            // No Parking object available to return
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean checkParking(String plateNumber) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (plateNumber == null || plateNumber.isEmpty()) {
            return false;
        }
        Vehicle v = vehicleDao.getVehicleByPlate(plateNumber);
        // TODO: Check actual payment status from a payment/parking record. For now
        // return true if vehicle is known in the system.
        return v != null;
    }

    @Override
    public boolean report(String plateNumber, Complaint complaint) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (plateNumber == null || plateNumber.isEmpty()) {
            return false;
        }
        Vehicle v = vehicleDao.getVehicleByPlate(plateNumber);

        vehicleDao.createComplaint(plateNumber, complaint);
        // that the vehicle was found and thus can be reported.
        return v != null;
    }

    @Override
    public boolean endParking(String plateNumber) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (plateNumber == null || plateNumber.isEmpty()) {
            return false;
        }
        Vehicle v = vehicleDao.getVehicleByPlate(plateNumber);
        vehicleDao.updateVehicle(null, v);
        // payment/parking DAO we can only return that the vehicle exists.
        return v != null;
    }

}
