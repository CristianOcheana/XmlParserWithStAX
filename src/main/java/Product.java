import java.sql.Timestamp;
import java.util.Date;

public class Product {
    private String description;
    private String gtin;
    private double price;
    private String currency;
    private String orderid;
    private Date timestamp;

    public Product(String orderno, Date timestamp) {
        this.orderid = orderno;
        this.timestamp = timestamp;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getGtin() {
        return gtin;
    }

    public String getOrderid() {
        return orderid;
    }
}
