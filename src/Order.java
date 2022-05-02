

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Order {

    private long orderId;
    private Date orderDate;
    private Date deliveryDate;
    private OrderStatus status;
    private long customerId;

    public Order(String[] orderInfo) throws ParseException {
        this(Long.parseLong(orderInfo[1]),
                new SimpleDateFormat("dd/MM/yyyy").parse(orderInfo[4]),
                new SimpleDateFormat("dd/MM/yyyy").parse(orderInfo[7]),
                OrderStatus.valueOf(orderInfo[9]),
                Long.parseLong(orderInfo[12]));
    }

    public Order(long Oid, Date OrderDate, Date deliveryDate, OrderStatus status, long customerId)
    {
        setOrderId(Oid);
        setOrderDate(OrderDate);
        setDeliveryDate(deliveryDate);
        setStatus(status);
        setCustomrId(customerId);
    }

    public String toString()
    {
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
        return "order: "+ getOrderId() + " order date: "+ ft.format(getOrderDate()) +" delivery date: "+ ft.format(getDeliveryDate()) + " status: "+ getStatus() + " customr id: "+ getCustomrId()+"\n";
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getCustomrId() {
        return customerId;
    }

    public void setCustomrId(long customrId) {
        this.customerId = customrId;
    }
}