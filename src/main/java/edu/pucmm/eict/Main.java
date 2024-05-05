package edu.pucmm.eict;

import edu.pucmm.eict.controladores.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.text.SimpleDateFormat;
import java.util.Date;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {


    public static void main(String[] args) {
        //Ejemplo hola mundo
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        //Creando la instancia del servidor y configurando.
        Javalin app = Javalin.create(config ->{
            //configurando los documentos estaticos.
            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
                staticFileConfig.precompress=false;
                staticFileConfig.aliasCheck=null;
            });

            //Confifgurar el sistema de plantilla por defecto.
            config.fileRenderer(new JavalinThymeleaf());

            //
            config.router.apiBuilder(() -> {
                path("/api",() -> {

                    path("/estudiante", () -> {
                        get(ApiControlador::listarEstudiantes);
                        post(ApiControlador::crearEstudiante);
                        put(ApiControlador::actualizarEstudiante);
                        path("/{matricula}", () -> {
                            get(ApiControlador::estudiantePorMatricula);
                            delete(ApiControlador::eliminarEstudiante);
                        });
                    });

                });

                /**
                 * Las clases que implementan el sistema de plantilla están agregadas en PlantillasControlador.
                 * http://localhost:7000/crud-simple/listar
                 */
                path("/crud-simple/", () -> {
                    get(ctx -> {
                        ctx.redirect("/crud-simple/listar");
                    });
                    get("/listar",CrudTradicionalControlador::listar);
                    get("/crear",CrudTradicionalControlador::crearEstudianteForm);
                    post("/crear",CrudTradicionalControlador::procesarCreacionEstudiante);
                    get("/visualizar/{matricula}",CrudTradicionalControlador::visualizarEstudiante);
                    get("/editar/{matricula}",CrudTradicionalControlador::editarEstudianteForm);
                    post("/editar",CrudTradicionalControlador::procesarEditarEstudiante);
                    delete("/eliminar/{matricula}",CrudTradicionalControlador::eliminarEstudiante);
                });

            });


        });

        //
        app.start(getHerokuAssignedPort());

        //creando el manejador
        app.get("/", ctx -> ctx.result("Hola Mundo en Javalin :-D"));

        //aplicando los diferentes conceptos.
        new ConceptoBasicosControlador(app).aplicarRutas();
        //aplicando las rutas para el procesamiento de los datos.
        new RecibirDatosControlador(app).aplicarRutas();
        //ejemplos de cookies y sesiones.
        new CookiesSesionesControlador(app).aplicarRutas();
        //ejemplo de manejo de sesiones y seguridad clasica.
        new ZonaAdminClasica(app).aplicarRutas();
        //manejo de codigo de errores y excepciones
        new ExcepcionesControlador(app).aplicarRutas();
        //Manejo de plantillas.
        new PlantillasControlador(app).aplicarRutas();
        //Concepto de seguridad con roles.
        new ZonaAdminConRoles(app).aplicarRutas();

        //Endpoint ejemplos html5.
        app.get("/fecha", ctx -> {
            ctx.result(""+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        //Filtro para enviar el header de validación
        app.after(ctx -> {
            if(ctx.path().equalsIgnoreCase("/serviceworkers.js")){
                System.out.println("Enviando el header de seguridad para el Service Worker");
                ctx.header("Content-Type","application/javascript");
                ctx.header("Service-Worker-Allowed", "/");
            }

        });

    }

    /**
     * Metodo para indicar el puerto en Heroku
     * @return
     */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 7000; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }

}
