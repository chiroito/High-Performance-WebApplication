package domain;

import java.util.UUID;

public class Order {

    public final String orderId;

    public final Product product;

    public final User customer;

    public final int quantity;

    public Order(User customer, Product product, int quantity) {

        this.orderId = UUID.randomUUID().toString();
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }
}
