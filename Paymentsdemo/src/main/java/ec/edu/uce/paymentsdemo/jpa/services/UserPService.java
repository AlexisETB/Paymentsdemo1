package ec.edu.uce.paymentsdemo.jpa.services;

import ec.edu.uce.paymentsdemo.jpa.Entities.UserP;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;


public class UserPService implements CrudService<UserP> {

    @PersistenceContext
    private EntityManager em;

    public UserPService(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void createUserP(UserP userP) {
        try {
            this.em.persist(userP);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    @Override
    @Transactional
    public List<UserP> list() {
        try {
            return em.createQuery("FROM UserP", UserP.class).getResultList();
        }catch (Exception e) { throw new ServiceJdbcException(e.getMessage(), e.getCause());}
    }

    @Override
    @Transactional
    public UserP findById(Long id) {
        try {
            return em.find(UserP.class, id);
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void save(UserP user) {
        try {
            if (user.getId() != null && user.getId() > 0) {
                em.merge(user);
            } else {
                em.persist(user);
            }
        }catch (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            UserP user = em.find(UserP.class, id);
            if (user != null) {
                em.remove(user);
            }
        }catch  (Exception e) {
            throw new ServiceJdbcException(e.getMessage(), e.getCause());
        }
    }

    @Override
    @Transactional
    public void update(UserP user) {
        try {
            if (user.getId() == null || user.getId() <= 0) {
                throw new IllegalArgumentException("El ID debe ser válido para actualizar");
            }
            em.merge(user);
        }catch (Exception e) {
            throw new ServiceJdbcException("Error updating user: " + user.getName(), e);
        }
    }
}
