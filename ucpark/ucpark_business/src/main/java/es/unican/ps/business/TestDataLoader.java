package es.unican.ps.business;

import java.time.LocalDateTime;
import java.util.List;

import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

/**
 * Singleton EJB that loads test data on application startup.
 * Runs automatically on each deployment and checks for existing data to avoid
 * duplicates.
 */
@Singleton
@Startup
public class TestDataLoader {

    @PersistenceContext(unitName = "ucPark")
    private EntityManager em;

    @PostConstruct
    public void loadTestData() {
        System.out.println("TestDataLoader: Starting test data initialization...");

        // Check if test data already exists
        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.id LIKE 'test-%'",
                Long.class);
        Long existingTestUsers = countQuery.getSingleResult();

        if (existingTestUsers > 0) {
            System.out.println("TestDataLoader: Test data already exists. Skipping initialization.");
            return;
        }

        // Create test users
        User user1 = createUser("test-user1", "juan.perez@example.com", "password123");
        User user2 = createUser("test-user2", "maria.garcia@example.com", "password456");
        User user3 = createUser("test-user3", "carlos.lopez@example.com", "password789");

        // Create vehicles for user1
        Vehicle vehicle1 = createVehicle("ABC1234", "Toyota", "Corolla", user1);
        Vehicle vehicle2 = createVehicle("DEF5678", "Honda", "Civic", user1);

        // Create vehicles for user2
        Vehicle vehicle3 = createVehicle("GHI9012", "Ford", "Focus", user2);
        Vehicle vehicle4 = createVehicle("JKL3456", "Chevrolet", "Cruze", user2);
        Vehicle vehicle5 = createVehicle("MNO7890", "Nissan", "Sentra", user2);

        // Create vehicles for user3
        Vehicle vehicle6 = createVehicle("PQR1234", "Volkswagen", "Jetta", user3);
        Vehicle vehicle7 = createVehicle("STU5678", "Hyundai", "Elantra", user3);

        // Create parking records
        createParking(vehicle1, 5.0, 60, LocalDateTime.now().minusDays(2));
        createParking(vehicle1, 3.5, 45, LocalDateTime.now().minusDays(1));

        createParking(vehicle2, 7.0, 90, LocalDateTime.now().minusDays(3));

        createParking(vehicle3, 4.0, 50, LocalDateTime.now().minusDays(1));
        createParking(vehicle3, 6.0, 75, LocalDateTime.now().minusHours(5));

        createParking(vehicle4, 2.5, 30, LocalDateTime.now().minusDays(4));

        createParking(vehicle5, 8.0, 120, LocalDateTime.now().minusDays(2));
        createParking(vehicle5, 5.5, 70, LocalDateTime.now().minusHours(10));

        createParking(vehicle6, 4.5, 55, LocalDateTime.now().minusDays(1));

        createParking(vehicle7, 6.5, 80, LocalDateTime.now().minusDays(3));
        createParking(vehicle7, 3.0, 40, LocalDateTime.now().minusHours(8));

        System.out.println("TestDataLoader: Test data initialization completed successfully.");
        System.out.println("  - Created 3 users");
        System.out.println("  - Created 7 vehicles");
        System.out.println("  - Created 11 parking records");
    }

    private User createUser(String id, String email, String password) {
        User user = new User(id, email, password);
        em.persist(user);
        return user;
    }

    private Vehicle createVehicle(String plate, String brand, String model, User owner) {
        Vehicle vehicle = new Vehicle(plate, brand, model);
        vehicle.setOwner(owner);
        owner.getVehicles().add(vehicle);
        em.persist(vehicle);
        return vehicle;
    }

    private Parking createParking(Vehicle vehicle, double amount, int minutes, LocalDateTime startTime) {
        Parking parking = new Parking(amount, minutes, startTime);
        parking.setVehicle(vehicle);
        vehicle.getHistory().add(parking);
        em.persist(parking);
        return parking;
    }
}
