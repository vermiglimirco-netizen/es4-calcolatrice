package it.mirco.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        // accendo la tastiera
        Scanner tastiera = new Scanner(System.in);

        //prendo i dati dalla tastiera (lo tengo come testo)
        System.out.print("Inserisci il primo numero: ");
        String primo = tastiera.nextLine().trim();


        System.out.print("Inserisci l'operatore tra + - * / ");
        String operatore = tastiera.nextLine().trim();
        // legge l'operatore

        System.out.print("Inserisci il secondo numero: ");
        String secondo = tastiera.nextLine().trim();
        // legge il secondo numero

        // preparo il messaggio da mandare,
        String richiesta = primo + " " + operatore + " " + secondo;

        // il client cerca il server e apre la socket
        // il try-with-resources chiude da solo la socket alla fine
        try (Socket socket = new Socket("localhost", 5000)) {

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // creo il canale di uscita 

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // creo il canale per leggere

            out.println(richiesta);
            // invio la richiesta sul canale

            String risposta = in.readLine();
            // legge la risposta dal canale 

            
            // stampo il risultato
            if (risposta == null)             
                System.out.println("Il server non ha risposto");
            
            else if (risposta.startsWith("ERRORE"))             
                System.out.println(risposta);
            
            else 
                System.out.println(richiesta + " = " + risposta);
            

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        
        
        
        tastiera.close();

    }
}