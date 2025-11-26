
package es.unican.ps.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Complaint {
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
    private LocalDate date;

    @Getter
    @Setter
    private boolean paid;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private InfractionType type;

    @Getter
    @Setter
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private Vehicle vehicle;

    public Complaint(double amount, LocalDate date, boolean paid, InfractionType type) {
        this.amount = amount;
        this.date = date;
        this.paid = paid;
        this.type = type;
    }

    public Complaint() {
    }
}
