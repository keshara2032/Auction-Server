package sample.StockDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class Stocks {

    private String symbol;
    private String securityName;
    private double price;
    private ObservableList<StockHistoryDB> bidHistory;
    static public List<Stocks> stockListCollection;
    public static HashMap<String,Stocks> map;



    public Stocks(String symbol, String securityName, double price) {
        this.symbol = symbol;
        this.securityName = securityName;
        this.price = price;
        this.bidHistory = FXCollections.observableArrayList();
        bidHistory.add(new StockHistoryDB("Initial Price","Start", price));

    }

    public ObservableList<StockHistoryDB> getBidHistory() {
        return bidHistory;
    }

    public void setBidHistory(ObservableList<StockHistoryDB> bidHistory) {
        this.bidHistory = bidHistory;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}
