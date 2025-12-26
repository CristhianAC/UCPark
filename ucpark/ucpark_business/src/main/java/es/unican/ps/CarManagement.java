package es.unican.ps;

import java.util.List;

import es.unican.ps.business.ICarUserManagement;
import es.unican.ps.business.IPay;
import es.unican.ps.business.ITaxManagement;
import es.unican.ps.business.ITimer;
import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.dao.IParkingDao;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class CarManagement implements ITaxManagement, ICarUserManagement, ITimer, IPay {
    @EJB
    private IVehicleDao vehicleDao;

    @EJB
    private IParkingDao parkingDao;

    public CarManagement() {
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
        if (vehicleDao == null || parkingDao == null) {
            throw new IllegalStateException("DAOs not set");
        }
        if (user == null || vehicle == null) {
            return false;
        }

        Parking parking = new Parking();
        parking.setVehicle(vehicle);
        parking.setStartTime(java.time.LocalDateTime.now());
        parking.setMinutes(0);
        parking.setAmount(0.0);

        return parkingDao.createParking(parking) != null;
    }

    @Override
    public Parking getParkingInformations(User user) {
        // This method signature is weird for finding current parking (User vs Vehicle)
        // But fulfilling interface...
        // Assuming we need to find active parking for one of the user's vehicles?
        // Or maybe just the last one created by user?
        // For now, let's leave this as null or implement if strict requirement.
        // The issue was about CREATE not working.
        return null;
    }

    @Override
    public Parking increaseParkingTime(Vehicle vehicle, int minutes) {
        if (parkingDao == null) {
            throw new IllegalStateException("parkingDao not set");
        }
        if (vehicle == null || minutes <= 0) {
            return null;
        }

        List<Parking> parkings = parkingDao.getParkingsByVehicle(vehicle);
        if (parkings != null && !parkings.isEmpty()) {
            Parking current = parkings.get(0);
            current.setMinutes(current.getMinutes() + minutes);
            // Optional: Calculate amount logic here if needed
            return parkingDao.updateParking(current);
        }
        return null;
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

    @Override
    public List<Vehicle> getVehicles(User user) {
        if (vehicleDao == null) {
            throw new IllegalStateException("vehicleDao not set");
        }
        if (user == null) {
            return null;
        }
        return vehicleDao.getVehiclesByOwner(user);
    }

}
