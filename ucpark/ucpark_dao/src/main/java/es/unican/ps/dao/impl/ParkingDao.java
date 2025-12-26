package es.unican.ps.dao.impl;

import java.util.List;

import es.unican.ps.dao.IParkingDao;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.Vehicle;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Stateless
public class ParkingDao implements IParkingDao {

    @PersistenceContext(unitName = "ucPark")
    private EntityManager em;

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
