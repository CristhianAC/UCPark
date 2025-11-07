package es.unican.ps.dao;

import java.util.List;

import es.unican.ps.entities.User;

public interface IUserDao {

    /**
     * Deletes a user by ID.
     *
     * @param ID
     * @return
     */
    public User deleteUser(String ID);

    /**
     * Updates a user.
     *
     * @param user
     * @return
     */
    public User updateUser(User user);

    /**
     * Gets all users.
     *
     * @return
     */
    public List<User> getAllUsers();

    /**
     * Creates a new user.
     *
     * @param email
     * @param password
     * @return
     */
    public User createUser(String email, String password);

    /**
     * Gets a user by ID.
     *
     * @param ID
     * @return
     */
    public User getUserByID(String ID);

    /**
     * Gets a user by email.
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email);
}
