package edu.pucmm.eict;

import edu.pucmm.eict.controladores.*;
import io.javalin.Javalin;
import io.javalin.core.util.RouteOverviewPlugin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {


    public static void main(String[] args) {
        //Ejemplo hola mundo
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                staticFileConfig.location = Location.CLASSPATH;
            });
            config.registerPlugin(new RouteOverviewPlugin("/rutas")); //aplicando plugins de las rutas
            config.enableCorsForAllOrigins();
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));

        });

        //El contexto SOAP debe estar creando antes de inicio del servidor.
        new SoapControlador(app).aplicarRutas();

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
        //Manejo de Api.
        new ApiControlador(app).aplicarRutas();
        //Concepto de seguridad con roles.
        new ZonaAdminConRoles(app).aplicarRutas();
        //
        new CrudTradicionalControlador(app).aplicarRutas();


        //Endpoint ejemplos html5.
        app.get("/fecha", ctx -> {
            ctx.result(""+new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        });

        //Filtro para enviar el header de validación
        app.after(ctx -> {
            //System.out.println("Enviando el header de seguridad para el Service Worker");
            ctx.header("Service-Worker-Allowed", "/");
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

    private static OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("My Application");
        return new OpenApiOptions(applicationInfo).path("/openapi").swagger(new SwaggerOptions("/openapi-ui"));
    }

}
