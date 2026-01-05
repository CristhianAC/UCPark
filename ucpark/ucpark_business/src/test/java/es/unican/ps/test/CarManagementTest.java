package es.unican.ps.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.unican.ps.CarManagement;
import es.unican.ps.dao.IVehicleDao;
import es.unican.ps.entities.Parking;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;

/**
 * Unit tests for CarManagement.newParking() method
 * Tests business logic: initialization of parking state before persistence
 * 
 * Uses @InjectMocks to automatically inject mocks into @EJB annotated fields
 */
@ExtendWith(MockitoExtension.class)
public class CarManagementTest {

    @InjectMocks
    private CarManagement carManagement;

    @Mock
    private IVehicleDao vehicleDao;

    private User testUser;
    private Vehicle testVehicle;

    @BeforeEach
    public void setUp() {
        // @InjectMocks automatically creates carManagement instance and injects
        // vehicleDao
        // No need for manual reflection!

        // Create test data
        testUser = new User();
        testUser.setEmail("test@example.com");

        testVehicle = new Vehicle();
        testVehicle.setPlate("XYZ-999");
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Corolla");
    }

    /**
     * UT-BUS-01: Inicialización Correcta de Estacionamiento
     * Verifies that newParking correctly initializes a Parking object with:
     * - vehicle set to input vehicle
     * - minutes = 0
     * - amount = 0.0
     * - startTime = current time (approximately)
     * And that createParking is called and method returns true
     */
    @Test
    public void testNewParking_CorrectInitialization() {
        // Arrange
        Parking mockReturnedParking = new Parking();
        mockReturnedParking.setVehicle(testVehicle);
        when(vehicleDao.createParking(any(Parking.class))).thenReturn(mockReturnedParking);

        // Capture the Parking object passed to createParking
        ArgumentCaptor<Parking> parkingCaptor = ArgumentCaptor.forClass(Parking.class);

        // Act
        boolean result = carManagement.newParking(testUser, testVehicle);

        // Assert
        verify(vehicleDao, times(1)).createParking(parkingCaptor.capture());

        Parking capturedParking = parkingCaptor.getValue();
        assertNotNull(capturedParking, "Parking object should not be null");
        assertEquals(testVehicle, capturedParking.getVehicle(), "Vehicle should be set correctly");
        assertEquals(0, capturedParking.getMinutes(), "Minutes should be initialized to 0");
        assertEquals(0.0, capturedParking.getAmount(), 0.001, "Amount should be initialized to 0.0");
        assertNotNull(capturedParking.getStartTime(), "Start time should not be null");

        assertTrue(result, "Method should return true when parking is created successfully");
    }

    /**
     * UT-BUS-02: Fallo por Entradas Nulas
     * Verifies that method returns false and does NOT call createParking
     * when user or vehicle is null
     */
    @Test
    public void testNewParking_NullUser() {
        // Act
        boolean result = carManagement.newParking(null, testVehicle);

        // Assert
        assertFalse(result, "Method should return false when user is null");
        verify(vehicleDao, never()).createParking(any(Parking.class));
    }

    @Test
    public void testNewParking_NullVehicle() {
        // Act
        boolean result = carManagement.newParking(testUser, null);

        // Assert
        assertFalse(result, "Method should return false when vehicle is null");
        verify(vehicleDao, never()).createParking(any(Parking.class));
    }

    @Test
    public void testNewParking_BothNull() {
        // Act
        boolean result = carManagement.newParking(null, null);

        // Assert
        assertFalse(result, "Method should return false when both user and vehicle are null");
        verify(vehicleDao, never()).createParking(any(Parking.class));
    }

    /**
     * UT-BUS-03: Fallo en Persistencia DAO
     * Verifies that method returns false when DAO fails to persist
     * (createParking returns null)
     */
    @Test
    public void testNewParking_DaoFailure() {
        // Arrange
        when(vehicleDao.createParking(any(Parking.class))).thenReturn(null);

        // Act
        boolean result = carManagement.newParking(testUser, testVehicle);

        // Assert
        assertFalse(result, "Method should return false when DAO fails to persist");
        verify(vehicleDao, times(1)).createParking(any(Parking.class));
    }

    /**
     * UT-BUS-04: Verificación de Timestamp Correcto
     * Verifies that startTime is set to current time (within ±2 seconds)
     */
    @Test
    public void testNewParking_CorrectTimestamp() {
        // Arrange
        LocalDateTime beforeCall = LocalDateTime.now();

        Parking mockReturnedParking = new Parking();
        when(vehicleDao.createParking(any(Parking.class))).thenReturn(mockReturnedParking);

        ArgumentCaptor<Parking> parkingCaptor = ArgumentCaptor.forClass(Parking.class);

        // Act
        carManagement.newParking(testUser, testVehicle);

        LocalDateTime afterCall = LocalDateTime.now();

        // Assert
        verify(vehicleDao, times(1)).createParking(parkingCaptor.capture());

        Parking capturedParking = parkingCaptor.getValue();
        assertNotNull(capturedParking.getStartTime(), "Start time should not be null");

        LocalDateTime startTime = capturedParking.getStartTime();

        // Verify startTime is within ±2 seconds of current time
        long secondsFromBefore = ChronoUnit.SECONDS.between(beforeCall, startTime);
        long secondsFromAfter = ChronoUnit.SECONDS.between(startTime, afterCall);

        assertTrue(secondsFromBefore >= -2 && secondsFromBefore <= 2,
                "Start time should be within 2 seconds before the call");
        assertTrue(secondsFromAfter >= -2 && secondsFromAfter <= 2,
                "Start time should be within 2 seconds after the call");
    }

    /**
     * UT-BUS-05: Validación de Vehículo Ya Estacionado
     * Verifies that the system prevents creating a new parking
     * if the vehicle already has an active parking session
     */
    @Test
    public void testNewParking_VehicleAlreadyParked() {
        // Arrange
        // Create a vehicle with an active parking
        Vehicle vehicleWithParking = new Vehicle();
        vehicleWithParking.setPlate("ABC-123");
        vehicleWithParking.setBrand("Ford");
        vehicleWithParking.setModel("Focus");

        Parking activeParking = new Parking();
        activeParking.setVehicle(vehicleWithParking);
        activeParking.setStartTime(LocalDateTime.now());
        activeParking.setMinutes(30);
        activeParking.setAmount(5.0);

        // Set the active parking on the vehicle
        vehicleWithParking.setActiveParking(activeParking);

        // Act
        boolean result = carManagement.newParking(testUser, vehicleWithParking);

        // Assert
        assertFalse(result, "Method should return false when vehicle already has active parking");
        verify(vehicleDao, never()).createParking(any(Parking.class));
    }
}
