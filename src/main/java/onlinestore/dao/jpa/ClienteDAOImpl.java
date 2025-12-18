package onlinestore.dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import onlinestore.dao.ClienteDAO;
import onlinestore.factory.JPAUtil;
import onlinestore.models.Cliente;
import java.math.BigDecimal;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public int insertar(Cliente cliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
            return cliente.getId(); // devuelve el id generado
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente buscarPorEmail(String email) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery(
                    "SELECT c FROM Cliente c WHERE c.email = :email",
                    Cliente.class
            );
            q.setParameter("email", email);
            return q.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Cliente c",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Cliente cliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Cliente c = em.find(Cliente.class, id);
            if (c != null) {
                em.remove(c);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarEstandar() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM ClienteEstandar c",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarPremium() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM ClientePremium c",
                    Cliente.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int insertarConSP(Cliente cliente,
                             BigDecimal cuotaAnual,
                             BigDecimal descuento) throws Exception {
        // En JPA no se usan procedimientos almacenados
        // Se implementa para cumplir la interfaz
        throw new UnsupportedOperationException(
                "insertarConSP no está soportado en la implementación JPA");
    }
}