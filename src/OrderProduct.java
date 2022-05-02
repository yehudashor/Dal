

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.Long.parseLong;

public class OrderProduct {
    private long orderId;
    private long productId;
    private int quantity;

    public OrderProduct(String[] orderInfo) {
        this(Long.parseLong(orderInfo[2]),
                Long.parseLong(orderInfo[5]), Integer.parseInt(orderInfo[7]));
    }

    public OrderProduct(long oId, long pId, int quantity) {
        setOrderId(oId);
        setProductId(pId);
        setQuantity(quantity);
    }

    public String toString() {
        return "ord id: " + getOrderId() + " prod id: " + getProductId() + " quantity: " + getQuantity() + "\n";
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
