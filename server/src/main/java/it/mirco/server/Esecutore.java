package it.mirco.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Esecutore implements Runnable
{    
    //creo la socket
    private Socket clientsocket;

    public Esecutore(Socket clientsocket)
    {
        this.clientsocket = clientsocket;
    }


// questo metodo viene eseguito dal thread quando parte con start()
    @Override
    public void run()
    {
        // il try-with-resources chiude da solo canali e socket alla fine
        try (Socket socket = clientsocket;
            // apre il canale per leggere
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // apre il canale di uscita
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true))               
        {
            String richiesta = in.readLine();
            // legge dal canale 
            String risposta = calcola(richiesta);
            // calcolo il risultato (o l'errore) con il metodo qui sotto 
            out.println(risposta);
            // invio la risposta sul canale 
            System.out.println("richiesta '" + richiesta + "' -> " + risposta);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }         
        System.out.println("client disconnesso, thread terminato");
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
        if (parti.length != 3) 
        {
            return "ERRORE: formato non valido, deve essere numero operatore numero";
        }
 
        double a,b,risultato;
 
        try {
            // trasformo le stringhe in numerie accetto anche la virgola
            a = Double.parseDouble(parti[0].replace(',', '.'));
            b = Double.parseDouble(parti[2].replace(',', '.'));
        } 
        catch (NumberFormatException e) 
        {
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
                return "ERRORE: operatore non riconosciuto";
        }
        
        // rende stringa il risultato, percè se no non stamperebbe gli errori
        return String.valueOf(risultato);
    }
}
