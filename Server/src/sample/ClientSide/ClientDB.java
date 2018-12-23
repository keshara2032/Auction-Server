package sample.ClientSide;

import java.util.HashMap;

public class ClientDB {



    private String name;
    private String time;
    private String symbol;
    private String securityName;
    private Double price;

    public  static HashMap<String,ClientDB> mapClient = new HashMap<String, ClientDB>();


    public ClientDB(String name, String time, String symbol, String securityName, Double price) {
        this.name = name;
        this.time = time;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
