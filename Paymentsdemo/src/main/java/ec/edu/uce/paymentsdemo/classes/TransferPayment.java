package ec.edu.uce.paymentsdemo.classes;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@QualifierPay("Transfer")
public class TransferPayment extends Payment  {

    public TransferPayment() {
        super();
    }
    @PostConstruct
    public void init() {
        System.out.println("TransferPayment init");
    }

    @Override
    public String pay(Payment payment) {
        return String.format("Transfer Pay " + '\'' +
                "detail = '" + payment.getDetail() + '\'' +
                ", amount = " + payment.getAmount());
    }

    @PreDestroy
    public void destroy() {
        System.out.println("TransferPayment Destroy");
    }
}