package es.unican.ps.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Card {

   
    @Id
    private String number;

    
    private int cvv;

    
    private String expiryDate;


    private String holder;

   
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
