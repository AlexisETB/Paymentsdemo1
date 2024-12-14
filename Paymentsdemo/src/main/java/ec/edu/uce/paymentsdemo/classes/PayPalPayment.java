package ec.edu.uce.paymentsdemo.classes;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@QualifierPay("PayPal")
public class PayPalPayment extends Payment {

    public PayPalPayment() {
        super();
    }

    @PostConstruct
    public void init() {
        System.out.println("PayPalPayment init");
    }

    @Override
    public String pay(Payment payment) {
        return String.format("PayPal Pay " + '\'' +
                "detail = '" + payment.getDetail() + '\'' +
                ", amount = " + payment.getAmount());
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PayPalPayment Destroy");
    }
}
