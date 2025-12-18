package onlinestore.views;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PedidoView {

    private Stage stage;

    public PedidoView(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        Label label = new Label("Pantalla de Pedidos");

        Button volver = new Button("Volver");
        volver.setOnAction(e -> {
            TiendaView tiendaView = new TiendaView(stage);
            tiendaView.mostrar();
        });

        VBox layout = new VBox(15, label, volver);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 400, 300));
    }
}