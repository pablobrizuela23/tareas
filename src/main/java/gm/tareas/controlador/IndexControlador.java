package gm.tareas.controlador;

import gm.tareas.modelo.Tarea;
import gm.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.scene.control.*;


import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

@Component//esto indica que es un componente de spring
public class IndexControlador implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);//por si queremos mandar info a consola

    @Autowired
    private TareaServicio tareaServicio;//con esto ya podemos inyectar cualquier tipo de beans que tngamos dentro de la fabrica

    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea,Integer> idTareaColumna;//se mapean las columnas

    @FXML
    private TableColumn<Tarea,String> nombreTareaColumna;

    @FXML
    private TableColumn<Tarea,String> responsableColumna;

    @FXML
    private TableColumn<Tarea,String> estatusColumna;

    private final ObservableList<Tarea> tareaLista = FXCollections.observableArrayList();//esto es para que cualquier cambio que tengamos en nuestra lista se muestre de manera automatica

    //se mapean los componentes txfield
    @FXML
    private TextField nombreTareaTexto;
    @FXML
    private TextField responsableTexto;
    @FXML
    private TextField estatusTexto;

    private Integer idTareaInterno;//para diferenciar el agregar del modificar
    //cuando sea agregar debe ser nulo en cambio en modificar debe tener un valor

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);//para seleccion simple
        configurarColumnas();//es para poder relacionar la informacion que vamos a cargar en cada uno de los registros
        listarTareas();//cargamos la info de la base de datos
    }

    //se indican cada uno de los atributos que se van a cargar en cada columna
    private void configurarColumnas(){
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas(){
        logger.info("Ejecutando listado de tareas");
        tareaLista.clear();
        tareaLista.addAll(tareaServicio.listarTareas());//regresa todos los objetos de tipo tarea que tengamos en la base de datos
        tareaTabla.setItems(tareaLista);//relacionamos la tabla con la lista de la base de datos
    }

    public void agregarTarea(){
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error validacion", "debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }else{
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            tarea.setIdTarea(null);
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("informacion","Tarea agregada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void cargarTareaFormulario(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if(tarea != null){
             idTareaInterno=tarea.getIdTarea();
             nombreTareaTexto.setText(tarea.getNombreTarea());
             responsableTexto.setText(tarea.getResponsable());
             estatusTexto.setText(tarea.getEstatus());
        }
    }

    private void recolectarDatosFormulario(Tarea tarea){
        if (idTareaInterno!=null){
            tarea.setIdTarea(idTareaInterno);
        }
        tarea.setNombreTarea(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTexto.getText());
        tarea.setEstatus(estatusTexto.getText());
    }

    public void modificarTarea(){
        if(idTareaInterno==null){
            mostrarMensaje("Informacion","debe seleccionar una tarea");
            return;
        }
        if (nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error de validacion","Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }

        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion","tarea modificada");
        limpiarFormulario();
        listarTareas();

    }

    public void eliminarTarea(){
        var tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if (tarea != null){
            logger.info("Registro a eliminar: " + tarea.toString());
            tareaServicio.borrarTarea(tarea);
            mostrarMensaje("Informacion","tarea eliminada: " +tarea.getIdTarea());
            limpiarFormulario();
            listarTareas();
        }else{
            mostrarMensaje("Error","No se ha seleccionado ninguna tarea");
        }
    }

    public void limpiarFormulario(){
        idTareaInterno=null;
       nombreTareaTexto.clear();
       responsableTexto.clear();
       estatusTexto.clear();
    }

    private void mostrarMensaje(String titulo,String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
