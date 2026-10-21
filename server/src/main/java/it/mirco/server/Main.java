package it.mirco.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) 
    {
        // il server apre la porta 5000 
        // il try chiude da solo il Socket alla fine
        try (ServerSocket serversocket = new ServerSocket(5000)) 
        {
            System.out.println("Server in ascolto sulla porta 5000...");

            // ciclo infinito: finito il server ne aspetta un altro
            while (true) 
            {
                //il server aspetta un client
                Socket clientsocket = serversocket.accept();
                //stampa l'ip e socket
                System.out.println("nuovo client connesso: "+clientsocket.getInetAddress().getHostAddress());

                //si apre un nuovo thread sulla clientsocket
                Esecutore clientthread = new Esecutore(clientsocket);
                new Thread(clientthread).start();
                System.out.println("nuovo thread creato");
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }       
    }
}