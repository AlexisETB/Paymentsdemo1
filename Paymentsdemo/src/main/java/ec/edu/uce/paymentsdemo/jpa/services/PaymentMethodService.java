package ec.edu.uce.paymentsdemo.jpa.services;
import ec.edu.uce.paymentsdemo.jpa.Entities.PaymentMethod;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PaymentMethodService implements CrudService<PaymentMethod> {

    public PaymentMethodService() {}

    @PersistenceContext
    private EntityManager em;

    public PaymentMethodService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createPaymentMethod(PaymentMethod paymentMethod) {
        em.persist(paymentMethod);
    }

    @Override
    @Transactional
    public List<PaymentMethod> list() {
        try {
            return em.createQuery("FROM PaymentMethod", PaymentMethod.class).getResultList();
        }catch (Exception e) { throw new ServiceJdbcException(e.getMessage(), e.getCause());}
    }

    @Override
    @Transactional
    public PaymentMethod findById(Long id) {
        try {
            return em.find(PaymentMethod.class, id);
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Transactional
    public PaymentMethod findByIdS(String id) {
        try {
            return em.find(PaymentMethod.class, id);
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void save(PaymentMethod paymentMethod) {
        try {
            if (paymentMethod.getId() != null) {
                em.merge(paymentMethod);
            } else {
                em.persist(paymentMethod);
            }
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            PaymentMethod paymentMethod = em.find(PaymentMethod.class, id);
            if (paymentMethod != null) {
                em.remove(paymentMethod);
            }
        }catch  (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void update(PaymentMethod paymentMethod) {
        try {
            if (paymentMethod.getId() == null) {
                throw new IllegalArgumentException("El ID debe ser válido para actualizar");
            }
            em.merge(paymentMethod);
        }catch (Exception e) {
            throw new ServiceJdbcException("Error updating user: " + paymentMethod.getId(), e);
        }
    }
}
