import java.net.*;
import java.io.*;

public class ServidorArchivo{
    public static void main(String args[]){
        try {
            // Se crea el serversocket en el puerto 1234
            ServerSocket serverSocket = new ServerSocket(1234);
            
            // Se pone en "listen()" al socket.
            while(true){
                Socket cliente = serverSocket.accept();
                System.out.println("Conexión aceptada.");
                
                // Se instancia el objeto para lectura.
                DataInputStream dis = new DataInputStream(cliente.getInputStream());
                
                // Se lee la cantidad de archivos mandados.
                int archivos = dis.readInt();

                // Se crea el tamaño del flujo.
                byte[] b = new byte[archivos*1024];
                
                // Se empieza la escritura.
                for (int i = 0; i < archivos; i++) {
                    
                    // Se reciben los datos del archivo.
                    String nombre = dis.readUTF();
                    
                    long tam = dis.readLong();
                    long recibido = 0;
                    int n, porcentaje;
                    System.out.println("Recibimos una archivo llamado: "+nombre);
                    
                    // Se instancia el objeto de escritura.
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                    
                    //Se escribe el archivo.
                    while(recibido < tam){
                        n= dis.read(b);
                        dos.write(b,0,n);
                        dos.flush();
                        recibido = recibido+n;
                        porcentaje = (int)(recibido*100/tam);
                        System.out.println("Enviado: "+porcentaje+"%");
                    }
                    System.out.println("Archivo recibido.");

                    // Se cierra el flujo de escritura.
                    dos.close();    
                }
                // Se cierran los demás flujos.
                dis.close();
                cliente.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}