package es.unican.ps.dao.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.unican.ps.dao.impl.VehicleDao;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

/**
 * Unit tests for VehicleDao.createParking() method
 * Tests persistence logic and error handling
 * 
 * Uses @InjectMocks to automatically inject mocks into @PersistenceContext
 * fields
 */
@ExtendWith(MockitoExtension.class)
public class VehicleDaoTest {

    @InjectMocks
    private VehicleDao vehicleDao;

    @Mock
    private EntityManager em;

    private Parking testParking;
    private Vehicle testVehicle;

    @BeforeEach
    public void setUp() {
        // @InjectMocks automatically creates vehicleDao instance and injects em
        // No need for manual reflection!

        // Create test data
        testVehicle = new Vehicle();
        testVehicle.setPlate("ABC-1234");
        testVehicle.setBrand("Honda");
        testVehicle.setModel("Civic");

        testParking = new Parking();
        testParking.setVehicle(testVehicle);
        testParking.setStartTime(LocalDateTime.now());
        testParking.setMinutes(0);
        testParking.setAmount(0.0);
    }

    /**
     * UT-DAO-01: Persistencia Exitosa
     * Verifies that:
     * - EntityManager.persist() is called with the Parking object
     * - The method returns the same Parking object
     */
    @Test
    public void testCreateParking_SuccessfulPersistence() {
        // Arrange
        doNothing().when(em).persist(any(Parking.class));

        // Act
        Parking result = vehicleDao.createParking(testParking);

        // Assert
        verify(em, times(1)).persist(testParking);
        assertNotNull(result, "Method should return the parking object");
        assertEquals(testParking, result, "Should return the same parking object that was passed");
        assertEquals(testVehicle, result.getVehicle(), "Vehicle should be preserved");
    }

    /**
     * UT-DAO-02: Persistencia Fallida (Duplicado/Error)
     * Verifies that when persist() throws an exception,
     * the exception is propagated (no exception handling in current implementation)
     */
    @Test
    public void testCreateParking_PersistenceException() {
        // Arrange
        doThrow(new PersistenceException("Constraint violation"))
                .when(em).persist(any(Parking.class));

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            vehicleDao.createParking(testParking);
        }, "Should propagate PersistenceException when persist fails");

        verify(em, times(1)).persist(testParking);
    }

    /**
     * UT-DAO-03: Verificar Transaccionalidad
     * Note: Transactions are managed by the container (JTA) in production.
     * This test verifies that if persist() fails, the method doesn't continue
     * (exception is propagated)
     */
    @Test
    public void testCreateParking_TransactionBehavior() {
        // Arrange
        doThrow(new PersistenceException("Transaction error"))
                .when(em).persist(any(Parking.class));

        // Act & Assert
        try {
            vehicleDao.createParking(testParking);
            fail("Should have thrown PersistenceException");
        } catch (PersistenceException e) {
            // Expected behavior - exception is propagated
            // In production, container will handle rollback
            assertEquals("Transaction error", e.getMessage());
        }

        verify(em, times(1)).persist(testParking);
    }

    /**
     * UT-DAO-04: Parking con Datos Nulos Parciales
     * Verifies that when Parking has null vehicle, persist() throws exception
     */
    @Test
    public void testCreateParking_NullVehicle() {
        // Arrange
        Parking parkingWithNullVehicle = new Parking();
        parkingWithNullVehicle.setVehicle(null);
        parkingWithNullVehicle.setStartTime(LocalDateTime.now());
        parkingWithNullVehicle.setMinutes(0);
        parkingWithNullVehicle.setAmount(0.0);

        // Mock EntityManager to throw exception for null vehicle
        // (This simulates database constraint violation)
        doThrow(new PersistenceException("Null vehicle not allowed"))
                .when(em).persist(parkingWithNullVehicle);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            vehicleDao.createParking(parkingWithNullVehicle);
        }, "Should throw exception when vehicle is null");

        verify(em, times(1)).persist(parkingWithNullVehicle);
    }

    /**
     * UT-DAO-05: Verificar ID Autogenerado
     * Verifies that after persist(), the ID is set (simulated via mock)
     * In real JPA, the ID would be generated by the database
     */
    @Test
    public void testCreateParking_IdAutogeneration() {
        // Arrange
        Parking parkingWithoutId = new Parking();
        parkingWithoutId.setVehicle(testVehicle);
        parkingWithoutId.setStartTime(LocalDateTime.now());
        parkingWithoutId.setMinutes(60);
        parkingWithoutId.setAmount(10.0);

        // Initially, ID is null
        assertNull(parkingWithoutId.getId(), "ID should be null before persist");

        // Simulate ID generation by setting it when persist is called
        doAnswer(invocation -> {
            Parking parking = invocation.getArgument(0);
            // Simulate ID assignment by JPA/database
            parking.setId(123L);
            return null;
        }).when(em).persist(any(Parking.class));

        // Act
        Parking result = vehicleDao.createParking(parkingWithoutId);

        // Assert
        verify(em, times(1)).persist(parkingWithoutId);
        assertNotNull(result.getId(), "ID should be set after persist");
        assertEquals(123L, result.getId(), "ID should be the generated value");
    }

    /**
     * Additional test: Verify complete parking object persistence
     */
    @Test
    public void testCreateParking_CompleteObject() {
        // Arrange
        Parking completeParking = new Parking();
        completeParking.setVehicle(testVehicle);
        completeParking.setStartTime(LocalDateTime.of(2026, 1, 5, 14, 30));
        completeParking.setMinutes(120);
        completeParking.setAmount(20.0);

        ArgumentCaptor<Parking> captor = ArgumentCaptor.forClass(Parking.class);
        doNothing().when(em).persist(captor.capture());

        // Act
        Parking result = vehicleDao.createParking(completeParking);

        // Assert
        verify(em, times(1)).persist(any(Parking.class));

        Parking capturedParking = captor.getValue();
        assertEquals(testVehicle, capturedParking.getVehicle());
        assertEquals(120, capturedParking.getMinutes());
        assertEquals(20.0, capturedParking.getAmount(), 0.001);
        assertNotNull(capturedParking.getStartTime());

        assertEquals(completeParking, result);
    }
}
