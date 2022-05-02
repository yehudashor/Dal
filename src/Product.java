

import java.util.List;

import static java.lang.Long.parseLong;

public class Product
{
    private long ProductId;
    private String name;
    private ProductCategory category;
    private double price;

    public Product(String[] orderInfo)  {
        this(Long.parseLong(orderInfo[1]), ProductCategory.valueOf(orderInfo[4]),
                Double.parseDouble(orderInfo[6]));
    }

    public Product(long PId, ProductCategory category, double price)
    {
        setProductId(PId);
        setCategory(category);
        setPrice(price);
    }

    public String toString()
    {
        return "Product: "+ getProductId()  + " product" + getProductId() + " category: "+ getCategory() + " price: "+ getPrice()+"\n";
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


}