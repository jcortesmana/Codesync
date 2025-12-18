package onlinestore.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import onlinestore.dao.PedidoDAO;
import onlinestore.factory.DAOFactory;
import onlinestore.models.Pedido;

import java.util.List;

public class PedidoView {

    private final Stage stage;
    private final PedidoDAO pedidoDAO;
    private final TableView<Pedido> tabla;

    public PedidoView(Stage stage) {
        this.stage = stage;
        this.pedidoDAO = DAOFactory.getPedidoDAO();
        this.tabla = new TableView<>();
    }

    public void mostrar() {

        Label titulo = new Label("Gestión de Pedidos");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        configurarTabla();
        cargarPedidos();

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> {
            TiendaView tiendaView = new TiendaView(stage);
            tiendaView.mostrar();
        });

        HBox botones = new HBox(btnVolver);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.CENTER);
        BorderPane.setMargin(titulo, new Insets(10));

        root.setCenter(tabla);
        root.setBottom(botones);

        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void configurarTabla() {

        TableColumn<Pedido, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Pedido, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        TableColumn<Pedido, Boolean> colEnviado = new TableColumn<>("Enviado");
        colEnviado.setCellValueFactory(new PropertyValueFactory<>("enviado"));

        tabla.getColumns().addAll(colId, colCantidad, colEnviado);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void cargarPedidos() {
        try {
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
            tabla.setItems(datos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}