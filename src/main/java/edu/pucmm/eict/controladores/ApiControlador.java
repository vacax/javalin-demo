package edu.pucmm.eict.controladores;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.servicios.FakeServices;
import edu.pucmm.eict.util.BaseControlador;
import edu.pucmm.eict.util.NoExisteEstudianteException;
import io.javalin.Javalin;
import io.javalin.openapi.OpenApi;

import static io.javalin.apibuilder.ApiBuilder.*;
@OpenApi(path = "/api")
public class ApiControlador extends BaseControlador {

    private FakeServices fakeServices = FakeServices.getInstancia();

    public ApiControlador(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {
            path("/api", () -> {
                /**
                 * Ejemplo de una API REST, implementando el CRUD
                 * ir a
                 */
                path("/estudiante", () -> {
                    after(ctx -> {
                        ctx.header("Content-Type", "application/json");
                    });

                    get("/", ctx -> {
                        ctx.json(fakeServices.listarEstudiante());
                    });

                    get("/{matricula}", ctx -> {
                        ctx.json(fakeServices.getEstudiantePorMatricula(ctx.pathParamAsClass("matricula", Integer.class).get()));
                    });

                    post("/", ctx -> {
                        //parseando la informacion del POJO debe venir en formato json.
                        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                        //creando.
                        ctx.json(fakeServices.crearEstudiante(tmp));
                    });

                    put("/", ctx -> {
                        //parseando la informacion del POJO.
                        Estudiante tmp = ctx.bodyAsClass(Estudiante.class);
                        //creando.
                        ctx.json(fakeServices.actualizarEstudiante(tmp));

                    });

                    delete("/{matricula}", ctx -> {
                        //creando.
                        ctx.json(fakeServices.eliminandoEstudiante(ctx.pathParamAsClass("matricula", Integer.class).get()));
                    });
                });
            });
        });

        app.exception(NoExisteEstudianteException.class, (exception, ctx) -> {
            ctx.status(404);
            ctx.json(""+exception.getLocalizedMessage());
        });
    }
}
