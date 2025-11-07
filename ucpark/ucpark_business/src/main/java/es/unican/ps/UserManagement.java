package es.unican.ps;

import java.util.List;

import es.unican.ps.business.IUserAnomManagement;
import es.unican.ps.business.IUserManagement;
import es.unican.ps.entities.Card;
import es.unican.ps.entities.Complaint;
import es.unican.ps.entities.User;

public class UserManagement implements IUserManagement, IUserAnomManagement {

    @Override
    public User registerUser(User user) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public boolean payComplaints(Card card, String plateNumber) {
        return false;
    }

    @Override
    public List<Complaint> getComplaints(String emailUser) {
        return null;
    }

}
