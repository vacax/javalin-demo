package edu.pucmm.eict.servicios;

import edu.pucmm.eict.encapsulaciones.Estudiante;
import edu.pucmm.eict.encapsulaciones.Usuario;
import edu.pucmm.eict.util.NoExisteEstudianteException;
import edu.pucmm.eict.util.RolesApp;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ejemplo de servicio patron Singleton
 */
public class FakeServices {

    private static FakeServices instancia;
    private List<Estudiante> listaEstudiante = new ArrayList<>();
    private List<Usuario> listaUsuarios = new ArrayList<>();

    /**
     * Constructor privado.
     */
    private FakeServices(){
        //añadiendo los estudiantes.
        listaEstudiante.add(new Estudiante(20011136, "Carlos Camacho", "ITT"));
        //anadiendo los usuarios.
        listaUsuarios.add(new Usuario("admin", "admin", "1234", Set.of(RolesApp.ROLE_ADMIN, RolesApp.CUALQUIERA, RolesApp.LOGUEADO)));
        listaUsuarios.add(new Usuario("logueado", "logueado", "logueado", Set.of(RolesApp.CUALQUIERA)));
        listaUsuarios.add(new Usuario("usuario", "usuario", "usuario", Set.of(RolesApp.ROLE_USUARIO)));

    }

    public static FakeServices getInstancia(){
        if(instancia==null){
            instancia = new FakeServices();
        }
        return instancia;
    }

    /**
     * Permite autenticar los usuarios. Lo ideal es sacar en
     * @param usuario
     * @param password
     * @return
     */
    public Usuario autheticarUsuario(String usuario, String password){
        //simulando la busqueda en la base de datos.
        return new Usuario(usuario, "Usuario "+usuario, password);
    }

    public List<Usuario> getListaUsuarios(){
        return listaUsuarios;
    }

    public List<Estudiante> listarEstudiante(){
        return listaEstudiante;
    }

    public Estudiante getEstudiantePorMatricula(int matricula){
        return listaEstudiante.stream().filter(e -> e.getMatricula() == matricula).findFirst().orElse(null);
    }

    public Estudiante crearEstudiante(Estudiante estudiante){
        if(getEstudiantePorMatricula(estudiante.getMatricula())!=null){
            System.out.println("Estudiante registrado...");
            return null; //generar una excepcion...
        }
        listaEstudiante.add(estudiante);
        return estudiante;
    }

    public Estudiante actualizarEstudiante(Estudiante estudiante){
        Estudiante tmp = getEstudiantePorMatricula(estudiante.getMatricula());
        if(tmp == null){//no existe, no puede se actualizado
            throw new NoExisteEstudianteException("No Existe el estudiante: "+estudiante.getMatricula());
        }
        tmp.mezclar(estudiante);
        return tmp;
    }

    public boolean eliminandoEstudiante(int matricula){
        Estudiante tmp = new Estudiante();
        tmp.setMatricula(matricula);
        return listaEstudiante.remove(tmp);
    }

}
