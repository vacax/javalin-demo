package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.List;

public class CookiesSesionesControlador extends BaseControlador {

    public static List<String> lista = new ArrayList<>();

    public CookiesSesionesControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        /**
         * La creación de la cookie es un proceso indicando en la trama de respuesta del servidor
         * en el cliente, en el objeto response, Javalin simplifica con un método directo.
         * http://localhost:7000/crearCookie/micookie/valor-de-la-cookie
         */
        app.get("/crearCookie/{nombre}/{valor}", ctxContext -> {
            //creando una cookie para dos minutos, el parametro indicando en segundos.
            //ctx.res.addCookie(new Cookie("key", "valor"));
            ctxContext.cookie(ctxContext.pathParam("nombre"), ctxContext.pathParam("valor"), 120);
            ctxContext.cookie("usuario", "CarlosCamacho", 120);
            ctxContext.result("Cookie creada...");
        });

        /**
         * Listando todas las cookies almacenadas en el cliente para el servidor
         * que nos encontramos.
         * http://localhost:7000/listarCookies
         */
        app.get("/listarCookies", ctxContext -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando las cookies generadas en el cliente:");
            //listando la informacion.
            ctxContext.cookieMap().forEach((key, valor) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", valor)));
            });
            //
            if(ctxContext.cookie("usuario")!=null){
                salida.add("Hola "+ctxContext.cookie("usuario"));
            }else{
                salida.add("No envio la información");
            }
            //
            ctxContext.result(String.join("\n", salida));
        });

        /**
         *
         */
        app.post("/login-cookies", ctxContext -> {
            //recibiendo información del formulario.
            String usuario = ctxContext.formParam("usuario");
            String contrasena = ctxContext.formParam("contrasena");
            if(usuario==null || contrasena == null){
                //errror para procesar la información.
                ctxContext.redirect("/formulario_cookie.html");
                return;
            }
            //Estamos haciendo fake de un servicio de autenticacion, busque en un servicio.
            ctxContext.cookie("usuario", usuario, 120);
            ctxContext.cookie("nombre", "Nombre%20de%20Usuario%20"+usuario, 120);
            //enviando a la vista.
            ctxContext.redirect("/inicio-cookie");
        });

        /**
         *
         *
         */
        app.get("/inicio-cookie", ctxContext -> {
            if(ctxContext.cookie("nombre") == null || ctxContext.cookie("usuario")== null){//no ha realizado el proceso de login.
                ctxContext.redirect("/formulario_cookie.html");
                return;
            }
            ctxContext.result("Hola "+ctxContext.cookie("nombre")+", gracias por su visita!");
        });

        /**
         * Creando una variable de sesion en el servidor asociado al usuario.
         * http://localhost:7000/contadorSesion
         */
        app.get("/contadorSesion", ctxContext -> {
            //ctx.req.getSession().setAttribute("key", "valor"); // HTTPSession.
            Integer contador = ctxContext.sessionAttribute("contador");
            if(contador==null){
                contador = 0;
            }
            contador++;
            ctxContext.sessionAttribute("contador", contador);
            //
            ctxContext.result(String.format("Usted a visitado esta pagina %d, sesion ID #%s", contador, ctxContext.req().getSession().getId()));
        });

        /**
         * Invalidando la sesion, todos los objetos almacenados son eliminados.
         * http://localhost:7000/invalidarSesion
         */
        app.get("/invalidarSesion", ctxContext -> {
            String id = ctxContext.req().getSession().getId();
            //invalidando la sesion.
            ctxContext.req().getSession().invalidate();
            ctxContext.result(String.format("Sesion con ID: %s fue invalidada", id));
        });

        /**
         * Ejemplo de como autenticar utilizando sesiones.
         */
        app.post("/autenticar", ctxContext -> {
            //Obteniendo la informacion de la petion. Pendiente validar los parametros.
            String nombreUsuario = ctxContext.formParam("usuario");
            String password = ctxContext.formParam("password");
            //Autenticando el usuario para nuestro ejemplo siempre da una respuesta correcta.
            Usuario usuario = FakeServices.getInstancia().autheticarUsuario(nombreUsuario, password);
            //agregando el usuario en la session... se puede validar si existe para solicitar el cambio.-
            ctxContext.sessionAttribute("usuario", usuario);
            //redireccionando la vista con autorizacion.
            ctxContext.redirect("/zona-admin-clasica/");
        });

        app.get("/contexto", ctxContext -> {
            ctxContext.req().setAttribute("variable-request", "valor"); //se mantiene hasta la respuesta del servidor.
            ctxContext.sessionAttribute("variable-sesion", "....."); //asociado a la cookie sesion que crea el servidor. 30 min.
            //aplicación del referencia del objeto de clase o de instancia que tenga acceso.
            lista.add("asasd");

        });

    }
}
