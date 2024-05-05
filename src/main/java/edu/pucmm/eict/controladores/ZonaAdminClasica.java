package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;


public class ZonaAdminClasica extends BaseControlador {

    public ZonaAdminClasica(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        /**
         * Ejemplo para agrupar path según su contexto, ejemplo.
         * http://localhost:7000/zona-admin-clasica/
         *
         * Las sesiones pueden ser vulnerables por el robo se sesiones:
         * https://es.wikipedia.org/wiki/Secuestro_de_sesi%C3%B3n
         */

        /**
         * Validando que exista el usuario por el filtro.
         */
        app.before("/zona-admin-clasica/", ctx -> {
            //recuperando el usuario de la sesión,en caso de no estar, redirecciona la pagina 401.
            Usuario usuario = ctx.sessionAttribute("usuario");
            if(usuario == null){// usuario no existe
                ctx.redirect("/401.html");
            }
            //continuando con la consulta del endpoint solicitado.
        });

        //Respuesta del index.
        //http://localhost:7000/zona-admin-clasica/
        app.get("/zona-admin-clasica/", ctx ->{
            Usuario usuario = ctx.sessionAttribute("usuario");
            ctx.result("Zona Admin por la forma clasica --- Usuario: "+usuario.getUsuario());
        } );

        //controlado por el filtro...
        //http://localhost:7000/zona-admin-clasica/otro-zona/otra/
        app.get("/zona-admin-clasica/otro-zona/otra/", ctx -> {
            ctx.result("El filtro controla todas los path por debajo del grupo....");
        });

    }
}
