package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;

import javax.servlet.http.Cookie;
import javax.swing.*;
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
        app.get("/crearCookie/:nombre/:valor", ctx -> {
            //creando una cookie para dos minutos, el parametro indicando en segundos.
            //ctx.res.addCookie(new Cookie("key", "valor"));
            ctx.cookie(ctx.pathParam("nombre"), ctx.pathParam("valor"), 120);
            ctx.cookie("usuario", "CarlosCamacho", 120);
            ctx.result("Cookie creada...");
        });

        /**
         * Listando todas las cookies almacenadas en el cliente para el servidor
         * que nos encontramos.
         * http://localhost:7000/listarCookies
         */
        app.get("/listarCookies", ctx -> {
            List<String> salida = new ArrayList<>();
            salida.add("Mostrando las cookies generadas en el cliente:");
            //listando la informacion.
            ctx.cookieMap().forEach((key, valor) -> {
                salida.add(String.format("[%s] = [%s]", key, String.join(",", valor)));
            });
            //
            if(ctx.cookie("usuario")!=null){
                salida.add("Hola "+ctx.cookie("usuario"));
            }else{
                salida.add("No envio la información");
            }
            //
            ctx.result(String.join("\n", salida));
        });

        /**
         *
         */
        app.post("/login-cookies", ctx -> {
            //recibiendo información del formulario.
            String usuario = ctx.formParam("usuario");
            String contrasena = ctx.formParam("contrasena");
            if(usuario==null || contrasena == null){
                //errror para procesar la información.
                ctx.redirect("/formulario_cookie.html");
                return;
            }
            //Estamos haciendo fake de un servicio de autenticacion, busque en un servicio.
            ctx.cookie("usuario", usuario, 120);
            ctx.cookie("nombre", "Nombre%20de%20Usuario%20"+usuario, 120);
            //enviando a la vista.
            ctx.redirect("/inicio-cookie");
        });

        /**
         *
         *
         */
        app.get("/inicio-cookie", ctx -> {
            if(ctx.cookie("nombre") == null || ctx.cookie("usuario")== null){//no ha realizado el proceso de login.
                ctx.redirect("/formulario_cookie.html");
                return;
            }
            ctx.result("Hola "+ctx.cookie("nombre")+", gracias por su visita!");
        });

        /**
         * Creando una variable de sesion en el servidor asociado al usuario.
         * http://localhost:7000/contadorSesion
         */
        app.get("/contadorSesion", ctx -> {
            //ctx.req.getSession().setAttribute("key", "valor"); // HTTPSession.
            Integer contador = ctx.sessionAttribute("contador");
            if(contador==null){
                contador = 0;
            }
            contador++;
            ctx.sessionAttribute("contador", contador);
            //
            ctx.result(String.format("Usted a visitado esta pagina %d, sesion ID #%s", contador, ctx.req.getSession().getId()));
        });

        /**
         * Invalidando la sesion, todos los objetos almacenados son eliminados.
         * http://localhost:7000/invalidarSesion
         */
        app.get("/invalidarSesion", ctx -> {
            String id = ctx.req.getSession().getId();
            //invalidando la sesion.
            ctx.req.getSession().invalidate();
            ctx.result(String.format("Sesion con ID: %s fue invalidada", id));
        });

        /**
         * Ejemplo de como autenticar utilizando sesiones.
         */
        app.post("/autenticar", ctx -> {
            //Obteniendo la informacion de la petion. Pendiente validar los parametros.
            String nombreUsuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");
            //Autenticando el usuario para nuestro ejemplo siempre da una respuesta correcta.
            Usuario usuario = FakeServices.getInstancia().autheticarUsuario(nombreUsuario, password);
            //agregando el usuario en la session... se puede validar si existe para solicitar el cambio.-
            ctx.sessionAttribute("usuario", usuario);
            //redireccionando la vista con autorizacion.
            ctx.redirect("/zona-admin-clasica/");
        });

        app.get("/contexto", ctx -> {
            ctx.req.setAttribute("variable-request", "valor"); //se mantiene hasta la respuesta del servidor.
            ctx.sessionAttribute("variable-sesion", "....."); //asociado a la cookie sesion que crea el servidor. 30 min.
            //aplicación del referencia del objeto de clase o de instancia que tenga acceso.
            lista.add("asasd");

        });

    }
}
