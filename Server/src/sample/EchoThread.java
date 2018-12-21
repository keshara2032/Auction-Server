package sample;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class EchoThread extends Thread {
    protected Socket socket;
    private TextField inputText;
    private TextArea chatWindow;

    private  HashMap<String,Stocks> map;


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

        String name = null;

        try {

            out.writeBytes("Enter your name:");
            out.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }



        while (true) {
            try {

                line = brinp.readLine();

                if ((line == null) || line.equalsIgnoreCase("QUIT")) {


                    socket.close();
                    return;


                } else {




                    if(!nameSet ){

                        if(!line.equalsIgnoreCase("")){
                            nameSet = true;
                            System.out.println("Name Set");
                            name = line;
                            chatWindow.appendText("Welcome " +name+ "\n");


                        }else {

                            out.writeBytes("Enter your name: ");
                            out.flush();

                        }
                    }else {
                        if(!line.trim().equals(""))
                        chatWindow.appendText(name + "- "+line+ "\n");


                        if (!symbolChance) {
                            String enterSymbol = "Please enter the symbol: ";

                                if (Stocks.map.containsKey(line)) {

                                    out.writeBytes(Stocks.map.get(line).getSecurityName() +"  Current Price: "+ Stocks.map.get(line).getPrice()+ "\n\r");
                                    out.flush();
                                    currentKey = line;
                                    symbolChance=true;

                                }else {
                                    out.writeBytes("Enter valid Symbol \n\r");
                                    out.flush();
                                }

                                if(!symbolChance) {
                                    out.writeBytes(enterSymbol);
                                    out.flush();
                                }
                        }
                        else {
                            out.writeBytes("Enter bidding price: ");
                            out.flush();

                            if (line.matches("[0-9]+") || line.contains(".") ){
                                Double biddedPrice = 0.0;
                                try {
                                  biddedPrice  = Double.parseDouble(line);


                                }catch (NumberFormatException e){

                                    out.writeBytes("\nEnter a valid price !!!!");
                                    out.flush();

                                }finally {

                                    if(Stocks.map.get(currentKey).getPrice() <= biddedPrice) {

                                        System.out.println("You have successfully bid for " + map.get(currentKey).getSecurityName() + " with a price of " + biddedPrice);
                                        Stocks.map.get(currentKey).setPrice(biddedPrice);
                                        Stocks.stockListCollection = new ArrayList<Stocks>(Stocks.map.values());


                                    }
                                }

                            }
                        }

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}