package system.cache.delegate;

import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.Binary;
import com.tangosol.util.Converter;
import domain.Product;
import domain.Stock;
import domain.dao.ProductDao;
import system.cache.entity.CachedProduct;
import system.cache.entity.CachedStock;

import java.util.Map.Entry;

public class ProductDaoEp implements ProductDao {

    private final BackingMapManagerContext context;

    // TODO: キャッシュから取ったものを保持する。本来はフレームワークで実装する
    private final Entry<Integer, CachedStock> cachedStockEntry;

    public ProductDaoEp(BackingMapManagerContext context, Entry<Integer, CachedStock> cachedStockEntry) {
        this.context = context;
        this.cachedStockEntry = cachedStockEntry;
    }

    // TODO: 本来なら存在チェックを含めるが、このサンプルで伝える目的ではないため省略
    @Override
    public Product find(int productNo) {

        // キャッシュへアクセスする準備
        Converter<Integer, Binary> key2binConverter = this.context.getKeyToInternalConverter();
        Binary binaryProductKey = key2binConverter.convert(productNo);
        BackingMapContext productBackingMap = this.context.getBackingMapContext("Product");

        // キャッシュされたオブジェクトを取得
        Entry<Integer, CachedProduct> productEntry = productBackingMap.getReadOnlyEntry(binaryProductKey);
        CachedProduct cachedProduct = productEntry.getValue();
        CachedStock cachedStock = this.cachedStockEntry.getValue();

        // ドメインオブジェクトの生成
        Product product = new Product(productNo, cachedProduct.productName);
        Stock stock = new Stock(product, cachedStock.getStockedQuantity());

        return product;
    }


    // TODO: 保持していた情報を取得するのは本来フレームワークで実装すべきです。
    @Override
    public void save(Product product) {

        // ドメインのデータ構造から Coherence 内部のデータ構造へ変換
        CachedStock cachedStock = this.cachedStockEntry.getValue();
        cachedStock.setStockedQuantity(product.stock.getStockedQuantity());
        Converter<Integer, Binary> key2binConverter = this.context.getKeyToInternalConverter();
        Binary binaryCachingStockKey = key2binConverter.convert(product.productNo);

        // オブジェクトをキャッシュする
        BackingMapContext stockBackingMap = this.context.getBackingMapContext("Stock");
        Entry<Integer, CachedStock> cachedStockEntry = stockBackingMap.getBackingMapEntry(binaryCachingStockKey);
        cachedStockEntry.setValue(cachedStock);
    }
}
