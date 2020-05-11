package edu.pucmm.eict.util;

import io.javalin.core.security.Role;

/**
 * Enum para manejar los roles de la aplicacion.
 */
public enum RolesApp implements Role {
    CUALQUIERA,
    LOGUEADO,
    ROLE_USUARIO,
    ROLE_ADMIN;
}
