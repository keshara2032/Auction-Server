package sample;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;



    public static void test(String msg){

    }


    public void startRunning(){

        try{

            connectToServer();
            setupStreams();
            whileBidding();


        }catch (EOFException e){

            printMessage("Client terminated connection !");

        }catch (IOException e){


        }


        finally {
            closeConnections();
        }



    }

    private void closeConnections() {

        printMessage("Closing connections...");
        ableToType(false);

        try {

            output.close();
            input.close();
            connection.close();

        }catch (IOException e){

        }

    }

    private void ableToType(boolean b) {

    }

    private void whileBidding() throws IOException {
        ableToType(true);

        do {

            try {

                message = (String) input.readObject();
                printMessage("\n" + message);


            }catch (ClassNotFoundException e){

                printMessage("Something's Wrong!");

            }


        }while (!message.equals("SERVER - END"));


    }

    private void setupStreams() throws IOException{

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        printMessage("Connection Established! Start Bidding");


    }

    private void connectToServer() throws IOException {

        printMessage("Attempting to connect to Auction Server ....\n");
        connection = new Socket(InetAddress.getByName(serverIP),6789);
        printMessage("Connected to: " + connection.getInetAddress().getHostName());

    }

    private void printMessage(String s) {

        System.out.println(s);

    }

}
