 

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection extends Observable {
    
    private String ip;
    private int port;
    private String message;
    
    public Connection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        new Thread(new Receives()).start();
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getIp() {
        return ip;
    }
    
    public int getPort() {
        return port;
    }
    
    public void sends(String text) {
        new Thread(new Sends(text)).start();
    }
    
    public void notifies(String message) {
        this.message = message;
        setChanged();
        notifyObservers();
    }
    
    class Receives implements Runnable {
    
        byte[] dataReceive = new byte[255];
        boolean error = false;
        DatagramSocket socket = null;
        String text;
         
        @Override
        public void run() {
            while (true) {
            try {
                socket = new DatagramSocket(getPort());
            } catch (SocketException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            error = false;
            while (!error) {
                DatagramPacket packageReceived = new DatagramPacket(dataReceive, dataReceive.length);
                try {
                    socket.receive(packageReceived);
                    byte[] b = packageReceived.getData();
                    String s = "";
                    for (int i = 0; i < b.length; i++) {
                        if(b[i] != 0) {
                            s += (char) b[i];
                        }
                    }
                    String name = packageReceived.getAddress().toString() + "disse:";
                    notifies(name + s);
                } catch (Exception e) {
                    System.out.println("erro");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    error = true;
                    continue;
                }
            }
            }
        }
    }
    
    class Sends implements Runnable {
        
        String text;
        public Sends(String text) {
            this.text = text;
        }
        
        @Override
        public void run() {
            
            byte[] data = text.getBytes();
            
            try {
            DatagramSocket clientSocket = new DatagramSocket();       
            InetAddress address = InetAddress.getByName(getIp());
            DatagramPacket packages = new DatagramPacket(data, data.length, address, getPort());
            clientSocket.send(packages);       
            clientSocket.close();
                   
            } catch (SocketException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        }
        
        }
    
