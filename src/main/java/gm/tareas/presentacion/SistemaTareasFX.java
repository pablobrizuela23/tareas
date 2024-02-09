package gm.tareas.presentacion;

import ch.qos.logback.core.util.Loader;
import gm.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFX extends Application {

    private ConfigurableApplicationContext applicationContext;

//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void init(){
        this.applicationContext = new SpringApplicationBuilder(TareasApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/template/index.fxml"));
        loader.setControllerFactory(applicationContext::getBean);//con esta linea integramos javafx con spring
        Scene escena = new Scene(loader.load());
        stage.setScene(escena);//establecemos la escena que hemos creado
        stage.show();

    }
    @Override
    public void stop(){
        applicationContext.close();//cerramos cualquier conexion con la base de datos
        Platform.exit();//cerramos la aplicacion de javaFX
    }
}
