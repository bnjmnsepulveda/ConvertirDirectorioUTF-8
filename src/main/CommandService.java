package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Servicio de ejecucion de comandos y procesos.
 *
 * @author Roberto
 */
public class CommandService {

    /**
     * Ejecuta un comando y devuelve la salida en String.
     *
     * @param cmd comando a ejecutar
     * @return
     * @throws java.io.IOException
     */
    public String executeCommand2(String cmd) throws IOException {
        String linea;
        String salida = null;
        String lineaError;
        String salidaError;
        Process proceso;
        InputStreamReader entrada = null;
        InputStreamReader error = null;
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            String[] pipelineCmd = {
                "/bin/bash",
                "-c",
                cmd
            };
            proceso = Runtime.getRuntime().exec(pipelineCmd);
            entrada = new InputStreamReader(proceso.getInputStream());
            error = new InputStreamReader(proceso.getErrorStream());
            stdInput = new BufferedReader(entrada);
            stdError = new BufferedReader(error);
            System.out.println("Comando a ejecutar " + cmd);
            if ((linea = stdInput.readLine()) != null) {
                salida = linea;
                while ((linea = stdInput.readLine()) != null) {
                    salida += linea + "\n";
                }
                System.out.println("Resultado del comando " + salida);
            } else {
                System.out.println("No se a producido ninguna salida");
            }
            if ((lineaError = stdError.readLine()) != null) {
                salidaError = lineaError;
                while ((lineaError = stdError.readLine()) != null) {
                    salidaError += lineaError + "\n";
                }
                System.out.println("Resultado del error en comando " + salidaError);
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (entrada != null) {
                entrada.close();
            }
            if (error != null) {
                error.close();
            }
            if (stdInput != null) {
                stdInput.close();
            }
            if (stdError != null) {
                stdError.close();
            }
        }
        return salida;
    }

    public static String execute(String cmd) throws IOException {
        String linea;
        String salida = null;
        String[] pipelineCmd = {
            "/bin/bash",
            "-c",
            cmd
        };
        Process proceso = Runtime.getRuntime().exec(pipelineCmd);
        BufferedReader stdInput;
        try (InputStreamReader entrada = new InputStreamReader(proceso.getInputStream())) {
            stdInput = new BufferedReader(entrada);
            if ((linea = stdInput.readLine()) != null) {
                salida = linea;
                while ((linea = stdInput.readLine()) != null) {
                    salida += linea + "\n";
                }
            }
        }
        stdInput.close();
        return salida;
    }

    public static int executeExitValue(String cmd) throws IOException {
        String[] pipelineCmd = {
            "/bin/bash",
            "-c",
            cmd
        };
        Process proceso = Runtime.getRuntime().exec(pipelineCmd);
        return proceso.exitValue();
    }

}
