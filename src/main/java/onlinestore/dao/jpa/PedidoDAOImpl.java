package onlinestore.dao.jpa;

import onlinestore.dao.PedidoDAO;
import onlinestore.factory.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import onlinestore.models.Pedido;
import onlinestore.models.PedidoLinea;

import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    // ====================================================
    // INSERTAR PEDIDO COMPLETO CON LINEAS
    // ====================================================
    @Override
    public String insertarPedidoConLineas(Pedido pedido, List<PedidoLinea> lineas) throws Exception {

        // NO modificar la lista original → se asigna solo la relación
        for (PedidoLinea pl : lineas) {
            pl.setPedido(pedido);
        }

        // La persistencia con cascada guarda las líneas
        Pedido p = insertarEntidad(pedido);

        return (p.getNumero() == null) ? null : String.valueOf(p.getNumero());
    }


    // ====================================================
    // MÉTODO PRIVADO: SOLO PERSISTE EL PEDIDO (JPA + CASCADE)
    // ====================================================
    public Pedido insertarEntidad(Pedido pedido) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
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


    // ====================================================
    // BUSCAR POR NÚMERO
    // ====================================================
    @Override
    public Pedido buscarPorNumero(String numero) throws Exception {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            TypedQuery<Pedido> q = em.createQuery(
                    "SELECT DISTINCT p FROM Pedido p " +
                    "LEFT JOIN FETCH p.lineas l " +
                    "LEFT JOIN FETCH l.articulo " +
                    "LEFT JOIN FETCH p.cliente " +
                    "WHERE p.numero = :num", Pedido.class
            );

            q.setParameter("num", Integer.valueOf(numero));
            return q.getResultStream().findFirst().orElse(null);

        } finally {
            em.close();
        }
    }


    // ====================================================
    // LISTAR TODOS
    // ====================================================
    @Override
    public List<Pedido> listarTodos() throws Exception {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT p FROM Pedido p " +
                    "LEFT JOIN FETCH p.lineas l " +
                    "LEFT JOIN FETCH l.articulo " +
                    "LEFT JOIN FETCH p.cliente",
                    Pedido.class
            ).getResultList();

        } finally {
            em.close();
        }
    }


    // ====================================================
    // ACTUALIZAR
    // ====================================================
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


    // ====================================================
    // ELIMINAR
    // ====================================================
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


    // ====================================================
    // LISTAR POR EMAIL
    // ====================================================
    @Override
    public List<String[]> listarPedidosPorEmail(String email) throws Exception {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            List<Pedido> pedidos = em.createQuery(
                    "SELECT DISTINCT p FROM Pedido p " +
                            "LEFT JOIN FETCH p.lineas l " +
                            "LEFT JOIN FETCH l.articulo " +
                            "LEFT JOIN FETCH p.cliente " +
                            "WHERE p.cliente.email = :email", Pedido.class)
                    .setParameter("email", email)
                    .getResultList();

            return pedidos.stream()
                    .map(p -> new String[]{
                            String.valueOf(p.getNumero()),
                            p.getCliente().getEmail(),
                            String.valueOf(p.calcularTotal()),
                            p.getFecha().toString()
                    })
                    .toList();

        } finally {
            em.close();
        }
    }


    @Override
    public List<String[]> listarPedidosDetallados() throws Exception {
        throw new UnsupportedOperationException("No es necesario con JPA correctamente mapeado.");
    }
}
