package system.sql;

import domain.Order;
import domain.dao.OrderDao;
import domain.dao.exception.DataStoreException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDaoSql implements OrderDao {

    private final Connection con;

    public OrderDaoSql(Connection con) {
        this.con = con;
    }


    private static final String INSERT_SQL = "INSERT INTO order_t (order_id, customer, product_no, quantity) VALUES (?, ?, ?, ?)";

    @Override
    public void save(Order order) throws DataStoreException {

        // ドメインのデータ構造をデータベースへ格納する
        try (PreparedStatement orderStmt = con.prepareStatement(INSERT_SQL)) {

            orderStmt.setString(1, order.orderId);
            orderStmt.setInt(2, order.customer.userId);
            orderStmt.setInt(3, order.product.productNo);
            orderStmt.setInt(4, order.quantity);
            orderStmt.execute();

        } catch (SQLException e) {
            throw new DataStoreException("データベースに問題が発生しました。", e);
        }
    }
}
