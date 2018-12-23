package sample.ServerThreadStarters;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.ClientSide.ClientDB;
import sample.StockDatabase.StockHistoryDB;
import sample.StockDatabase.Stocks;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class EchoThread extends Thread {
    protected Socket socket;
    private TextField inputText;
    private TextArea chatWindow;
    private int  biddingStage;


    private  HashMap<String, Stocks> map;


    public EchoThread(Socket clientSocket,TextArea chatWindow, TextField inputText,HashMap<String,Stocks> map ) {
        this.socket = clientSocket;
        this.chatWindow = chatWindow;
        this.inputText = inputText;
        this.map = map;
    }


    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;

        Boolean symbolChance = false;
        Boolean priceChance = false;
        Boolean nameSet = false;
        String currentKey=null;



        String clientName = null;

        try {

            out.writeBytes("Enter your name:");
            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            line = brinp.readLine();

            while (line != null && !line.equalsIgnoreCase("quit")){



                switch (biddingStage){

                    case 0:
                        // waiting for client to enter name
                        clientName = line;
                        out.writeBytes("Welcome "+clientName +"\n");
                        out.writeBytes("Please enter the Symbol: ");
                        chatWindow.appendText("Welcome " +clientName+ "\n");

                        biddingStage = 1;
                        break;

                    case 1:

                        if (Stocks.map.containsKey(line)) {

                            out.writeBytes(Stocks.map.get(line).getSecurityName() +"  Current Price: "+ Stocks.map.get(line).getPrice()+ "\n\r");
                            out.writeBytes("Enter bidding price: ");
                            currentKey = line;
                            biddingStage = 2;

                        }else {
                            out.writeBytes("-1\n");
                            out.writeBytes("Enter a valid symbol: ");
                        }

                        break;

                    case 2:


                        if (line.matches("[0-9]+") || line.contains(".") ){
                            Double biddedPrice = 0.0;
                            try {
                                biddedPrice  = Double.parseDouble(line);
                                if(Stocks.map.get(currentKey).getPrice() <= biddedPrice) {
                                    out.writeBytes("You have successfully bid for " + map.get(currentKey).getSecurityName() + " with a price of " + biddedPrice);
                                    Stocks.map.get(currentKey).setPrice(biddedPrice);
                                    String bidTime = new SimpleDateFormat("hh:mm:ss").format(new Date());
                                    Stocks.stockListCollection = new ArrayList<Stocks>(Stocks.map.values());
                                    ClientDB.mapClient.put(currentKey,new ClientDB(clientName,bidTime,Stocks.map.get(currentKey).getSymbol(),Stocks.map.get(currentKey).getSecurityName(),Stocks.map.get(currentKey).getPrice()));
                                    Stocks.map.get(currentKey).getBidHistory().add(new StockHistoryDB(clientName,bidTime,biddedPrice));
                                    out.writeBytes("\nPlease enter the Symbol: ");

                                    biddingStage = 1;

                                }else {
                                    out.writeBytes("Please enter a higher price than current price!\n");
                                    biddingStage = 2;
                                    out.writeBytes("Enter bidding price: ");


                                }

                            }catch (NumberFormatException e){

                                out.writeBytes("\nEnter a valid price !!!!");
                                biddingStage = 2;
                                out.writeBytes("\nEnter bidding price: ");

                            }
                        }else{
                            out.writeBytes("\nEnter a valid price !!!!");
                            biddingStage = 2;
                            out.writeBytes("\nEnter bidding price: ");
                        }

                        break;


                    default:
                        System.out.println("Something went wrong! Please check help section\n\r");
//                        return;



                }

                out.flush();
                line = brinp.readLine();

            }

            socket.close();
            return;





        }catch (Exception e){
            e.printStackTrace();

        }





//        while (true) {
//            try {
//
//                line = brinp.readLine();
//
//                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
//
//
//                    socket.close();
//                    return;
//
//
//                } else {
//
//
//
//
//                    if(!nameSet ){
//
//                        if(!line.equalsIgnoreCase("")){
//                            nameSet = true;
//                            System.out.println("Name Set");
//                            name = line;
//                            chatWindow.appendText("Welcome " +name+ "\n");
//
//
//                        }else {
//
//                            out.writeBytes("Enter your name: ");
//                            out.flush();
//
//                        }
//                    }else {
//                        if(!line.trim().equals(""))
//                        chatWindow.appendText(name + "- "+line+ "\n");
//
//
//                        if (!symbolChance) {
//                            String enterSymbol = "Please enter the symbol: ";
//
//                                if (Stocks.map.containsKey(line)) {
//
//                                    out.writeBytes(Stocks.map.get(line).getSecurityName() +"  Current Price: "+ Stocks.map.get(line).getPrice()+ "\n\r");
//                                    out.flush();
//                                    currentKey = line;
//                                    symbolChance=true;
//
//                                }else {
//                                    out.writeBytes("Enter valid Symbol \n\r");
//                                    out.flush();
//                                }
//
//                                if(!symbolChance) {
//                                    out.writeBytes(enterSymbol);
//                                    out.flush();
//                                }
//                        }
//                        else {
//                            out.writeBytes("Enter bidding price: ");
//                            out.flush();
//
//                            if (line.matches("[0-9]+") || line.contains(".") ){
//                                Double biddedPrice = 0.0;
//                                try {
//                                  biddedPrice  = Double.parseDouble(line);
//
//
//                                }catch (NumberFormatException e){
//
//                                    out.writeBytes("\nEnter a valid price !!!!");
//                                    out.flush();
//
//                                }finally {
//
//                                    if(Stocks.map.get(currentKey).getPrice() <= biddedPrice) {
//
//                                        System.out.println("You have successfully bid for " + map.get(currentKey).getSecurityName() + " with a price of " + biddedPrice);
//                                        Stocks.map.get(currentKey).setPrice(biddedPrice);
//                                        Stocks.stockListCollection = new ArrayList<Stocks>(Stocks.map.values());
//
//
//                                    }
//                                }
//
//                            }
//                        }
//
//                    }
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return;
//            }
//        }
    }
}