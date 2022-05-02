import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


public class BL implements IBL {

    private <T> Stream<T> getDataByPredicate(Collection<T> data, Predicate<T> predicate) {
        return data.stream().filter(predicate);
    }

    private <T> List<T> getDataByPredicateToList(Collection<T> data, Predicate<T> predicate) {
        return getDataByPredicate(data, predicate).collect(Collectors.toList());
    }

    private List<OrderProduct> getOrderProductByPredicate(Predicate<OrderProduct> predicate) {
        return getDataByPredicateToList(DataSource.allOrderProducts, predicate);
    }

    private <T, S, K, R> List<R> getDataByJoin(Collection<T> outer, Collection<S> inner, Function<T, K> outerKey,
                                               Function<S, K> innerKey, BiFunction<T, S, R> result) {
        return outer.stream().flatMap(keyO ->
                inner.stream().filter(keyI -> outerKey.apply(keyO) == innerKey.apply(keyI))
                        .map(x -> result.apply(keyO, x))).collect(Collectors.toList());
    }


    private <R> List<R> JoinProductsOnOrderProduct(List<OrderProduct> orderProducts, BiFunction<OrderProduct, Product, R> function) {
        return orderProducts.stream().
                flatMap(keyO -> DataSource.allProducts.stream()
                        .filter(keyI -> keyO.getProductId() == keyI.getProductId())
                        .map(x -> function.apply(keyO, x)))
                .collect(Collectors.toList());
    }

    private <R> List<R> JoinCustomerOrder(List<Order> Orders, BiFunction<Order, Customer, R> function) {
        Stream<Order> orders = Orders.stream().sorted(Comparator.comparing(Order::getCustomrId));

        return orders.flatMap(keyO -> DataSource.allCustomers.stream()
                        .filter(keyI -> keyO.getCustomrId() == keyI.getId())
                        .map(x -> function.apply(keyO, x)))
                .collect(Collectors.toList());
    }

    private <R> List<R> JoinOrderOnOrderProduct(List<OrderProduct> orderProducts, BiFunction<OrderProduct, Order, R> function) {
        return orderProducts.stream().
                flatMap(keyO -> DataSource.allOrders.stream()
                        .filter(keyI -> keyO.getOrderId() == keyI.getOrderId())
                        .map(x -> function.apply(keyO, x)))
                .collect(Collectors.toList());
    }

    private List<Product> JoinProductsOnOrderProductId(List<Long> orderProducts) {
        return orderProducts.stream().
                flatMap(keyO -> DataSource.allProducts.stream().
                filter(KeyI -> KeyI.getProductId() == keyO))
                .collect(Collectors.toList());
    }

    private <T> T getOneDataByPredicate(Collection<T> data, Predicate<T> predicate) {
        return getDataByPredicate(data, predicate).findFirst().orElse(null);
    }

    private <T, S> List<S> getDataByPredicateAndFunction(Collection<T> data, Predicate<T> predicate, Function<T, S> function) {
        return getDataByPredicate(data, predicate).map(function).collect(Collectors.toList());
    }

    @Override
    public Product getProductById(long productId) {
        return getOneDataByPredicate(DataSource.allProducts, x -> x.getProductId() == productId);
    }

    @Override
    public Order getOrderById(long orderId) {
        return getOneDataByPredicate(DataSource.allOrders, x -> x.getOrderId() == orderId);
    }

    @Override
    public Customer getCustomerById(long customerId) {
        return getOneDataByPredicate(DataSource.allCustomers, x -> x.getId() == customerId);
    }

    @Override
    public List<Product> getProducts(ProductCategory cat, double price) {
        return getDataByPredicateToList(DataSource.allProducts, x -> x.getCategory() == cat && x.getPrice() <= price);
    }

    @Override
    public List<Customer> popularCustomers() {

        Map<Long, Customer> CustomersWithTopTier = getDataByPredicate(DataSource.allCustomers, x -> x.getTier() == 3)
                .collect(Collectors.toMap(Customer::getId, x -> x));

        Map<Long, List<Order>> GropingOrdersByCustomerStir = getDataByPredicate(DataSource.allOrders, x -> CustomersWithTopTier.containsKey(x.getCustomrId()))
                .collect(groupingBy(Order::getCustomrId));

        return getDataByPredicateAndFunction(GropingOrdersByCustomerStir.values(),
                x -> x.size() > 10,
                x -> CustomersWithTopTier.get(x.get(0).getCustomrId()));
    }

