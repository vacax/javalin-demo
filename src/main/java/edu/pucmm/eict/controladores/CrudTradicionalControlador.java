package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Representa las rutas para manejar las operaciones de petición - respuesta.
 */
public class CrudTradicionalControlador  {

    private final static FakeServices fakeServices = FakeServices.getInstancia();


    public static void listar(@NotNull Context ctx) throws Exception {
        //tomando el parametro utl y validando el tipo.
        List<Estudiante> lista = fakeServices.listarEstudiante();
        //
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Listado de Estudiante");
        modelo.put("lista", lista);
        //enviando al sistema de plantilla.
        ctx.render("/templates/crud-tradicional/listar.html", modelo);
    }

    public static void crearEstudianteForm(@NotNull Context ctx) throws Exception {
        //
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Formulario Creación Estudiante");
        modelo.put("accion", "/crud-simple/crear");
        //enviando al sistema de plantilla.
        ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
    }

    public static void procesarCreacionEstudiante(@NotNull Context ctx) throws Exception {
        //obteniendo la información enviada.
        int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
        String nombre = ctx.formParam("nombre");
        String carrera = ctx.formParam("carrera");
        //
        Estudiante tmp = new Estudiante(matricula, nombre, carrera);
        //realizar algún tipo de validación...
        fakeServices.crearEstudiante(tmp); //puedo validar, existe un error enviar a otro vista.
        ctx.redirect("/crud-simple/");
    }

    public static void visualizarEstudiante(@NotNull Context ctx) throws Exception {
        Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
        //
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Formulario Visaulizar Estudiante "+estudiante.getMatricula());
        modelo.put("estudiante", estudiante);
        modelo.put("visualizar", true); //para controlar en el formulario si es visualizar
        modelo.put("accion", "/crud-simple/");

        //enviando al sistema de ,plantilla.
        ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
    }

    public static void editarEstudianteForm(@NotNull Context ctx) throws Exception {
        Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
        //
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("titulo", "Formulario Editar Estudiante "+estudiante.getMatricula());
        modelo.put("estudiante", estudiante);
        modelo.put("accion", "/crud-simple/editar");

        //enviando al sistema de ,plantilla.
        ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
    }

    public static void procesarEditarEstudiante(@NotNull Context ctx) throws Exception {
        //obteniendo la información enviada.
        int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
        String nombre = ctx.formParam("nombre");
        String carrera = ctx.formParam("carrera");
        //
        Estudiante tmp = new Estudiante(matricula, nombre, carrera);
        //realizar algún tipo de validación...
        fakeServices.actualizarEstudiante(tmp); //puedo validar, existe un error enviar a otro vista.
        ctx.redirect("/crud-simple/");
    }

    public static void eliminarEstudiante(@NotNull Context ctx) throws Exception {
        fakeServices.eliminandoEstudiante(ctx.pathParamAsClass("matricula", Integer.class).get());
        ctx.redirect("/crud-simple/");
    }

}
