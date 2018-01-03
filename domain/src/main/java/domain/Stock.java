package domain;

public class Stock {

    private final Product product;
    private int stockedQuantity;

    public Stock(Product product, int stockedQuantity) {
        this.product = product;
        product.stock = this;

        this.stockedQuantity = stockedQuantity;
    }

    /*
    在庫から引き当てる
     */
    public void apply(int applyNum) {
        this.stockedQuantity -= applyNum;
    }

    /*
    在庫を確認
     */
    public boolean isStocked(int applyNum) {
        return this.stockedQuantity >= applyNum;
    }

    /*
    在庫数を取得
     */
    public int getStockedQuantity() {
        return this.stockedQuantity;
    }
}
