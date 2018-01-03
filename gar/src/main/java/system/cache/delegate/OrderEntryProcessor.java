package system.cache.delegate;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.processor.AbstractProcessor;
import domain.*;
import domain.dao.OrderDao;
import domain.dao.ProductDao;
import domain.dao.UserDao;
import domain.dao.exception.DataStoreException;
import domain.dao.exception.DataStoreRuntimeException;
import system.cache.entity.CachedStock;
import system.cache.putget.UserDaoCache;

import java.io.IOException;

public class OrderEntryProcessor extends AbstractProcessor<Integer, CachedStock, Long> implements PortableObject {

    private static final UserDao userDao = new UserDaoCache();

    public int productNo;
    public int quantity;
    public int userId;

    public OrderEntryProcessor() {
    }

    public OrderEntryProcessor(int productNo, int quantity, int userId) {
        this.productNo = productNo;
        this.quantity = quantity;
        this.userId = userId;
    }

    // TODO: 本来なら存在チェックをするが、このサンプルで伝える目的ではないため省略
    @Override
    public Long process(InvocableMap.Entry<Integer, CachedStock> lockedStockEntry) {

        BackingMapManagerContext context = ((BinaryEntry<Integer, CachedStock>) lockedStockEntry).getContext();
        ProductDao productDao = new ProductDaoEp(context, lockedStockEntry);
        OrderDao orderDao = new OrderDaoEp(context);

        try {
            long start = System.nanoTime();

            // 購入対象となる商品をデータソースから取得する
            Product product = productDao.find(productNo);
            User user = userDao.find(userId);
            Order order = user.purchase(product, quantity);
            orderDao.save(order);
            productDao.save(product);

            long end = System.nanoTime();
            return (end - start);

        } catch (DataStoreException e) {
            throw new DataStoreRuntimeException("EntryProcessor の処理中に例外が発生しました", e);
        }
    }

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        this.productNo = pofReader.readInt(0);
        this.quantity = pofReader.readInt(1);
        this.userId = pofReader.readInt(2);
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeInt(0, this.productNo);
        pofWriter.writeInt(1, this.quantity);
        pofWriter.writeInt(2, this.userId);
    }
}
