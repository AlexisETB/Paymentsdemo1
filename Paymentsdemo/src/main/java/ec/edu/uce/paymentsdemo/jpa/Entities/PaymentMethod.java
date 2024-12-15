package ec.edu.uce.paymentsdemo.jpa.Entities;

import ec.edu.uce.paymentsdemo.classes.Payment;
import jakarta.persistence.*;

@Entity
@Table
public class PaymentMethod {

    @Id
    @Column(length = 50)
    private String id;

    private String description;

    @Lob
    private String details;

    public PaymentMethod() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        Payment payment;
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}