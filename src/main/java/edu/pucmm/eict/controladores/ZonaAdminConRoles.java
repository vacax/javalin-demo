package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import edu.pucmm.eict.util.RolesApp;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;

import java.util.Collections;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * Ejemplo de permisos basado en roles
 */
public class ZonaAdminConRoles extends BaseControlador {

    FakeServices fakeServices = FakeServices.getInstancia();

    public ZonaAdminConRoles(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {

        /*
         * Aplicando la configuracion para manejar los roles
         */

        app.beforeMatched("/zona-admin-role",ctx -> {
            //Si el endpoint no tiene roles asignados no es necesario verificarlo.
            if(ctx.routeRoles().isEmpty()){
                return;
            }

            //para obtener el usuario estaré utilizando el contexto de sesion.
            final Usuario usuario = ctx.sessionAttribute("usuario");
            if (usuario == null) {
                throw new UnauthorizedResponse();
            }

            //listando los roles del endpoint.
            System.out.println("Los roles asignados a la ruta: "+ctx.routeRoles());

            //buscando el permiso del usuario.
            Usuario usuarioTmp = fakeServices.getListaUsuarios().stream()
                    .filter(u -> u.getUsuario().equalsIgnoreCase(usuario.getUsuario()))
                    .findAny()
                    .orElse(null);

            if(usuarioTmp==null){
                System.out.println("Existe el usuario pero sin roles para acceder.");
                throw new UnauthorizedResponse("No tiene roles para acceder...");
            }

            System.out.println("Los roles asignados en el usuario: "+usuarioTmp.getListaRoles().toString());

            //validando que el usuario registrando tiene el rol permitido.
            boolean encontrado = false;
            for(RolesApp role : usuarioTmp.getListaRoles() ) {
                if (ctx.routeRoles().contains(role)) {
                    System.out.println(String.format("El Usuario: %s - con el Rol: %s tiene permiso", usuarioTmp.getUsuario(), role.name()));
                    encontrado = true;
                    break;
                }
            }
            //
            if(!encontrado){
                throw new UnauthorizedResponse("No tiene roles para acceder...");
            }

        });


        app.get("/zona-admin-role", (Handler) ctx -> {
            ctx.result("Con permiso para acceder a la zona");
        }, RolesApp.LOGUEADO);

        app.get("/zona-admin-role/admin", (Handler) ctx -> {
            ctx.result("Debe ser administrador");
        }, RolesApp.ROLE_ADMIN);

        app.get("/zona-admin-role/cliente", (Handler) ctx -> {
            ctx.result("Debe ser cliente");
        }, RolesApp.ROLE_USUARIO);

        app.get("/zona-admin-role/otro-rol", (Handler) ctx -> {
            ctx.result("otro cualquier rol");
        }, RolesApp.CUALQUIERA);


    }
}
