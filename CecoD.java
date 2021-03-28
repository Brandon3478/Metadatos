import java.net.*;
import java.io.*;
import java.util.Arrays;


public class CecoD {
    public static void main(String[] args){
      try{  
          int pto=1234;
          int aux = 0;
          String dir="127.0.0.1";
          InetAddress dst= InetAddress.getByName(dir);
          int tam = 15;
          BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
          DatagramSocket cl = new DatagramSocket();
          while(true){
              System.out.println("Escribe un mensaje, <Enter> para enviar, \"salir\" para terminar");
              String msj = br.readLine();
              if(msj.compareToIgnoreCase("salir")==0){
                  System.out.println("termina programa");
                  br.close();
                  cl.close();
                  System.exit(0);
              }else{
                  byte[]b = msj.getBytes();
                  if(b.length>tam){
                      byte[]b_eco = new byte[b.length];
                      System.out.println("b_eco: "+b_eco.length+" bytes");
                      int tp = (int)(b.length/tam);
    //                  if(b.length%tam>0)
    //                      tp=tp+1;
                      for(int j=0;j<tp;j++){
                          //byte[] tmp = new byte[tam];
                          byte []tmp=Arrays.copyOfRange(b, j*tam, ((j*tam)+(tam)));
                          //System.out.println("tmp tam "+tmp.length);
                          ByteArrayOutputStream baos = new ByteArrayOutputStream();
                          DataOutputStream dos = new DataOutputStream(baos);
                          dos.writeInt(j+1);
                          dos.writeInt(tmp.length);
                          dos.write(tmp);
                          dos.flush();
                          
                          byte[] b1 = baos.toByteArray();
                          DatagramPacket p= new DatagramPacket(b1,b1.length,dst,pto);
                          cl.send(p);
                          System.out.println("Enviando fragmento "+(j+1)+" de "+tp+"\ndesde: "+(j*tam)+" hasta "+((j*tam)+(tam)));
                          
                          String segmento  = new String (tmp);
                          System.out.println("El segmento enviado es: " + segmento);
                          
//                          DatagramPacket p1= new DatagramPacket(new byte[tam],tam);
//                          cl.receive(p1);
//                          byte[]bp1 = p1.getData();
                          byte[] b2= obtenerDatos(cl);
                          for(int i=0; i<tam;i++){
                              b_eco[(j*tam)+i]=b2[i];
                          }//for
                          aux = j+2;
                      }//for
                      if(b.length%tam>0){ //bytes sobrantes  
                          //tp=tp+1;
                          int sobrantes = b.length%tam;
                          System.out.println("Sobrantes: "+sobrantes);
                          
                          System.out.println("b:"+b.length+" ultimo pedazo desde "+tp*tam+" hasta "+((tp*tam)+sobrantes));
                          byte[] tmp = Arrays.copyOfRange(b, tp*tam, ((tp*tam)+sobrantes));
                          
                          ByteArrayOutputStream baos = new ByteArrayOutputStream();
                          DataOutputStream dos = new DataOutputStream(baos);
                          dos.writeInt(aux);
                          dos.writeInt(tmp.length);
                          dos.write(tmp);
                          dos.flush();
                          byte[] b1 = baos.toByteArray();
                          DatagramPacket p = new DatagramPacket(b1,b1.length,dst,pto);
                          cl.send(p);
                          
                          String segmento  = new String (tmp);
                          System.out.println("El segmento enviado es: " + segmento);
                          
                          //System.out.println("tmp tam "+tmp.length);
//                          DatagramPacket p1= new DatagramPacket(new byte[tam],tam);
//                          cl.receive(p1);
//                          byte[]bp1 = p1.getData();
                          byte[] b2= obtenerDatos(cl);
                          for(int i=0; i<sobrantes;i++){
                              //System.out.println((tp*tam)+i+"->"+i);
                              b_eco[(tp*tam)+i]=b2[i];
                          }//for
                      }//if

                      String eco = new String(b_eco);
                      System.out.println("Eco recibido: "+eco);
                  }else{

                      DatagramPacket p=new DatagramPacket(b,b.length,dst,pto);
                      cl.send(p);
                      DatagramPacket p1 = new DatagramPacket(new byte[65535],65535);
                      cl.receive(p1);
                      String eco = new String(p1.getData(),0,p1.getLength());
                      System.out.println("Eco recibido: "+eco);
                  }//else
              }//else
          }//while
      }catch(Exception e){
          e.printStackTrace();
      }//catch
    }//main
    
    public static byte[] obtenerDatos(DatagramSocket cl) throws IOException{
        DatagramPacket p = new DatagramPacket(new byte[65535],65535);
        cl.receive(p);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(p.getData()));
        int numero = dis.readInt();
        int tamano = dis.readInt();
        String eco = new String(p.getData(), 0, p.getLength());
        byte[] b = new byte[tamano];
        dis.read(b);
        return b;
    }
}


