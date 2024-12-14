package ec.edu.uce.paymentsdemo.classes;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@QualifierPay("CreditCard")
public class CreditCardPayment extends Payment {

    public CreditCardPayment() {
        super();
    }

    @PostConstruct
    public void init() {
        System.out.println("CreditCardPayment init");
    }

    @Override
    public String pay(Payment payment) {
        return String.format("CreditCard Pay " + '\'' +
                "detail = '" + payment.getDetail() + '\'' +
                ", amount = " + payment.getAmount());
    }

    @PreDestroy
    public void destroy() {
        System.out.println("CreditCardPayment Destroy");
    }
}
