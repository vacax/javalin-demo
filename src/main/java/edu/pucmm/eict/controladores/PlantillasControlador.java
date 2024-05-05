package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;


import io.javalin.http.ContentType;
import io.javalin.rendering.template.JavalinFreemarker;
import io.javalin.rendering.template.JavalinVelocity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PlantillasControlador extends BaseControlador {

    public PlantillasControlador(Javalin app) {
        super(app);

    }

    @Override
    public void aplicarRutas() {

        /**
         * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
         * ir a https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
         * http://localhost:7000/thymeleaf/
         */
        app.get("/thymeleaf", ctx -> {
            List<Estudiante> listaEstudiante = getEstudiantes();

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("titulo", "Ejemplo de funcionalidad Thymeleaf");
            modelo.put("listaEstudiante", listaEstudiante);

            //
            ctx.render("/templates/thymeleaf/funcionalidad.html", modelo);
        });

        /**
         * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
         * ir a https://freemarker.apache.org/docs/dgui.html
         * Validando el sistema de plantilla
         * Ir a: http://localhost:7000/freemarker/datosEstudiante/20011136
         */
        app.get("/freemarker/datosEstudiante/{matricula}", ctx -> {
            //tomando el parametro utl y validando el tipo.
            int matricula = ctx.pathParamAsClass("matricula", Integer.class).get();
            Estudiante estudiante = new Estudiante(matricula, "Estudiante matricula: "+matricula, "ISC");
            //
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("estudiante", estudiante);

            // enviando al sistema de plantilla para este caso con FreeMarker
            var render = new JavalinFreemarker();
            ctx.contentType(ContentType.HTML);
            ctx.result(render.render("/templates/freemarker/datosEstudiante.ftl", modelo, ctx));
        });


        /**
         * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
         * ir a https://velocity.apache.org/engine/2.2/user-guide.html
         * http://localhost:7000/velocity/
         */
        app.get("/velocity", ctx -> {
            //listando los estudiantes..
            List<Estudiante> listaEstudiante = getEstudiantes();

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("titulo", "Ejemplo de funcionalidad Velocity");
            modelo.put("listaEstudiante", listaEstudiante);

            // enviando al sistema de plantilla para este caso con FreeMarker
            var render = new JavalinVelocity();
            ctx.contentType(ContentType.HTML);
            ctx.result(render.render("/templates/velocity/funcionalidad.vm", modelo, ctx));
        });


    }

    @NotNull
    private List<Estudiante> getEstudiantes() {
        //listando los estudiantes..
        List<Estudiante> listaEstudiante = new ArrayList<>();
        listaEstudiante.add(new Estudiante(20011136, "Carlos Camacho", "ITT"));
        listaEstudiante.add(new Estudiante(20011137, "Otro Estudiante", "ISC"));
        listaEstudiante.add(new Estudiante(20011138, "Otro otro", "ISC"));
        return listaEstudiante;
    }
}
