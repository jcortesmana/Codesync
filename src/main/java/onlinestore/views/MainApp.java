package onlinestore.views;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Online Store");
        new TiendaView(stage).mostrar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}