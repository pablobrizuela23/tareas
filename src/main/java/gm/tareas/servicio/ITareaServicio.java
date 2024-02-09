package gm.tareas.servicio;

import gm.tareas.modelo.Tarea;

import java.util.List;

public interface ITareaServicio {
    public List<Tarea> listarTareas();

    public Tarea buscarTareaXId(Integer idTarea);

    public void guardarTarea(Tarea tarea);

    public void borrarTarea(Tarea tarea);
}
