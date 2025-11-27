package es.unican.ps;

import java.time.LocalDateTime;
import java.util.List;

import es.unican.ps.business.ITimer;
import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.Vehicle;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup; 
import jakarta.inject.Inject; 

@Singleton
@Startup
public class TimerManagement implements ITimer {

    @Inject
    private IVehicleDao vehicleDao;

    public TimerManagement() {
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
        if (v != null) {
            vehicleDao.updateVehicle(null, v);
            return true;
        }
        return false;
    }

    @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    public void checkParkings() {
        if (vehicleDao == null) {
            return;
        }
        List<Vehicle> vehicles = vehicleDao.getVehiclesAll();
        if (vehicles == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        for (Vehicle vehicle : vehicles) {
            Parking activeParking = vehicle.getActiveParking();
            if (activeParking != null) {
                LocalDateTime startTime = activeParking.getStartTime();
                int durationMinutes = activeParking.getMinutes();
                LocalDateTime expirationTime = startTime.plusMinutes(durationMinutes);

                if (now.isAfter(expirationTime)) {
                   vehicle.getHistory().add(activeParking);
                   vehicle.setActiveParking(null);
                   vehicleDao.updateVehicle(null, vehicle);
                }
            }
        }
    }
}
