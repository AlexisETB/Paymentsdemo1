package ec.edu.uce.paymentsdemo.jpa.services;

import ec.edu.uce.paymentsdemo.jpa.Entities.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ProductService implements CrudService<Product> {
    public ProductService (){}

    @PersistenceContext
    private EntityManager em;

    public ProductService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createProduct(Product product) {
        em.persist(product);
    }

    @Override
    @Transactional
    public List<Product> list() {
        try {
            return em.createQuery("FROM Product", Product.class).getResultList();
        }catch (Exception e) { throw new ServiceJdbcException(e.getMessage(), e.getCause());}
    }

    @Override
    @Transactional
    public Product findById(Long id) {
        try {
            return em.find(Product.class, id);
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void save(Product product) {
        try {
            if (product.getId() != null && product.getId() > 0) {
                em.merge(product);
            } else {
                em.persist(product);
            }
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
            }
        }catch  (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void update(Product product) {
        try {
            if (product.getId() == null || product.getId() <= 0) {
                throw new IllegalArgumentException("El ID debe ser válido para actualizar");
            }
            em.merge(product);
        }catch (Exception e) {
            throw new ServiceJdbcException("Error updating user: " + product.getName(), e);
        }
    }
}
