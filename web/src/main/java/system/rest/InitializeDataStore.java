package system.rest;

import com.tangosol.net.NamedCache;
import system.cache.entity.*;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.ws.rs.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.IntStream;

@Path("init")
@RequestScoped
public class InitializeDataStore {

    @Resource(mappedName = "Product")
    private NamedCache<Integer, CachedProduct> productCache;

    @Resource(mappedName = "User")
    private NamedCache<Integer, CachedUser> userCache;

    @Resource(mappedName = "Order")
    private NamedCache<CachedOrderKey, CachedOrder> orderCache;

    @Resource(mappedName = "Stock")
    private NamedCache<Integer, CachedStock> stockCache;

    private static final String TRUNCATE_SQL = "TRUNCATE TABLE %s";
    private static final String[] TABLES = {"order_t", "product_t", "stock_t", "user_t"};

    private static final String INSERT_USER_SQL = "INSERT INTO user_t (user_id, name) VALUES (?, ?)";
    private static final String INSERT_PRODUCT_SQL = "INSERT INTO product_t (product_no, name) VALUES (?, ?)";
    private static final String INSERT_STOCK_SQL = "INSERT INTO stock_t (product_no, quantity) VALUES (?, ?)";

    @Resource(lookup = "jdbc/TechDeepDive")
    private DataSource ds;

    @GET
    @Produces("text/plain")
    public String init(@QueryParam("quantity") @DefaultValue("1000000") int quantity, @QueryParam("productNum") @DefaultValue("10000") int productNum, @QueryParam("userNum") @DefaultValue("10000") int userNum) {

        // Truncate all cache
        productCache.clear();
        stockCache.clear();
        userCache.clear();
        orderCache.clear();

        IntStream.range(0, productNum).forEach(productNo -> {
            productCache.put(productNo, new CachedProduct("あめちゃん" + productNo));
            stockCache.put(productNo, new CachedStock(quantity));
        });

        IntStream.rangeClosed(0, userNum).forEach(userId -> {
            userCache.put(userId, new CachedUser("ユーザ" + userId));
        });

        // データベースの初期化
        try (Connection con = this.getCustomizedConnection(ds)) {

            for (String table : TABLES) {
                String truncateSql = String.format(TRUNCATE_SQL, table);
                try (PreparedStatement truncStmt = con.prepareStatement(truncateSql)) {
                    truncStmt.execute();
                }
            }

            try (PreparedStatement userStmt = con.prepareStatement(INSERT_USER_SQL)) {
                for (int userId = 0; userId < userNum; userId++) {
                    userStmt.setInt(1, userId);
                    userStmt.setString(2, "ユーザ" + userId);
                    userStmt.addBatch();
                    userStmt.clearParameters();
                }
                userStmt.executeBatch();
            }

            try (PreparedStatement productStmt = con.prepareStatement(INSERT_PRODUCT_SQL); PreparedStatement stockStmt = con.prepareStatement(INSERT_STOCK_SQL)) {
                for (int productNo = 0; productNo < productNum; productNo++) {
                    productStmt.setInt(1, productNo);
                    productStmt.setString(2, "あめちゃん" + productNo);
                    productStmt.addBatch();
                    productStmt.clearParameters();

                    stockStmt.setInt(1, productNo);
                    stockStmt.setInt(2, quantity);
                    stockStmt.addBatch();
                    stockStmt.clearParameters();
                }

                productStmt.executeBatch();
                stockStmt.executeBatch();
            }

            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Initialized DataSource!";
    }

    private Connection getCustomizedConnection(DataSource ds) throws SQLException {
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        return con;
    }
}
