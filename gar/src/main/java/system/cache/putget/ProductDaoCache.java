package system.cache.putget;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.TypeAssertion;
import domain.Product;
import domain.Stock;
import domain.dao.ProductDao;
import system.cache.entity.CachedProduct;
import system.cache.entity.CachedStock;

public class ProductDaoCache implements ProductDao {

    private NamedCache<Integer, CachedProduct> productCache = CacheFactory.getTypedCache("Product", TypeAssertion.withTypes(Integer.class, CachedProduct.class));

    private NamedCache<Integer, CachedStock> stockCache = CacheFactory.getTypedCache("Stock", TypeAssertion.withTypes(Integer.class, CachedStock.class));

    // TODO: 本来なら存在チェックを含めるが、このサンプルで伝える目的ではないため省略
    // TODO: フレームワークで必ずアンロックされるようにすること。
    @Override
    public Product find(int productNo) {

        // キャッシュされたオブジェクトを取得
        CachedProduct cachedProduct = productCache.get(productNo);
        // 在庫をロック
        stockCache.lock(productNo,-1);
        CachedStock cachedStock = stockCache.get(productNo);

        // ドメインオブジェクトの生成
        Product product = new Product(productNo, cachedProduct.productName);
        Stock stock = new Stock(product, cachedStock.getStockedQuantity());

        return product;
    }

    // TODO: フレームワークで必ずアンロックされるようにすること。
    @Override
    public void save(Product product) {

        // ドメインのデータ構造から Coherence にキャッシュするデータ構造へ変換
        CachedStock stock = new CachedStock(product.stock.getStockedQuantity());

        // オブジェクトをキャッシュする
        stockCache.put(product.productNo, stock);
        // 在庫をアンロック
        stockCache.unlock(product.productNo);
    }
}
