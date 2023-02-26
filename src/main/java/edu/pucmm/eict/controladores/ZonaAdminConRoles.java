package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import edu.pucmm.eict.util.RolesApp;
import io.javalin.Javalin;
import io.javalin.http.Handler;
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

        /**
         * Aplicando la configuracion para manejar los roles/
         */
        app.cfg.accessManager((handler, ctx, permittedRoles) -> {
            //para obtener el usuario estaré utilizando el contexto de sesion.
            final Usuario usuario = ctx.sessionAttribute("usuario");
            System.out.println("Los roles permitidos: "+permittedRoles.toString());
            if(permittedRoles.isEmpty()){
                handler.handle(ctx);
                return;
            }
            //validando si existe el usuario.
            if(usuario == null){
                System.out.println("No tiene permiso para acceder..");
                ctx.status(401).result("No tiene permiso para acceder...");
                return;
            }
            //buscando el permiso del usuario.
            Usuario usuarioTmp = fakeServices.getListaUsuarios().stream()
                    .filter(u -> u.getUsuario().equalsIgnoreCase(usuario.getUsuario()))
                    .findAny()
                    .orElse(null);

            if(usuarioTmp==null){
                System.out.println("Existe el usuario pero sin roles para acceder.");
                ctx.status(401).result("No tiene roles para acceder...");
                return;
            }

            //validando que el usuario registrando tiene el rol permitido.
            for(RolesApp role : usuarioTmp.getListaRoles() ) {
                if (permittedRoles.contains(role)) {
                    System.out.println(String.format("El Usuario: %s - con el Rol: %s tiene permiso", usuarioTmp.getUsuario(), role.name()));
                    handler.handle(ctx);
                    break;
                }
            }

        });

        app.routes(() -> {
           path("/zona-admin-role",() -> {
               get("/", (Handler) ctx -> {
                   ctx.result("Con permiso para acceder a la zona");
               }, RolesApp.LOGUEADO);

               get("/admin", (Handler) ctx -> {
                   ctx.result("Debe ser administrador");
               }, RolesApp.ROLE_ADMIN);

               get("/cliente", (Handler) ctx -> {
                   ctx.result("Debe ser cliente");
               }, RolesApp.ROLE_USUARIO);

               get("/otro-rol", (Handler) ctx -> {
                   ctx.result("otro cualquier rol");
               }, RolesApp.CUALQUIERA);

           });
        });


    }
}
