package es.unican.ps.entities;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Vehicle {

    @Getter
    @Setter
    @Id
    private String plate;

    @Getter
    @Setter
    private String brand;

    @Getter
    @Setter
    private String model;

    @Getter
    @Setter
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private User owner;

    @Getter
    @Setter
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Complaint> complaints;

    @Getter
    @Setter
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private List<Parking> history;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = jakarta.persistence.FetchType.LAZY)
    private Parking activeParking;

    public Vehicle(String plate, String brand, String model) {
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.complaints = new ArrayList<>();
        this.history = new ArrayList<>();
    }

    public Vehicle() {
        this.complaints = new ArrayList<>();
        this.history = new ArrayList<>();
    }

}
