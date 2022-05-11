package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.util.BaseControlador;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinFreemarker;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import io.javalin.plugin.rendering.template.JavalinVelocity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PlantillasControlador extends BaseControlador {

    public PlantillasControlador(Javalin app) {
        super(app);
        registrandoPlantillas();
    }

    /**
     * Registrando los sistemas de plantillas utilizados.
     */
    private void registrandoPlantillas(){
        //registrando los sistemas de plantilla.
        JavalinRenderer.register(JavalinFreemarker.INSTANCE, ".ftl");
        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        JavalinRenderer.register(JavalinVelocity.INSTANCE, ".vm");
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {

            /**
             * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
             * ir a https://freemarker.apache.org/docs/dgui.html
             */
            path("/freemarker", () -> {

                /**
                 * Validando el sistema de plantilla
                 * Ir a: http://localhost:7000/freemarker/datosEstudiante/20011136
                 */
                get("/datosEstudiante/{matricula}", ctx -> {
                    //tomando el parametro utl y validando el tipo.
                    int matricula = ctx.pathParamAsClass("matricula", Integer.class).get();
                    Estudiante estudiante = new Estudiante(matricula, "Estudiante matricula: "+matricula, "ISC");
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("estudiante", estudiante);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/freemarker/datosEstudiante.ftl", modelo);
                });
            });

            /**
             * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
             * ir a https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
             */
            path("/thymeleaf", () -> {

                /**
                 * http://localhost:7000/thymeleaf/
                 */
                get("/", ctx -> {
                    List<Estudiante> listaEstudiante = getEstudiantes();

                    Map<String, Object> modelo = new HashMap<>();
                        modelo.put("titulo", "Ejemplo de funcionalidad Thymeleaf");
                        modelo.put("listaEstudiante", listaEstudiante);

                        //
                        ctx.render("/templates/thymeleaf/funcionalidad.html", modelo);
                    });
            });

            /**
             * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
             * ir a https://velocity.apache.org/engine/2.2/user-guide.html
             */
            path("/velocity", () -> {

                /**
                 * http://localhost:7000/velocity/
                 */
                get("/", ctx -> {
                    //listando los estudiantes..
                    List<Estudiante> listaEstudiante = getEstudiantes();

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Ejemplo de funcionalidad Velocity");
                    modelo.put("listaEstudiante", listaEstudiante);

                    //
                    ctx.render("/templates/velocity/funcionalidad.vm", modelo);
                });
            });
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
