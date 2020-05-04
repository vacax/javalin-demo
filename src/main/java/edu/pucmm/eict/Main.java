package edu.pucmm.eict;

import edu.pucmm.eict.controladores.*;
import io.javalin.Javalin;

public class Main {


    public static void main(String[] args) {
        //Ejemplo hola mundo
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        //Creando la instancia del servidor.
        Javalin app = Javalin.create(config ->{
            config.addStaticFiles("/publico"); //desde la carpeta de resources
        }).start(7000);

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
        //Manejo de Api
        new ApiControlador(app).aplicarRutas();
    }

}
