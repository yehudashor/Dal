import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataSource {

    public static List<Customer> allCustomers;
    public static List<Order> allOrders;
    public static List<Product> allProducts;
    public static List<OrderProduct> allOrderProducts;

    // Update this path according to your data files location
    public static String basePath = "C:\\Users\\97253\\OneDrive\\שולחן העבודה\\c#\\TextFileCh\\ConsoleApp2\\targil2\\Files\\";
    public static String customersPath = basePath + "customers.txt";
    public static String ordersPath = basePath + "orders.txt";
    public static String productsPath = basePath + "products.txt";
    public static String orderProductPath = basePath + "orderProduct.txt";

    static {
        try {
            allCustomers = readCustomersFromFile();
            allOrders = readOrdersFromFile();
            allProducts = readProductsFromFile();
            allOrderProducts = readOrderProductsFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> List<T> readDataFromFile(String fileName, Function<String, T> function) throws IOException {
        return Files.lines(Paths.get(fileName)).map(function).collect(Collectors.toList());
    }

    public static List<Customer> readCustomersFromFile() throws IOException {
        return readDataFromFile(customersPath, x -> new Customer(splitString(x)));
    }

    public static List<Order> readOrdersFromFile() throws IOException {
        return readDataFromFile(ordersPath, x -> {
            try {
                return new Order(splitString(x));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public static List<Product> readProductsFromFile() throws IOException {
        return readDataFromFile(productsPath, x -> new Product(splitString(x)));
    }

    public static List<OrderProduct> readOrderProductsFromFile() throws IOException {
        return readDataFromFile(orderProductPath, x -> new OrderProduct(splitString(x)));
    }

    private static String[] splitString(String str){
        return str.split(" ");
    }
}


