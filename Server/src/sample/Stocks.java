package sample;

import java.util.HashMap;
import java.util.List;

public class Stocks {

    private String symbol;
    private String securityName;
    private double price;

    static public List<Stocks> stockListCollection;

    public static HashMap<String,Stocks> map;



    public Stocks(String symbol, String securityName, double price) {
        this.symbol = symbol;
        this.securityName = securityName;
        this.price = price;
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
