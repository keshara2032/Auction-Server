package sample.ServerSide;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server extends Thread {

    private ServerSocket server;
    private Socket connection;
    private Scanner input;
    private OutputStream output;
    private javafx.scene.control.TextArea chatWindow;
    private javafx.scene.control.TextField userText;

    public Server(javafx.scene.control.TextField userText, javafx.scene.control.TextArea chatWindow){

        this.chatWindow = chatWindow;
        this.userText = userText;


    }


    // setup and run the server
    public void startRunning(){



        try {

            server = new ServerSocket(6789,100);


            while (true){
                printMessage("Trying to start\n");


                try {


                    waitForConnection();
                    setUpStreams();
                    whileBidding();


                }catch (EOFException e){
                    printMessage("\nServer ended trying!\n");
                }
                finally {
                    closeConnections();
                }


            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void closeConnections() {

        printMessage("Closing Auction Server .....\n");
        ableToType(false);
        try {

                input.close();
                output.close();
                connection.close();
//                System.exit(0);


            }catch (Exception e){
                e.printStackTrace();

            }
//            input.close();


    }

    // waiting for a connection
    private void waitForConnection() throws IOException{

        printMessage("Waiting for connections....\n");


        connection = server.accept();

        printMessage("Connected with client: "+ connection.getInetAddress().getHostName() +"\n");

        PrintWriter pwrite = new PrintWriter(connection.getOutputStream(), true);
        pwrite.println("Connected to Server\n");



    }

    // printing message to the screen on app
    private void printMessage(final String s) {


        chatWindow.appendText(s);



    }


    // setting up streaming
    private void setUpStreams() throws IOException{

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new Scanner(connection.getInputStream());
        printMessage("\nStreams are now setup\n");
    }


    private void whileBidding() throws IOException{

        String msgEnter = "Please enter your name: \n";
        sendMessage(msgEnter);
        String name = input.next();

        if(name.equals("\n") || name.equals("END") ){
            return;
        }else {


            final String clientName = name ;
            String msg = "You may start bidding now ";
            sendMessage(msg +clientName+"\n");
            ableToType(true);

            do {

                msg = input.nextLine();

                if(!msg.equals(""))
                printMessage("\n"+clientName+ " - " + msg);

                if( msg.contains("AAPL")){  // check whether symbol is in the stock list
                    System.out.println(msg);
                    String price = input.next();
                    if(price.contains("123")){ // check whether given is a double and price is higher than current
                        System.out.println(price);
                    }
                }

                if(!Controller.serverStatus) break;

            } while (!msg.equals("END") );

        }
    }

    public void ableToType(final boolean b) {

        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {

                        userText.setEditable(b);

                    }
                }


        );

    }

    public void sendMessage(String msg) {

        try {

            PrintStream p2 = new PrintStream(connection.getOutputStream());
            p2.print("\nSERVER- " + msg +"\n");
            printMessage("\nSERVER - " + msg);


        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
