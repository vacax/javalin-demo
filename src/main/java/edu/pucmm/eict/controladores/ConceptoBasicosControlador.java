package edu.pucmm.eict.controladores;

import io.javalin.Javalin;
import io.javalin.http.Context;

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
        app.before(ctxContextContext -> {
            //
            String mensaje = String.format("Manejador before aplicando a todas las llamadas: %s, Contexto: %s, Metodo: %s",
                    ctxContextContext.req().getRemoteHost(),
                    ctxContextContext.path(),
                    ctxContextContext.req().getMethod());
            //
            System.out.println(mensaje);
        });

        /**
         * Manejador que se aplica de la ruta /isc415
         */
        app.before("/isc415", ctxContextContext -> {
            //
            String mensaje = String.format("Manejador before aplicando en el Contexto: %s, Metodo: %s",
                    ctxContextContext.req().getRequestURI(),
                    ctxContextContext.req().getMethod());
            //aplicando cambios o validaciones.
            ctxContextContext.attribute("mi-variable", "Hola Mundo"); //variable en el contexto de petición
            //
            System.out.println(mensaje);
        });

        /**
         * Handler sobre el endpoint, en al variable ctxContext.
         */
        app.get("/isc415", ctxContextContext -> {
            String metodo = ctxContextContext.req().getMethod(); //la información del encapsulada del cliente.
            metodo = ctxContextContext.method().name();
            ctxContextContext.res().setHeader("asignatura", "ISC-415");
            ctxContextContext.header("otro-header", "Mi header enviado");
            //La forma utilizando HttpServletResponse
            /*PrintWriter printWriter = new PrintWriter(ctxContext.res.getOutputStream());
            printWriter.println("Endpoint "+ctxContext.req.getRequestURI()+" -  Metodo: "+metodo);
            printWriter.flush();
            printWriter.close();*/
            ctxContextContext.result("Endpoint "+ctxContextContext.req().getRequestURI()+" -  Metodo: "+metodo+" - Variable: "+ctxContextContext.attribute("mi-variable"));
        });

        /**
         * Handler despues de cualquier llamada, siempre que no exista un error.
         * nota la ausencia de path
         */
        app.after(ctxContextContext -> {
            String mensaje = String.format("Handler after para cualquier llamada - Usuario: %s, Contexto: %s",
                    ctxContextContext.req().getRemoteHost(),
                    ctxContextContext.contextPath()
                    );
            System.out.println(mensaje);
        });

        /**
         * Aplica luego de la respuesta del endpoint del contexto /isc415
         */
        app.after("/isc415", ctxContext -> {
            //
            String mensaje = String.format("Manejador after aplicando en el Contexto: %s, Metodo: %s",
                    ctxContext.req().getRequestURI(),
                    ctxContext.req().getMethod());
            //aplicando cambios o validaciones.
            ctxContext.header("incluido-after","fue ejecutando en bloque after");
            //ctxContext.header("nombre"); ctxContext.req.getHeader("nombre") desde el cliente.
            //ctxContext.header("otro-header", ctxContext.res.getHeader("otro-header").toUpperCase()+" - Incluir otra cosa....");
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
        app.after("/html5/sinconexion.appcache", ctxContext -> {
            System.out.println("Llamando el cache....");
            ctxContext.contentType("text/cache-manifest");
        });

    }

    /**
     *
     * @param ctxContext
     */
    private void procesamiento(Context ctxContext){
        ctxContext.result("Trabajando por el metodo: "+ctxContext.method()+" - Header[profesor] = "+ctxContext.header("profesor"));
    }
}
