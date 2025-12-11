package es.unican.ps.business;

import java.util.List;

import es.unican.ps.entities.Card;
import es.unican.ps.entities.Complaint;
import jakarta.ejb.Local;

@Local

public interface IUserManagement {

    /**
     * Pays the complaints associated with a vehicle identified by its plate
     * number using the provided card.
     *
     * @param card        The card used for payment.
     * @param plateNumber The plate number of the vehicle whose complaints are
     *                    to be paid.
     * @return true if the payment was successful, false otherwise.
     */
    public boolean payComplaints(Card card, String plateNumber);

    /**
     * Retrieves the list of complaints associated with a vehicle identified by
     * its plate number.
     *
     * @param plateNumber The plate number of the vehicle whose complaints are
     *                    to be retrieved.
     * @return A list of complaints associated with the vehicle.
     */
    public List<Complaint> getComplaints(String emailUser);

}
