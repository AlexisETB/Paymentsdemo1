package ec.edu.uce.paymentsdemo.payments;

import ec.edu.uce.paymentsdemo.classes.IPay;
import ec.edu.uce.paymentsdemo.classes.PaymentProcess;
import ec.edu.uce.paymentsdemo.classes.QualifierPay;
import ec.edu.uce.paymentsdemo.jpa.Entities.UserP;
import ec.edu.uce.paymentsdemo.jpa.services.UserPService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pay")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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

    @Inject
    private UserPService userPService;

    @POST
    @Path("/Users/{name}/{email}/{phone}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("name")
    String name, @PathParam("email") String email, @PathParam("phone") String phone) {
        try{
            UserP userP = new UserP();
            userP.setName(name);
            userP.setEmail(email);
            userP.setPhone(phone);

            userPService.createUserP(userP);
            return Response.status(Response.Status.CREATED).
                    entity("Usuario nuevo: " + userP.getName()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/Users")
    public Response getAllUsers() {
        try{
            List<UserP> usersP = userPService.list();
            StringBuilder response = new StringBuilder();
            response.append("--------------Users List--------------\n\n");

            return Response.ok(response.toString()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/Users/{id}")
    public Response getUsersById(@PathParam("id") Long id) {
        try{
            UserP userP = userPService.findById(id);
            StringBuilder response = new StringBuilder();
            response.append("--------------User id--------------\n\n");

            return Response.ok(response.toString() + userP).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/Users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUsers(@PathParam("id") Long id, UserP userP) {
        try{
            userP.setId(id);
            userPService.update(userP);
            StringBuilder response = new StringBuilder();
            response.append("--------------User new id--------------\n\n");

            return Response.ok(response.toString() + userP.getName()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
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