import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class ClienteArchivo{
    public static void main(String args[]){
        try {
            // Se conecta al servidor en el puerto 1234.
            Socket cliente = new Socket("localhost",1234);
            
            // Se instancia el filechooser con multiples archivos.
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);

            // Se instancia el objeto de escritura.
            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());

            // Se valida que se seleccionaron archivos.
            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                
                // Se guardan en un arreglo de tipo File.
                File[] files = fileChooser.getSelectedFiles();

                // Se manda la cantidad de archivos.
                dos.writeInt(files.length);
                dos.flush();

                // Se escribe cada archivo con su información.
                for (File f : files) {

                    // Se obtienen los datos de los archivos.
                    String archivo = f.getAbsolutePath();
                    String nombre = f.getName();
                    long tam = f.length();

                    //Se escriben los datos generales.
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    
                    // Se instancian el objeto de lectura.
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo));
                    byte[] b = new byte[files.length*1024];
                    long enviados = 0;
                    int porcentaje, n;
                    
                    // Se escribe el archivo.
                    while (enviados < tam){
                        n = dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();
                        enviados = enviados+n;
                        porcentaje = (int) (enviados*100/tam);
                        System.out.println("Enviado: "+porcentaje+"%");
                    }
                    // Se cierra el flujo de lectura.
                    System.out.println("Archivo enviado.");
                    dis.close();
                }
                // Se cierran los otros flujos.
                dos.close();
                cliente.close();
            }            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}