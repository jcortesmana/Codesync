package dao.jpa;

import dao.PedidoDAO;
import factory.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import models.Pedido;
import models.PedidoLinea;

import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    // Insert using JPA entities (cascading persists the lines)
    public Pedido insertarEntidad(Pedido pedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (pedido.getCliente() != null && (pedido.getCliente().getId() == 0)) {
                em.persist(pedido.getCliente());
            } else if (pedido.getCliente() != null) {
                pedido.setTienda(null); // evitar serialización accidental
                pedido = em.merge(pedido);
            }
            em.persist(pedido);
            tx.commit();
            return pedido;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // Implement interface method insertarPedidoConLineas by delegating:
    @Override
    public String insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception {
        // attach lineas to pedido entity
        for (PedidoLinea pl : lineas) {
            pl.setPedido(pedido);
            pedido.getLineas().add(pl);
        }
        Pedido p = insertarEntidad(pedido);
        return p.getNumero() == null ? null : String.valueOf(p.getNumero());
    }

    @Override
    public Pedido buscarPorNumero(String numero) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido p WHERE p.numero = :num", Pedido.class);
            q.setParameter("num", Integer.valueOf(numero));
            return q.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Pedido> listarTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido p", Pedido.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizar(Pedido pedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(pedido);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminar(String numero) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Pedido p = em.find(Pedido.class, Integer.valueOf(numero));
            if (p != null) em.remove(p);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // listarPedidosDetallados / listarPedidosPorEmail podrías implementarlas con JPQL; dejamos sin implementación simple:
    @Override
    public List<String[]> listarPedidosDetallados() throws Exception {
        throw new UnsupportedOperationException("Implementar JPQL según formato requerido");
    }

    @Override
    public List<String[]> listarPedidosPorEmail(String email) throws Exception {
        throw new UnsupportedOperationException("Implementar JPQL según formato requerido");
    }
}