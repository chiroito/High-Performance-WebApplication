package system.cache.delegate;

import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.Binary;
import com.tangosol.util.Converter;
import domain.Order;
import domain.dao.OrderDao;
import system.cache.entity.CachedOrder;
import system.cache.entity.CachedOrderKey;

import java.util.Map;

public class OrderDaoEp implements OrderDao {

    private final BackingMapManagerContext context;

    public OrderDaoEp(BackingMapManagerContext context) {
        this.context = context;
    }

    @Override
    public void save(Order order) {

        // ドメインのデータ構造から Coherence 内部のデータ構造へ変換
        Converter<CachedOrderKey, Binary> b2kConverter = this.context.getKeyToInternalConverter();
        CachedOrderKey key = new CachedOrderKey(order.orderId, order.product.productNo);
        Binary bKey = b2kConverter.convert(key);
        CachedOrder value = new CachedOrder(order.quantity, order.customer.userId);

        // オブジェクトをキャッシュする
        BackingMapContext orderBackingMap = this.context.getBackingMapContext("Order");
        Map.Entry<CachedOrderKey, CachedOrder> cachedOrderEntry = orderBackingMap.getBackingMapEntry(bKey);
        cachedOrderEntry.setValue(value);
    }
}
