package es.unican.ps.entities;

import java.time.LocalDateTime;
import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Parking implements Serializable {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private int minutes;

    @Getter
    @Setter
    private LocalDateTime startTime;
    @Getter
    @Setter
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private Vehicle vehicle;

    public Parking(double amount, int minutes, LocalDateTime startTime) {
        this.amount = amount;
        this.minutes = minutes;
        this.startTime = startTime;
    }

    public Parking() {
    }

}
