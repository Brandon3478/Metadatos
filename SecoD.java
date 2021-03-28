import java.net.*;
import java.io.*;
/**
 *
 * @author axele
 */
public class SecoD {
    public static void main(String[] args){
      try{  
          int pto=1234;
          String msj="";
          DatagramSocket s = new DatagramSocket(pto);
          s.setReuseAddress(true);
         // s.setBroadcast(true);
          System.out.println("Servidor iniciado... esperando datagramas..");
          for(;;){
              DatagramPacket p = new DatagramPacket(new byte[65535],65535);
              s.receive(p);
              DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
              int n = dis.readInt();
              int tam = dis.readInt();
              byte[] b = new byte[tam];
              dis.read(b);
              msj = new String(b);
              System.out.println("Paquete: "+ n+ " desde "+p.getAddress()+":"+p.getPort()+" con " + tam + " bytes y el mensaje: "+msj);
              s.send(p);
              dis.close();
          }//for
          
      }catch(Exception e){
          e.printStackTrace();
      }//catch
        
    }//main
}
