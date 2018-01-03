package system.cache.putget;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.TypeAssertion;
import domain.Order;
import domain.dao.OrderDao;
import system.cache.entity.CachedOrder;
import system.cache.entity.CachedOrderKey;

public class OrderDaoCache implements OrderDao {

    private NamedCache<CachedOrderKey, CachedOrder> orderCache = CacheFactory.getTypedCache("Order", TypeAssertion.withTypes(CachedOrderKey.class, CachedOrder.class));

    @Override
    public void save(Order order) {

        // ドメインのデータ構造から Coherence にキャッシュするデータ構造へ変換
        CachedOrderKey key = new CachedOrderKey(order.orderId, order.product.productNo);
        CachedOrder value = new CachedOrder(order.quantity, order.customer.userId);

        // オブジェクトをキャッシュする
        orderCache.put(key, value);
    }
}
