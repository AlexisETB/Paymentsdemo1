package ec.edu.uce.paymentsdemo.payments;

import ec.edu.uce.paymentsdemo.classes.IPay;
import ec.edu.uce.paymentsdemo.classes.PaymentProcess;
import ec.edu.uce.paymentsdemo.classes.QualifierPay;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {

    @Inject
    PaymentProcess paymentProcessSingleton;

    @Inject
    @QualifierPay("CreditCard")
    IPay creditCardPayment;

    @Inject
    @QualifierPay("PayPal")
    IPay payPalPayment;

    @Inject
    @QualifierPay("Transfer")
    IPay transferPayment;

    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
    @GET
    @Produces("text/plain")
    @Path("/CreditCard")
    public String creditCardpay (){
        /*EntityManagerFactory emf = Persistence.createEntityManagerFactory("paymentUnit");
        EntityManager em = emf.createEntityManager();
        UserPService userPService = new UserPService(em);
        userPService.createUserP(new UserP((long) 1L, "Alexis", "alexis22", "0960062255"));*/
        return paymentProcessSingleton.processPayment(creditCardPayment);
    }

    @GET
    @Produces("text/plain")
    @Path("/PayPal")
    public String PayPalpay (){
        return paymentProcessSingleton.processPayment(payPalPayment);
    }



    @GET
    @Produces("text/plain")
    @Path("/Transfer")
    public String trasnferPay (){
        return paymentProcessSingleton.processPayment(transferPayment);
    }
}