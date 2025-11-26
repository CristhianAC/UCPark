package es.unican.ps.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Card {

    @Getter
    @Setter
    @Id
    private String number;

    @Getter
    @Setter
    private int cvv;

    @Getter
    @Setter
    private String expiryDate;

    @Getter
    @Setter
    private String holder;

    @Getter
    @Setter
    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    private User user;

    public Card(String number, int cvv, String expiryDate, String holder) {
        this.number = number;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.holder = holder;
    }

    public Card() {
    }

}
