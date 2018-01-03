package domain;

public class Product {

    public final int productNo;

    public final String productName;

    public Stock stock;

    public Product(int productNo, String productName) {
        this.productNo = productNo;
        this.productName = productName;
    }
}
