package onlinestore.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import onlinestore.models.Cliente;
import onlinestore.models.ClienteEstandar;
import onlinestore.models.ClientePremium;

public class ClienteView {

    private Stage stage;
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    public ClienteView(Stage stage) {
        this.stage = stage;
    }

    @SuppressWarnings("unchecked")
    public void mostrar() {

        /* ================= FORMULARIO ================= */
        TextField txtNombre = new TextField();
        TextField txtEmail = new TextField();
        TextField txtNif = new TextField();

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.getItems().addAll("ESTANDAR", "PREMIUM");
        cmbTipo.setValue("ESTANDAR");

        GridPane formulario = new GridPane();
        formulario.setPadding(new Insets(15));
        formulario.setHgap(10);
        formulario.setVgap(10);

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);

        formulario.add(new Label("Email:"), 0, 1);
        formulario.add(txtEmail, 1, 1);

        formulario.add(new Label("NIF:"), 0, 2);
        formulario.add(txtNif, 1, 2);

        formulario.add(new Label("Tipo cliente:"), 0, 3);
        formulario.add(cmbTipo, 1, 3);

        /* ================= TABLA ================= */
        TableView<Cliente> tabla = new TableView<>();
        tabla.setItems(listaClientes);

        TableColumn<Cliente, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Cliente, String> colNif = new TableColumn<>("NIF");
        colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));

        TableColumn<Cliente, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue() instanceof ClientePremium ? "PREMIUM" : "ESTANDAR"
                )
        );

        tabla.getColumns().addAll(colNombre, colEmail, colNif, colTipo);
        tabla.setPrefHeight(220);

        /* ================= BOTONES ================= */
        Button btnGuardar = new Button("Guardar");
        Button btnVolver = new Button("Volver");

        btnGuardar.setOnAction(e -> {

            if (txtNombre.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                mostrarAlerta("Error", "Nombre y Email son obligatorios");
                return;
            }

            Cliente cliente;
            if (cmbTipo.getValue().equals("PREMIUM")) {
                cliente = new ClientePremium(
                        txtNombre.getText(),
                        txtEmail.getText(),
                        txtNif.getText(), null
                );
            } else {
                cliente = new ClienteEstandar(
                        txtNombre.getText(),
                        txtEmail.getText(),
                        txtNif.getText(), null
                );
            }

            listaClientes.add(cliente);

            txtNombre.clear();
            txtEmail.clear();
            txtNif.clear();
        });

        btnVolver.setOnAction(e -> {
            TiendaView tiendaView = new TiendaView(stage);
            tiendaView.mostrar();
        });

        HBox botones = new HBox(10, btnGuardar, btnVolver);
        botones.setAlignment(Pos.CENTER);
        botones.setPadding(new Insets(10));

        /* ================= LAYOUT PRINCIPAL ================= */
        BorderPane root = new BorderPane();
        root.setTop(formulario);
        root.setCenter(tabla);
        root.setBottom(botones);

        stage.setScene(new Scene(root, 600, 450));
        stage.setTitle("Gestión de Clientes");
        stage.show();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}