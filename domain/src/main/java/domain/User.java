package domain;

import domain.exception.FailPurcaseException;

public class User {

    public final int userId;
    public final String name;

    public User(int userId, String name) {

        this.userId = userId;
        this.name = name;
    }

    public Order purchase(Product product, int quantity) throws FailPurcaseException {

        if (product.stock.isStocked(quantity)) {

            product.stock.apply(quantity);
            return new Order(this, product, quantity);
        }

        throw new FailPurcaseException(String.format("ユーザ (%d) は %d 個の製品 (%d) を買えませんでした！", this.userId, quantity, product.productNo));
    }
}
