package edu.pucmm.eict.controladores;

import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecibirDatosControlador extends BaseControlador {

    public RecibirDatosControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        /**
         * Ejemplo para leer por parametros de consulta (query param)
         * http://localhost:7000/parametros?matricula=20011126&nombre=Carlos%20Camacho
         */
        app.get("/parametros", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos los parametros enviados:");
            //listando la informacion.
            ctx.queryParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            ctx.result(String.join("\n", salida));
        });

        /**
         * Ejemplo de parametros como parte de la URL, notar los ':' en el path.
         * http://localhost:7000/parametros/20011136/
         */
        app.get("/parametros/:matricula/", ctx -> {
            //TODO: metodo para validar matricula..
            ctx.result("El Estudiante tiene la matricula: "+ctx.pathParam("matricula"));
        });

        /**
         * Ejemplo de parametros como parte de la URL, notar los ':' en el path.
         * Puedo hacer combinaciones
         * http://localhost:7000/parametros/20011136/nombre/carloscamacho
         */
        app.get("/parametros/:matricula/nombre/:nombre", ctx -> {
            ctx.result("El Estudiante tiene la matricula: "+ctx.pathParam("matricula")+" - nombre: "+ctx.pathParam("nombre"));
        });

       /* app.get("/parametros/:para1/:para2/:para3", ctx -> {
            ctx.result("hhhhh");
        });

        app.get("/parametros/:para4/:para5/:para6", ctx -> {
            ctx.result("kkkkkk");
        });*/

        /**
         * Ejemplo de información en el cuerpo del mensaje
         * http://localhost:7000/formulario.html para el formulario
         */
        app.post("/parametros", ctx -> {
            System.out.println("El tipo de datos recibido: "+ctx.header("Content-Type")+ "Matricula:"+ctx.queryParam("matricula"));
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando todos la informacion enviada en el cuerpo:");
            //listando la informacion.
            ctx.formParamMap().forEach((key, lista) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });
            //
            ctx.result(String.join("\n", salida));
        });

        /**
         * En cualquier situación puedo los encabezados de la trama HTTP.
         * http://localhost:7000/leerheaders
         */
        app.get("leerheaders", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando la informacion enviada en los headers:");
            //listando la informacion.
            ctx.headerMap().forEach((key, valor) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", valor)));
            });
            //
            ctx.result(String.join("\n", salida));
        });
    }
}
