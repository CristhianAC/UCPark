import java.io.Serializable;
import java.util.List;

import es.unican.ps.business.ICarUserManagement;
import es.unican.ps.business.IUserAnomManagement;
import es.unican.ps.business.IUserManagement;
import es.unican.ps.entities.User;
import es.unican.ps.entities.Vehicle;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class BusquedasBean implements Serializable {

    @EJB
    private IUserManagement userManagement;
    @EJB
    private IUserAnomManagement userAnomManagement;
    @EJB
    private ICarUserManagement carUserManagement;
    
    @Getter 
    @Setter
    private User user;
    
    @Getter
    @Setter
    private String termino;
    
    @Getter
    @Setter
    private List<Vehicle> vehicles;
    
    @Getter
    @Setter
    private Vehicle selectedVehicle;
    
    @Getter
    @Setter
    private int minutes;

    public String doSearchUser() {
        this.user = userAnomManagement.getUserByEmail(termino);
        if (this.user != null) {
            this.vehicles = carUserManagement.getVehicles(user);
            return "newParking.xhtml?faces-redirect=true";
        }
        return null; // Stay on same page if user not found
    }
    
    public void selectVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
    }
    
    public String createParking() {
        if (user != null && selectedVehicle != null && minutes > 0) {
            boolean success = carUserManagement.newParking(user, selectedVehicle);
            if (success) {
                carUserManagement.increaseParkingTime(selectedVehicle, minutes);
                return "success.xhtml?faces-redirect=true"; 
            }
        }
        return null;
    }
    
    public void doSearchCar() {
        carUserManagement.getParkingInformations(user);
    }

}
