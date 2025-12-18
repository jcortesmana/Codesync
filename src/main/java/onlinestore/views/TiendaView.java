package onlinestore.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TiendaView {

    private final Stage stage;

    public TiendaView(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {

        Label titulo = new Label("Online Store");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnClientes = new Button("Gestión de Clientes");
        Button btnPedidos = new Button("Gestión de Pedidos");
        Button btnSalir = new Button("Salir");

        btnClientes.setPrefWidth(200);
        btnPedidos.setPrefWidth(200);
        btnSalir.setPrefWidth(200);

        btnClientes.setOnAction(e -> {
            ClienteView clienteView = new ClienteView(stage);
            clienteView.mostrar();
        });

        btnPedidos.setOnAction(e -> {
            PedidoView pedidoView = new PedidoView(stage);
            pedidoView.mostrar();
        });

        btnSalir.setOnAction(e -> stage.close());

        VBox layout = new VBox(15, titulo, btnClientes, btnPedidos, btnSalir);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}