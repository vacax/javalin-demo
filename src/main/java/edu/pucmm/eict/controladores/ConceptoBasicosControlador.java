package edu.pucmm.eict.controladores;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.eclipse.jetty.io.WriterOutputStream;

import java.io.PrintWriter;

public class ConceptoBasicosControlador {

    private Javalin app;

    public ConceptoBasicosControlador(Javalin app){
        this.app = app;
    }

    public void aplicarRutas(){
        /**
         * Manejador que se aplica todas las llamadas que sean realizada.
         * Notar la ausencia del path.
         */
        app.before(ctx -> {
            //
            String mensaje = String.format("Manejador before aplicando a todas las llamadas: %s, Contexto: %s, Metodo: %s",
                    ctx.req.getRemoteHost(),
                    ctx.path(),
                    ctx.req.getMethod());
            //
            System.out.println(mensaje);
        });

        /**
         * Manejador que se aplica de la ruta /isc415
         */
        app.before("/isc415", ctx -> {
            //
            String mensaje = String.format("Manejador before aplicando en el Contexto: %s, Metodo: %s",
                    ctx.req.getRequestURI(),
                    ctx.req.getMethod());
            //aplicando cambios o validaciones.
            ctx.attribute("mi-variable", "Hola Mundo"); //variable en el contexto de petición
            //
            System.out.println(mensaje);
        });

        /**
         * Handler sobre el endpoint, en al variable ctx.
         */
        app.get("/isc415", ctx -> {
            String metodo = ctx.req.getMethod(); //la información del encapsulada del cliente.
            metodo = ctx.method();
            ctx.res.setHeader("asignatura", "ISC-415");
            ctx.header("otro-header", "Mi header enviado");
            //La forma utilizando HttpServletResponse
            /*PrintWriter printWriter = new PrintWriter(ctx.res.getOutputStream());
            printWriter.println("Endpoint "+ctx.req.getRequestURI()+" -  Metodo: "+metodo);
            printWriter.flush();
            printWriter.close();*/
            ctx.result("Endpoint "+ctx.req.getRequestURI()+" -  Metodo: "+metodo+" - Variable: "+ctx.attribute("mi-variable"));
        });

        /**
         * Handler despues de cualquier llamada, siempre que no exista un error.
         * nota la ausencia de path
         */
        app.after(ctx -> {
            String mensaje = String.format("Handler after para cualquier llamada - Usuario: %s, Contexto: %s",
                    ctx.req.getRemoteHost(),
                    ctx.contextPath()
                    );
            System.out.println(mensaje);
        });

        /**
         * Aplica luego de la respuesta del endpoint del contexto /isc415
         */
        app.after("/isc415", ctx -> {
            //
            String mensaje = String.format("Manejador after aplicando en el Contexto: %s, Metodo: %s",
                    ctx.req.getRequestURI(),
                    ctx.req.getMethod());
            //aplicando cambios o validaciones.
            ctx.header("incluido-after","fue ejecutando en bloque after");
            //ctx.header("nombre"); ctx.req.getHeader("nombre") desde el cliente.
            //ctx.header("otro-header", ctx.res.getHeader("otro-header").toUpperCase()+" - Incluir otra cosa....");
            //
            System.out.println(mensaje);
        });

        /**
         * la ruta (path) puede ser la misma siempre y cuando el verbo cambie.
         * Ver los diferentes ejemplos.
         */
        app.post("/isc415",this::procesamiento);

        app.put("/isc415", this::procesamiento);

        app.delete("/isc415", this::procesamiento);

        app.options("/isc415", this::procesamiento);

        app.patch("/isc415", this::procesamiento);

        app.head("/isc415", this::procesamiento);

        /**
         * bloque retornar el mimetype del archivo para el cache.
         */
        app.after("/html5/sinconexion.appcache", ctx -> {
            System.out.println("Llamando el cache....");
            ctx.contentType("text/cache-manifest");
        });

    }

    /**
     *
     * @param ctx
     */
    private void procesamiento(Context ctx){
        ctx.result("Trabajando por el metodo: "+ctx.method()+" - Header[profesor] = "+ctx.header("profesor"));
    }
}
