package edu.pucmm.eict.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class PruebaRoboSesion {

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:7000/zona-admin-clasica/";
        String sesionId = "node07tvk9x16v9da11ht8gy4jhqz90"; //Cambiar la sesion Id.
        //ejecutando
        Connection.Response resp = Jsoup.connect(url).cookie("JSESSIONID", sesionId).method(Connection.Method.GET).execute();
        System.out.println("La respuesta es: "+resp.body());
    }
}
