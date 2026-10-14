package it.mirco.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        // il server apre la porta 5000 
        // il try chiude da solo il Socket alla fine
        try (ServerSocket server = new ServerSocket(5000)) {

            // ciclo infinito: finito un client, il server ne aspetta un altro
            while (true) 
            {
                // il server aspetta un client 
                try (Socket socket = server.accept())
                {    
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // apre il canale per leggere

                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    // creo il canale di uscita 

                    String richiesta = in.readLine();
                    // legge dal canale

                    String risposta = calcola(richiesta);
                    // calcolo il risultato (o l'errore) con il metodo qui sotto

                    out.println(risposta);
                    // invio la risposta sul canale

                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                } 
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }


    private static String calcola(String richiesta)
    {        
        if (richiesta == null || richiesta.trim().isEmpty()) 
        {
            return "ERRORE: richiesta vuota";
        }

        // uno o più spazi creano nuove stringhe, trim ignora quelli all'inizio e alla fine
        String[] parti = richiesta.trim().split("\\s+");

        // devono esserci numero operatore numero
        if (parti.length != 3) {
            return "ERRORE: formato non valido, deve essere numero operatore numero";
        }

        double a,b,risultato;

        try {
            // trasformo le stringhe in numerie accetto anche la virgola
            a = Double.parseDouble(parti[0].replace(',', '.'));
            b = Double.parseDouble(parti[2].replace(',', '.'));
        } catch (NumberFormatException e) {
            // se uno dei due non e' un numero lancia questa eccezione
            return "ERRORE: gli operandi devono essere numeri";
        }

        String operatore = parti[1];
        switch (operatore) {
            case "+":
                risultato = a + b;
            break;
            case "-":
                risultato = a - b;
            break;
            case "*":
                risultato = a * b;
            break;
            case "/":
                if (b == 0) 
                    return "ERRORE: divisione per zero";                
                risultato = a / b;
            break;
            default:
                return "ERRORE";
        }
        // rende stringa il risultato, percè se no non stamperebbe gli errori
        return String.valueOf(risultato);
    }
}