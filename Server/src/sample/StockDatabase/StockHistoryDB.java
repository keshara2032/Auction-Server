package sample.StockDatabase;

public class StockHistoryDB {

    private String clientName;
    private String time;
    private Double price;

    public StockHistoryDB(String clientName, String time, Double price) {
        this.clientName = clientName;
        this.time = time;
        this.price = price;

    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
