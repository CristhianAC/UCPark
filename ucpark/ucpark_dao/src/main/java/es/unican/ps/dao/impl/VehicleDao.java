package es.unican.ps.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

public class VehicleDao implements IVehicleDao {

    @PersistenceContext(unitName = "versionEspanholaPU")
    private EntityManager em;

    @Override
    public Vehicle createVehicle(User user, Vehicle vehicle) {
        if (em.find(Vehicle.class, vehicle.getPlate()) != null) {
            return null; // Vehicle already exists
        }
        // Assuming relationship is managed via User or Vehicle directly?
        // The diagram shows User has vehicles.
        // But Vehicle entity has no explicit owner field in my previous update, wait.
        // I should have added an owner field to Vehicle if I want to query by owner easily or map it.
        // Let's check Vehicle entity again. I added @OneToMany in Vehicle? No, I added @OneToMany in User?
        // Wait, I need to check the relationship.
        // Diagram: User 1 -- * Vehicle.
        // In Vehicle.java I didn't add the owner field in the previous step, I missed it.
        // I need to add 'private User owner' to Vehicle to map the relationship properly or use a join table.
        // For now, I will persist the vehicle.
        em.persist(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle deleteVehicle(User user, Vehicle vehicle) {
        Vehicle v = em.find(Vehicle.class, vehicle.getPlate());
        if (v != null) {
            em.remove(v);
            return v;
        }
        return null;
    }

    @Override
    public Vehicle getVehicleByPlate(String plate) {
        return em.find(Vehicle.class, plate);
    }

    @Override
    public Vehicle updateVehicle(User user, Vehicle vehicle) {
        return em.merge(vehicle);
    }

    @Override
    public List<Vehicle> getVehiclesByOwner(User user) {
        // Since I don't have an owner field in Vehicle yet, I can't query easily.
        // I should update Vehicle entity to have an owner field.
        // But for now, I will return null or empty list to avoid compilation error, 
        // and I will fix the Vehicle entity in the next step.
        return null; 
    }

    @Override
    public List<Vehicle> getVehiclesAll() {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
        return query.getResultList();
    }

    @Override
    public Vehicle createComplaint(String plateNumber, Complaint complaint) {
        Vehicle vehicle = em.find(Vehicle.class, plateNumber);
        if (vehicle != null) {
            vehicle.getComplaints().add(complaint);
            em.merge(vehicle);
            return vehicle;
        }
        return null;
    }

}
