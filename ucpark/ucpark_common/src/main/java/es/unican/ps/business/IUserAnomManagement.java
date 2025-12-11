package es.unican.ps.business;

import es.unican.ps.entities.User;

import es.unican.ps.entities.User;
import jakarta.ejb.Local;

@Local
public interface IUserAnomManagement {

    /**
     * Registers a new user.
     *
     * @param user The user to register.
     * @return The registered user.
     */
    public User registerUser(User user);

    /**
     * Retrieves a user by their email.
     *
     * @param email The email of the user.
     * @return The user with the specified email.
     */
    public User getUserByEmail(String email);

}
