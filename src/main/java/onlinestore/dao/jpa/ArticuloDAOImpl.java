package onlinestore.dao.jpa;

import onlinestore.dao.ArticuloDAO;
import onlinestore.factory.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import onlinestore.models.Articulo;

import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO {

    @Override
    public void insertar(Articulo articulo) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(articulo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Articulo buscarPorCodigo(String codigo) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Articulo.class, codigo);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Articulo> obtenerTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo a", Articulo.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(String codigo) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Articulo a = em.find(Articulo.class, codigo);
            if (a != null) em.remove(a);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}