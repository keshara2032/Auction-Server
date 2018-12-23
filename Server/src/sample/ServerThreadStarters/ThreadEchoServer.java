package sample.ServerThreadStarters;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.StockDatabase.Stocks;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ThreadEchoServer {

    static final int PORT = 2000;
    private TextField inputText;
    private TextArea chatWindow;
    public static boolean serverStatus = true;
    private HashMap<String, Stocks> map;

    public ThreadEchoServer(TextField inputText, TextArea chatWindow,HashMap<String,Stocks> map) {
        this.inputText = inputText;
        this.chatWindow = chatWindow;
        this.map = map;

    }

    public void startServer() {

        if (serverStatus) {

            ServerSocket serverSocket = null;
            Socket socket = null;

            try {
                serverSocket = new ServerSocket(PORT);
            } catch (IOException e) {
                e.printStackTrace();

            }
            while (serverStatus) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e);
                }
                new EchoThread(socket, chatWindow, inputText,map).start();
            }
        }
    }
}