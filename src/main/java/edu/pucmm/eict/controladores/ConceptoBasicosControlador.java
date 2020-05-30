package edu.pucmm.eict.controladores;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

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
                    ctx.req.getServletPath(),
                    ctx.req.getMethod());
            //
            System.out.println(mensaje);
        });

        /**
         * Manejador que se aplica de la ruta /isc415
         */
        app.before("/isc415", ctx -> {
            //
            String mensaje = String.format("Manejador aplicando en el Contexto: %s, Metodo: %s",
                    ctx.req.getServletPath(),
                    ctx.req.getMethod());
            //aplicando cambios o validaciones.

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
            ctx.result("Endpoint "+ctx.req.getRequestURI()+" -  Metodo: "+metodo);
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
            String mensaje = String.format("Manejador aplicando en el Contexto: %s, Metodo: %s",
                    ctx.req.getServletPath(),
                    ctx.req.getMethod());
            //aplicando cambios o validaciones.

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

    }

    /**
     *
     * @param ctx
     */
    private void procesamiento(Context ctx){
        ctx.result("Trabajando por el metodo: "+ctx.method()+" - Header[profesor] = "+ctx.header("profesor"));
    }
}
