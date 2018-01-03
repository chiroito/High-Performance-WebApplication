package system.sql;

import domain.Product;
import domain.Stock;
import domain.dao.ProductDao;
import domain.dao.exception.DataStoreException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDaoSql implements ProductDao {

    private final Connection con;

    public ProductDaoSql(Connection con) {
        this.con = con;
    }

    // TODO: 本来なら存在チェックを含めるが、このサンプルで伝える目的ではないため省略
    @Override
    public Product find(int productNo) throws DataStoreException {

        // DBに格納されたオブジェクトを取得
        try (PreparedStatement productStmt = createGetProductStatement(con, productNo); ResultSet productRS = productStmt.executeQuery(); PreparedStatement stockStmt = createGetStockStatement(con, productNo); ResultSet stockRS = stockStmt.executeQuery()) {

            productRS.next();
            stockRS.next();

            // ドメインオブジェクトの生成
            Product product = new Product(productNo, productRS.getString("NAME"));
            Stock stock = new Stock(product, stockRS.getInt("QUANTITY"));

            return product;

        } catch (SQLException e) {
            throw new DataStoreException("データベースに問題が発生しました。", e);
        }
    }

    private static final String UPDATE_STOCK_SQL = "UPDATE stock_t SET quantity = ? WHERE product_no = ?";

    // TODO: フレームワークで必ずトランザクションが終了されるようにすること。
    // TODO: 保持していた情報を取得するのは本来フレームワークで実装すべきです。
    @Override
    public void save(Product product) throws DataStoreException {

        // ドメインのデータ構造をデータベースへ格納する
        try (PreparedStatement stockStmt = con.prepareStatement(UPDATE_STOCK_SQL)) {

            stockStmt.setInt(1, product.stock.getStockedQuantity());
            stockStmt.setInt(2, product.productNo);
            stockStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataStoreException("データベースに問題が発生しました。", e);
        }
    }


    private static final String PRODUCT_SQL = "SELECT * FROM product_t WHERE product_no = ?";

    private PreparedStatement createGetProductStatement(Connection con, int productNo) throws SQLException {

        PreparedStatement productStmt = con.prepareStatement(PRODUCT_SQL);
        productStmt.setInt(1, productNo);

        return productStmt;
    }


    private static final String GET_STOCK_SQL = "SELECT * FROM stock_t WHERE product_no = ? FOR UPDATE";

    private PreparedStatement createGetStockStatement(Connection con, int productNo) throws SQLException {

        PreparedStatement stockStmt = con.prepareStatement(GET_STOCK_SQL);
        stockStmt.setInt(1, productNo);

        return stockStmt;
    }
}
