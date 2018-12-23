package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;


public class Controller  implements Initializable {

    private int minute;
    private int hour;
    private int second;
    private Server server;
    public static boolean serverStatus = false;
    private String message;
    private Timeline timer;
    private XYChart.Series<String, Number> series;


    private static ObservableList<Stocks> data;
    private static ObservableList<ClientDB> bidHistoryData;

    public void refreshTable() {
        stockTable.refresh();
    }

    // FXML Stuff

    @FXML
    private MenuBar menuBar;   // Menu bar in GUI

    @FXML
    private Menu serverMenuItem;   // Server tab in Menu bar

    @FXML
    private MenuItem startServer;  // Event Handler to start the server

    @FXML
    private MenuItem stopServer;  // Event Handler to stop the server

    @FXML
    private MenuItem exitProgram; // Event Handler to exit the program

    @FXML
    private TableView<Stocks> stockTable;

    @FXML
    private TableColumn<Stocks, String> Symbol;

    @FXML
    private TableColumn<Stocks, String> SecName;

    @FXML
    private TableColumn<Stocks, String> Price;

    @FXML
    private Label time;

    @FXML
    private TextArea biddingChat;


    @FXML
    private Label currentItem;


    @FXML
    private TextField sendMessage;


    @FXML
    private Menu stocksMenuItem;

    @FXML
    private MenuItem updateDatabase;


    @FXML
    private Label viewPrice7;

    @FXML
    private Text viewSecurityName7;

    @FXML
    private Label viewPrice6;

    @FXML
    private Text viewSecurityName6;

    @FXML
    private Label viewPrice5;

    @FXML
    private Text viewSecurityName5;

    @FXML
    private Label viewPrice4;

    @FXML
    private Text viewSecurityName4;

    @FXML
    private Label viewPrice3;

    @FXML
    private Text viewSecurityName3;

    @FXML
    private Label viewPrice2;

    @FXML
    private Text viewSecurityName2;

    @FXML
    private Label viewSymbol1;

    @FXML
    private Label viewPrice1;

    @FXML
    private Text viewSecurityName1;

    @FXML
    private Label viewPrice8;

    @FXML
    private Text viewSecurityName8;

    @FXML
    private TextField searchID;


    @FXML
    private AnchorPane FBTab;

    @FXML
    private LineChart<String, Number> lineChart;


    @FXML
    private AnchorPane TXNTab;

    @FXML
    private AnchorPane VRTUTab;

    @FXML
    private AnchorPane MSFTTab;


    @FXML
    private AnchorPane GOOGLTab;

    @FXML
    private AnchorPane YHOOTab;

    @FXML
    private AnchorPane XLNXTab;


    @FXML
    private AnchorPane TSLATab;

    @FXML
    private TableView<ClientDB> biddingHistory;

    @FXML
    private TableColumn<ClientDB, String> clientNameColumn;

    @FXML
    private TableColumn<ClientDB, String> clientTimeColumn;

    @FXML
    private TableColumn<ClientDB, String> clientSymbolColumn;

    @FXML
    private TableColumn<ClientDB, String> clientSecColumn;


    @FXML
    private TableColumn<ClientDB, String> clientPriceColumn;




    @FXML
    void FBLineChartUpdate() {


        lineChartUpdate("FB");


    }

    @FXML
    void VRTULineChartUpdate() {

    }


    @FXML
    void MSFTLineChartUpdate() {

    }

    @FXML
    void GOOGLLineChartUpdate() {

    }

    @FXML
    void YHOOLineChartUpdate() {

    }

    @FXML
    void XLNXLineChartUpdate() {

    }

    @FXML
    void TSLALineChartUpdate() {

    }

    @FXML
    void TXNLineChartUpdate() {

    }


    @FXML
    void lineChartUpdate(String id) {


       series = new XYChart.Series<String, Number>();

        for (StockHistoryDB i : Stocks.map.get(id).getBidHistory()) {

            series.getData().add(new XYChart.Data<>(i.getTime(), i.getPrice()));

        }

        series.setName(id);
        lineChart.getData().add(series);


    }


    @FXML
    void searchList() {

        if (searchID.getText().equals("")) stockTable.scrollTo(0);

        stockTable.getItems().stream()
                .filter(item -> (item.getSymbol().equals(searchID.getText())))
                .findAny()
                .ifPresent(item -> {
                    stockTable.getSelectionModel().select(item);
                    stockTable.scrollTo(item);
                });

        stockTable.getItems().stream()
                .filter(item -> (item.getSecurityName().toLowerCase().contains(searchID.getText().toLowerCase())))
                .findAny()
                .ifPresent(item -> {
                    stockTable.getSelectionModel().select(item);
                    stockTable.scrollTo(item);
                });

        searchID.clear();

    }


    @FXML
    void setTableDatabase() {

        setTable();

    }

    @FXML
    void exitProgram() {

        System.exit(0);

    }

    @FXML
    void stopServer() {

        ThreadEchoServer.serverStatus = false;


    }


    @FXML
    void sendMessage() {

        server.sendMessage(sendMessage.getText());

    }