    @Override
    public List<Order> getCustomerOrders(long customerId) {
        return getDataByPredicateToList(DataSource.allOrders, x -> x.getCustomrId() == customerId);
    }

    @Override
    public long numberOfProductInOrder(long orderId) {
        return getDataByPredicate(DataSource.allOrderProducts, x -> x.getOrderId() == orderId).count();

    }

    @Override
    public List<Product> getPopularOrderedProduct(int orderedTimes) {

        List<Long> groupingByProduct = getDataByPredicateAndFunction(DataSource.allOrderProducts.stream().
                collect(groupingBy(OrderProduct::getProductId, counting())).entrySet(),
                x -> x.getValue() >= orderedTimes,
                x -> x.getKey());
        return JoinProductsOnOrderProductId(groupingByProduct).stream().sorted(Comparator.comparing(Product::getProductId)).collect(Collectors.toList());
    }

  /*  @Override*/
  /*  public List<Product> getPopularOrderedProduct(int orderedTimes) {

        List<Long> groupingByProduct = DataSource.allOrderProducts.stream().
                collect(groupingBy(OrderProduct::getProductId, counting())).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                limit(orderedTimes).map(Map.Entry::getKey).collect(Collectors.toList());

        return JoinProductsOnOrderProductId(groupingByProduct);
    }              */

    @Override
    public List<Product> getOrderProducts(long orderId) {

        List<OrderProduct> productsOfOrder = getOrderProductByPredicate(x -> x.getOrderId() == orderId);

        return JoinProductsOnOrderProduct(productsOfOrder, (y, x) -> x).stream().sorted(Comparator.comparing(Product::getProductId)).collect(Collectors.toList());
    }

    @Override
    public List<Customer> getCustomersWhoOrderedProduct(long productId) {
        List<OrderProduct> orderProducts = getDataByPredicateToList(DataSource.allOrderProducts, x -> x.getProductId() == productId);

        List<Order> orders = JoinOrderOnOrderProduct(orderProducts, (y, x) -> x);

        return JoinCustomerOrder(orders, (y, x) -> x);
    }

    @Override
    public Product getMinOrderedProduct() {

        Map<Long, Long> groupingByProduct = DataSource.allOrderProducts.stream().
                collect(Collectors.groupingBy(x -> x.getProductId(), counting()));

        long max = groupingByProduct.values().stream().mapToLong(x -> x).max().getAsLong();

        long minOrderedProductId = groupingByProduct.entrySet().stream().filter(x -> x.getValue() == max).findFirst().
                map(x -> x.getKey()).get();

        return getProductById(minOrderedProductId);
    }

    @Override
    public double sumOfOrder(long orderID) {

        List<OrderProduct> orderProduct = getOrderProductByPredicate(x -> x.getOrderId() == orderID);
        return JoinProductsOnOrderProduct(orderProduct, (y, x) -> x.getPrice() * y.getQuantity()).stream().mapToDouble(x -> x).sum();
    }

    @Override
    public List<Order> getExpensiveOrders(double price) {
        return getDataByPredicateToList(DataSource.allOrders, x -> sumOfOrder(x.getOrderId()) > price);
    }

    @Override
    public List<Customer> ThreeTierCustomerWithMaxOrders() {
        List<Customer> CustomersWithTopTier = getDataByPredicateToList(DataSource.allCustomers, x -> x.getTier() == 3);

        List<Order> ordersOfCustomers = getDataByJoin(CustomersWithTopTier, DataSource.allOrders,
                Customer::getId, x -> x.getCustomrId(), (x, y) -> y);

        Map<Long, Long> numberOfOrdersParCustomers = ordersOfCustomers.stream().collect(groupingBy(Order::getCustomrId, counting()));

        return numberOfOrdersParCustomers.entrySet().stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                limit(3).map(x -> getCustomerById(x.getKey())).collect(Collectors.toList());
    }

}
