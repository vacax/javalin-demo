package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Representa las rutas para manejar las operaciones de petición - respuesta.
 */
public class CrudTradicionalControlador extends BaseControlador {

    FakeServices fakeServices = FakeServices.getInstancia();

    public CrudTradicionalControlador(Javalin app) {
        super(app);
    }

    /**
     * Las clases que implementan el sistema de plantilla están agregadas en PlantillasControlador.
     * http://localhost:7000/crud-simple/listar
     */
    @Override
    public void aplicarRutas() {
        app.routes(()->{
            path("/path/", () -> {
                before(ctx -> {
                    System.out.println("Entrando a la ruta path...");
                });
                get("/", ctx -> {
                    ctx.result("Ruta path /");
                });

                get("/compras", ctx -> {
                    ctx.result("Ruta /path/compras");
                });

                get("/otro", ctx -> {
                    ctx.result("Ruta /path/otro");
                });
            });
        });
        app.routes(() -> {
            path("/crud-simple/", () -> {


                get("/", ctx -> {
                    ctx.redirect("/crud-simple/listar");
                });

                get("/listar", ctx -> {
                    //tomando el parametro utl y validando el tipo.
                    List<Estudiante> lista = fakeServices.listarEstudiante();
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Listado de Estudiante");
                    modelo.put("lista", lista);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/crud-tradicional/listar.html", modelo);
                });

                get("/crear", ctx -> {
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Creación Estudiante");
                    modelo.put("accion", "/crud-simple/crear");
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                /**
                 * manejador para la creación del estudiante, una vez creado
                 * pasa nuevamente al listado.
                 */
                post("/crear", ctx -> {
                    //obteniendo la información enviada.
                    int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
                    String nombre = ctx.formParam("nombre");
                    String carrera = ctx.formParam("carrera");
                    //
                    Estudiante tmp = new Estudiante(matricula, nombre, carrera);
                    //realizar algún tipo de validación...
                    fakeServices.crearEstudiante(tmp); //puedo validar, existe un error enviar a otro vista.
                    ctx.redirect("/crud-simple/");
                });

                get("/visualizar/{matricula}", ctx -> {
                    Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Visaulizar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("visualizar", true); //para controlar en el formulario si es visualizar
                    modelo.put("accion", "/crud-simple/");

                    //enviando al sistema de ,plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                get("/editar/{matricula}", ctx -> {
                    Estudiante estudiante = fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get());
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Formulario Editar Estudiante "+estudiante.getMatricula());
                    modelo.put("estudiante", estudiante);
                    modelo.put("accion", "/crud-simple/editar");

                    //enviando al sistema de ,plantilla.
                    ctx.render("/templates/crud-tradicional/crearEditarVisualizar.html", modelo);
                });

                /**
                 * Proceso para editar un estudiante.
                 */
                post("/editar", ctx -> {
                    //obteniendo la información enviada.
                    int matricula = ctx.formParamAsClass("matricula", Integer.class).get();
                    String nombre = ctx.formParam("nombre");
                    String carrera = ctx.formParam("carrera");
                    //
                    Estudiante tmp = new Estudiante(matricula, nombre, carrera);
                    //realizar algún tipo de validación...
                    fakeServices.actualizarEstudiante(tmp); //puedo validar, existe un error enviar a otro vista.
                    ctx.redirect("/crud-simple/");
                });

                /**
                 * Puede ser implementando por el metodo post, por simplicidad utilizo el get. ;-D
                 */
                get("/eliminar/{matricula}", ctx -> {
                    fakeServices.eliminandoEstudiante(ctx.pathParamAsClass("matricula", Integer.class).get());
                    ctx.redirect("/crud-simple/");
                });

            });
        });
    }
}
