package dao.jpa;

import dao.ClienteDAO;
import factory.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Cliente;
import models.ClienteEstandar;
import models.ClientePremium;

import java.math.BigDecimal;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public int insertar(models.Cliente cliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
            return cliente.getId();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public int insertarConSP(models.Cliente cliente, BigDecimal cuotaAnual, BigDecimal descuento) throws Exception {
        // Puedes implementar llamada a SP si lo deseas con em.createStoredProcedureQuery(...)
        return insertar(cliente);
    }

    @Override
    public models.Cliente buscarPorEmail(String email) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Cliente> q = em.createQuery("SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
            q.setParameter("email", email);
            return q.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<models.Cliente> listarTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<models.Cliente> listarEstandar() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = :t", Cliente.class)
                    .setParameter("t", ClienteEstandar.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<models.Cliente> listarPremium() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c WHERE TYPE(c) = :t", Cliente.class)
                    .setParameter("t", ClientePremium.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(models.Cliente cliente) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
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
            models.Cliente c = em.find(models.Cliente.class, id);
            if (c != null) em.remove(c);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}