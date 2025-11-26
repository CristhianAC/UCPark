package es.unican.ps.entities;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "Users")
public class User {

    @Getter
    @Setter
    @Id
    private String id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Vehicle> vehicles;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Card> cards;

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.vehicles = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    public User() {
        this.vehicles = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

}