    @FXML
    void serverStart() {

        ThreadEchoServer.serverStatus = true;
        setTable();


        Thread worker = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        ThreadEchoServer test = new ThreadEchoServer(sendMessage, biddingChat, Stocks.map);
                        test.startServer();
                    }
                }

        );

        worker.start();


    }


    @FXML
    void updateStocks() {

        updateTable(Stocks.stockListCollection);


    }


    void setTable() {    //initialize the table

        stockTable.setEditable(true);


        String csvFile = "/Users/kesharaweerasinghe/Desktop/CO225/Server/src/sample/stocks.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        Stocks.stockListCollection = new ArrayList<Stocks>();

        Stocks.map = new HashMap<String, Stocks>();


        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] Name = line.split(cvsSplitBy);

                if (!Name[0].equals(null) && !Name[1].equals(null) && !Name[2].equals(null))
//                    Stocks.stockListCollection.add(new Stocks(Name[0],Name[1],Double.parseDouble(Name[2])));
                    Stocks.map.put(Name[0], new Stocks(Name[0], Name[1], Double.parseDouble(Name[2])));

            }


            Stocks.stockListCollection = new ArrayList<Stocks>(Stocks.map.values());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        System.out.println(Stocks.map.get("AAPL").getPrice());


        viewPrice1.setText(Double.toString(Stocks.map.get("FB").getPrice()));
        viewPrice2.setText(Double.toString(Stocks.map.get("VRTU").getPrice()));
        viewPrice3.setText(Double.toString(Stocks.map.get("MSFT").getPrice()));
        viewPrice4.setText(Double.toString(Stocks.map.get("GOOGL").getPrice()));
        viewPrice5.setText(Double.toString(Stocks.map.get("YHOO").getPrice()));
        viewPrice6.setText(Double.toString(Stocks.map.get("XLNX").getPrice()));
        viewPrice7.setText(Double.toString(Stocks.map.get("TSLA").getPrice()));
        viewPrice8.setText(Double.toString(Stocks.map.get("TXN").getPrice()));


        data = FXCollections.observableArrayList(Stocks.stockListCollection);


        Symbol.setCellValueFactory(new PropertyValueFactory<Stocks, String>("symbol"));
        SecName.setCellValueFactory(new PropertyValueFactory<Stocks, String>("securityName"));
        Price.setCellValueFactory(new PropertyValueFactory<Stocks, String>("price"));

        stockTable.setItems(data);


    }


    void updateTable(List<Stocks> newList) {


        String currentBidSymbol = "AAPL";
        double currentBidPrice = 233.34;

        Iterator<Stocks> itr = newList.iterator();

        while (itr.hasNext()) {

            Stocks stock = itr.next();

            if (stock.getSymbol().equals(currentBidSymbol)) {

                stock.setPrice(currentBidPrice);

                biddingChat.appendText(stock.getSymbol() + " New Price: " + stock.getPrice() + "\n");

                Controller.data = FXCollections.observableArrayList(Stocks.stockListCollection);

                Symbol.setCellValueFactory(new PropertyValueFactory<Stocks, String>("symbol"));
                SecName.setCellValueFactory(new PropertyValueFactory<Stocks, String>("securityName"));
                Price.setCellValueFactory(new PropertyValueFactory<Stocks, String>("price"));

                stockTable.setItems(Controller.data);

                stockTable.refresh();

            }


        }

    }

    void updateClientHistoryTable(){

        System.out.println("Here" );

        clientNameColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("name"));
        clientTimeColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("time"));
        clientSymbolColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("symbol"));
        clientSecColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("securityName"));
        clientPriceColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("price"));
//        clientSymbolColumn.setCellValueFactory(new PropertyValueFactory<ClientDB, String>("name"));

//
//        System.out.println( bidHistoryData.get(0).getSecurityName());

        biddingHistory.setItems(bidHistoryData);
        biddingHistory.refresh();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        stockTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                stockTable.getSelectionModel().clearSelection();
                lineChart.getData().removeAll();
                lineChart.getData().clear();
                lineChartUpdate(newSelection.getSymbol());

            }
        });

        timer = new Timeline(new KeyFrame(Duration.millis(500), (ActionEvent event) -> {

            stockTable.refresh();

            if(Stocks.map != null) {

                viewPrice1.setText(Double.toString(Stocks.map.get("FB").getPrice()));
                viewPrice2.setText(Double.toString(Stocks.map.get("VRTU").getPrice()));
                viewPrice3.setText(Double.toString(Stocks.map.get("MSFT").getPrice()));
                viewPrice4.setText(Double.toString(Stocks.map.get("GOOGL").getPrice()));
                viewPrice5.setText(Double.toString(Stocks.map.get("YHOO").getPrice()));
                viewPrice6.setText(Double.toString(Stocks.map.get("XLNX").getPrice()));
                viewPrice7.setText(Double.toString(Stocks.map.get("TSLA").getPrice()));
                viewPrice8.setText(Double.toString(Stocks.map.get("TXN").getPrice()));

            }

            bidHistoryData = FXCollections.observableArrayList(ClientDB.mapClient.values());


            if(bidHistoryData != null) updateClientHistoryTable();

        }));

        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }
}
