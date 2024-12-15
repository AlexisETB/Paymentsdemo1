package ec.edu.uce.paymentsdemo.jpa.services;

import ec.edu.uce.paymentsdemo.jpa.Entities.PaymentDetail;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class PaymentService implements CrudService<PaymentDetail> {
    public PaymentService() {}

    @PersistenceContext
    private EntityManager em;

    public PaymentService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createPayment(PaymentDetail payment) {
        em.persist(payment);
    }

    @Override
    @Transactional
    public List<PaymentDetail> list() {
        try {
            return em.createQuery("FROM PaymentDetail", PaymentDetail.class).getResultList();
        }catch (Exception e) { throw new ServiceJdbcException(e.getMessage(), e.getCause());}
    }

    @Override
    @Transactional
    public PaymentDetail findById(Long id) {
        try {
            return em.find(PaymentDetail.class, id);
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void save(PaymentDetail payment) {
        try {
            if (payment.getId() != null && payment.getId() > 0) {
                em.merge(payment);
            } else {
                em.persist(payment);
            }
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            PaymentDetail payment = em.find(PaymentDetail.class, id);
            if (payment != null) {
                em.remove(payment);
            }
        }catch  (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void update(PaymentDetail payment) {
        try {
            if (payment.getId() == null || payment.getId() <= 0) {
                throw new IllegalArgumentException("El ID debe ser válido para actualizar");
            }
            em.merge(payment);
        }catch (Exception e) {
            throw new ServiceJdbcException("Error updating user: " + payment.getPaymentMethod(), e);
        }
    }
}
