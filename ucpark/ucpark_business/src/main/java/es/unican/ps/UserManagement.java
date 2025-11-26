package es.unican.ps;

import java.util.List;

import es.unican.ps.business.IUserAnomManagement;
import es.unican.ps.business.IUserManagement;
import es.unican.ps.dao.IUserDao;
import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Card;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

public class UserManagement implements IUserManagement, IUserAnomManagement {

    private IUserDao userDao;
    private IVehicleDao vehicleDao;

    public UserManagement(IUserDao userDao, IVehicleDao vehicleDao) {
        this.userDao = userDao;
        this.vehicleDao = vehicleDao;
    }

    @Override
    public User registerUser(User user) {
        if (userDao.getUserByEmail(user.getEmail()) != null) {
            return null; // User already exists
        }
        return userDao.createUser(user.getEmail(), user.getPassword());
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public boolean payComplaints(Card card, String plateNumber) {
        Vehicle vehicle = vehicleDao.getVehicleByPlate(plateNumber);
        if (vehicle == null) {
            return false;
        }

        List<Complaint> complaints = vehicle.getComplaints();
        if (complaints == null || complaints.isEmpty()) {
            return false; // No complaints to pay
        }

        boolean paymentProcessed = false;
        for (Complaint complaint : complaints) {
            if (!complaint.isPaid()) {
                // Simulate payment processing
                // In a real scenario, we would call an external payment service here
                complaint.setPaid(true);
                paymentProcessed = true;
            }
        }

        if (paymentProcessed) {
            vehicleDao.updateVehicle(null, vehicle); // Update vehicle with paid complaints
        }

        return paymentProcessed;
    }

    @Override
    public List<Complaint> getComplaints(String emailUser) {
        User user = userDao.getUserByEmail(emailUser);
        if (user == null) {
            return null;
        }

        List<Vehicle> vehicles = vehicleDao.getVehiclesByOwner(user);
        if (vehicles == null) {
            return null;
        }

        List<Complaint> allComplaints = new java.util.ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getComplaints() != null) {
                allComplaints.addAll(vehicle.getComplaints());
            }
        }
        return allComplaints;
    }

}
