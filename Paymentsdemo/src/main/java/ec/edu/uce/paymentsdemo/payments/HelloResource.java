package ec.edu.uce.paymentsdemo.payments;

import ec.edu.uce.paymentsdemo.classes.*;
import ec.edu.uce.paymentsdemo.jpa.Entities.*;
import ec.edu.uce.paymentsdemo.jpa.services.*;
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

    @Inject
    private ProductService productService;

    @Inject
    private PaymentMethodService paymentMethodService;

    @Inject
    private PaymentMethod paymentMethod;

    @Inject
    private PaymentService paymentService;

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

    @POST
    @Path("/Products/{name}/{description}/{price}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProduct(@PathParam("name")
                                  String name, @PathParam("description") String description,
                                  @PathParam("price") double price) {
        try{
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);

            productService.createProduct(product);
            return Response.status(Response.Status.CREATED).
                    entity("Product nuevo: " + product.getName()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/Products")
    public Response getAllProducts() {
        try{
            List<Product> products = productService.list();
            StringBuilder response = new StringBuilder();
            response.append("--------------Products List--------------\n\n");

            return Response.ok(response.toString()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Products: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/Products/{id}")
    public Response getProductsById(@PathParam("id") Long id) {
        try{
            Product product = productService.findById(id);
            StringBuilder response = new StringBuilder();
            response.append("--------------User id--------------\n\n");

            return Response.ok(response.toString() + product).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/Products/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProducts(@PathParam("id") Long id, Product product) {
        try{
            product.setId(id);
            productService.update(product);
            StringBuilder response = new StringBuilder();
            response.append("--------------User new id--------------\n\n");

            return Response.ok(response.toString() + product.getName()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/Payments/{user}/{metod}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPayment(
            @PathParam("user") Long userId,
            @PathParam("metod") Long paymentMethodId,
            List<Product> products
    ) {
        try {

            UserP user = userPService.findById(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuario no encontrado").build();
            }

            PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);
            if (paymentMethod == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Método de pago no encontrado").build();
            }

            PaymentDetail paymentDetail = new PaymentDetail();
            paymentDetail.setUser(user);
            paymentDetail.setPaymentMethod(paymentMethod);
            paymentDetail.setProducts(products);

            paymentDetail.calculateTotalAmount();

            paymentService.createPayment(paymentDetail); // paymentService debe estar inyectado

            return Response.status(Response.Status.CREATED)
                    .entity("Pago creado exitosamente para el usuario: " + user.getName()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el pago").build();
        }
    }


    @GET
    @Path("/Payments")
    public Response getAllPayments() {
        try{
            List<PaymentDetail> payments = paymentService.list();
            StringBuilder response = new StringBuilder();
            response.append("--------------Payments List--------------\n\n");

            return Response.ok(response.toString()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Payments: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/Payments/{id}")
    public Response getPaymentsById(@PathParam("id") Long id) {
        try{
            PaymentDetail payments = paymentService.findById(id);
            StringBuilder response = new StringBuilder();
            response.append("--------------User id--------------\n\n");

            return Response.ok(response.toString() + payments).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/Payments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePayments(@PathParam("id") Long id, PaymentDetail payments) {
        try{
            payments.setId(id);
            paymentService.update(payments);
            StringBuilder response = new StringBuilder();
            response.append("--------------User new id--------------\n\n");

            return Response.ok(response.toString() + payments.getUser()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Users: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/Methods")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPaymentMethods() {
        try{
            paymentMethod.setId("CreditCard");
            paymentMethod.setDescription("CreditCard transaction");
            paymentMethod.setDetails(null);

            paymentMethodService.createPaymentMethod(paymentMethod);

            paymentMethod.setId("PayPal");
            paymentMethod.setDescription("Paypal transaction");
            paymentMethod.setDetails(null);

            paymentMethodService.createPaymentMethod(paymentMethod);

            paymentMethod.setId("Transfer");
            paymentMethod.setDescription("Transfer transaction");
            paymentMethod.setDetails(null);

            paymentMethodService.createPaymentMethod(paymentMethod);

            return Response.status(Response.Status.CREATED).
                    entity("New Methods added: ").build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/Methods")
    public Response getAllMethods() {
        try{
            List<PaymentMethod> methods = paymentMethodService.list();
            StringBuilder response = new StringBuilder();
            response.append("--------------Payment Methods List--------------\n\n");

            return Response.ok(response.toString()).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving Methods: " + e.getMessage())
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
        userPService.createUserP(new UserP((long) 1L, "Alexis", "alexis22",
         "0960062255"));*/

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