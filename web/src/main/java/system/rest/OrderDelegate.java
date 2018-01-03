package system.rest;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.TypeAssertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.cache.entity.CachedStock;
import system.cache.delegate.OrderEntryProcessor;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.concurrent.TimeUnit;

@Path("ep")
@RequestScoped
public class OrderDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger("DELEGATE_PERFORMANCE_LOG");

    @GET
    @Produces("text/plain")
    public long order(@QueryParam("productNo") int productNo, @QueryParam("quantity") int quantity, @QueryParam("userId") int userId) {

        // 在庫キャッシュの取得と In-place 処理となる EntryProcessor を作成する
        NamedCache<Integer, CachedStock> stockCache = CacheFactory.getTypedCache("Stock", TypeAssertion.withTypes(Integer.class, CachedStock.class));
        final OrderEntryProcessor inplaceProcess = new OrderEntryProcessor(productNo, quantity, userId);

        // In-place 処理を実行する
        long processNanoSeconds = stockCache.invoke(productNo, inplaceProcess);

        long processMicroSeconds = TimeUnit.MICROSECONDS.convert(processNanoSeconds, TimeUnit.NANOSECONDS);
        LOGGER.info(String.valueOf(processMicroSeconds));

        return processMicroSeconds;
    }
}
