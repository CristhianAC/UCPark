package es.unican.ps.dao.impl;

import java.util.List;

import es.unican.ps.dao.IUserDao;
import es.unican.ps.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

public class UserDao implements IUserDao {

    @PersistenceContext(unitName = "ucPark")
    private EntityManager em;

    @Override
    public User deleteUser(String ID) {
        User user = em.find(User.class, ID);
        if (user != null) {
            em.remove(user);
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        return em.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User createUser(String email, String password) {
        String id = String.valueOf(System.currentTimeMillis());
        User user = new User(id, email, password);
        em.persist(user);
        return user;
    }

    @Override
    public User getUserByID(String ID) {
        return em.find(User.class, ID);
    }

    @Override
    public User getUserByEmail(String email) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

}
