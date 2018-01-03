package system.rest;

import domain.Order;
import domain.Product;
import domain.User;
import domain.dao.OrderDao;
import domain.dao.ProductDao;
import domain.dao.UserDao;
import domain.dao.exception.DataStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.sql.OrderDaoSql;
import system.sql.ProductDaoSql;
import system.sql.UserDaoSql;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

@Path("db")
@RequestScoped
public class OrderSql {

    private static final Logger LOGGER = LoggerFactory.getLogger("DB_PERFORMANCE_LOG");

    @Resource(lookup = "jdbc/TechDeepDive")
    private DataSource ds;

    @GET
    @Produces("text/plain")
    @Transactional(Transactional.TxType.REQUIRED)
    public long order(@QueryParam("productNo") int productNo, @QueryParam("quantity") int quantity, @QueryParam("userId") int userId) {

        try (Connection con = this.getConnection()) {

            ProductDao productDao = new ProductDaoSql(con);
            UserDao userDao = new UserDaoSql(con);
            OrderDao orderDao = new OrderDaoSql(con);

            long start = System.nanoTime();

            Product product = productDao.find(productNo);
            User user = userDao.find(userId);
            Order order = user.purchase(product, quantity);
            orderDao.save(order);
            productDao.save(product);

            long end = System.nanoTime();
            long processMicroSeconds = TimeUnit.MICROSECONDS.convert(end - start, TimeUnit.NANOSECONDS);
            LOGGER.info(String.valueOf(processMicroSeconds));
            return processMicroSeconds;

        } catch (DataStoreException e) {
            throw new RestException("SQLのDAOで問題が発生しました", e);

        } catch (SQLException e) {
            throw new RestException("JAX-RS内でデータベースの問題が発生しました", e);
        }
    }

    private Connection getConnection() throws SQLException {

        Connection con = this.ds.getConnection();
        con.setAutoCommit(false);

        return con;
    }
}
