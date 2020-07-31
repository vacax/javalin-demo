package edu.pucmm.eict.soap;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.servicios.FakeServices;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para implementar un servicio web basado en SOAP
 */
@WebService
public class EstudianteWebServices {

    private FakeServices fakeServices = FakeServices.getInstancia();

    @WebMethod
    public String holaMundo(String hola){
        System.out.println("Ejecuntado en el servidor.");
        return "Hola Mundo "+hola+", :-D";
    }

    @WebMethod
    public List<Estudiante> getListaEstudiante(){
        return fakeServices.listarEstudiante();
    }

    @WebMethod
    public Estudiante getEstudiante(int matricula){
        return fakeServices.getEstudiantePorMatricula(matricula);
    }

    @WebMethod
    public Estudiante crearEstudiante(Estudiante estudiante){
        return fakeServices.crearEstudiante(estudiante);
    }

    @WebMethod
    public Estudiante actualizarEstudiante(Estudiante estudiante){
        return fakeServices.actualizarEstudiante(estudiante);
    }

}
