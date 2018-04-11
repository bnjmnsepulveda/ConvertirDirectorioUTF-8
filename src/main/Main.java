package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author benjamin
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Ingrese las opciones necesarias. java -jar ConvertirDirectorioUTF8 ruta_directorio");
            return;
        }
        String directorio = args[0];
        File dir = new File(directorio);
        List<String> archivos = getArchivosRecursivamente(dir, new ArrayList<String>());
        for (String archivo : archivos) {
            String cmdCodificacion = CommandService.execute("file -bi " + archivo);
            if (cmdCodificacion.contains("charset=") && !cmdCodificacion.contains("binary")) {                
                String codec = cmdCodificacion.split("charset=")[1].trim();
                System.out.println(archivo + " codec:" + codec);
                imprimrArchivo(archivo, codec, "UTF-8");
            }

        }
        System.out.println("archivos encontrados :" + archivos.size());

    }

    @SuppressWarnings("UseSpecificCatch")
    public static void imprimrArchivo(String ruta, String codificacionEntrada, String codificacionSalida) {
        try {
            BufferedWriter bw;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), codificacionEntrada))) {
                String sCadena;
                String salida = "";
                while ((sCadena = in.readLine()) != null) {
                    salida += sCadena + "\n";
                }
                File archivo = new File(ruta);
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(salida);
            }
            bw.close();
            System.out.println(ruta + " codificado OK.");
        } catch (Exception e) {
            System.out.println("Alerta "+ ruta + " no pudo codificarse de " + codificacionEntrada + " a " + codificacionSalida + " " + e.getMessage());
        }
    }

    public static List<String> getArchivosRecursivamente(File carpeta, List<String> archivos) {
        for (File ficheroEntrada : carpeta.listFiles()) {
            if (ficheroEntrada.isDirectory()) {
                getArchivosRecursivamente(ficheroEntrada, archivos);
            } else {
                archivos.add(ficheroEntrada.getAbsolutePath());
            }
        }
        return archivos;
    }

}
