package es.unican.ps.dao.impl;

import java.util.List;

import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class VehicleDao implements IVehicleDao {

    @PersistenceContext(unitName = "ucPark")
    private EntityManager em;

    @Override
    public Vehicle createVehicle(User user, Vehicle vehicle) {
        if (em.find(Vehicle.class, vehicle.getPlate()) != null) {
            return null; // Vehicle already exists
        }
        vehicle.setOwner(user);
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
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v WHERE v.owner = :user", Vehicle.class);
        query.setParameter("user", user);
        return query.getResultList();
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

    @Override
    public Parking createParking(Parking parking) {
        em.persist(parking);
        return parking;
    }

    @Override
    public Parking updateParking(Parking parking) {
        return em.merge(parking);
    }

    @Override
    public List<Parking> getParkingsByVehicle(Vehicle vehicle) {
        TypedQuery<Parking> query = em.createQuery(
                "SELECT p FROM Parking p WHERE p.vehicle = :vehicle ORDER BY p.startTime DESC", Parking.class);
        query.setParameter("vehicle", vehicle);
        return query.getResultList();
    }

}
