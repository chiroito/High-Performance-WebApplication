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
import system.cache.putget.OrderDaoCache;
import system.cache.putget.ProductDaoCache;
import system.cache.putget.UserDaoCache;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

@Path("cache")
@RequestScoped
public class OrderCache {

    private static final Logger LOGGER = LoggerFactory.getLogger("CACHE_PERFORMANCE_LOG");

    @GET
    @Produces("text/plain")
    public long order(@QueryParam("productNo") int productNo, @QueryParam("quantity") int quantity, @QueryParam("userId") int userId) {

        ProductDao productDao = new ProductDaoCache();
        OrderDao orderDao = new OrderDaoCache();
        UserDao userDao = new UserDaoCache();

        try {
            long start = System.nanoTime();

            // 購入対象となる商品をデータソースから取得する
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
            throw new RestException("CacheのDAOで問題が発生しました", e);
        }
    }
}
