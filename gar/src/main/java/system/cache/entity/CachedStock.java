package system.cache.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class CachedStock implements PortableObject {

    private int stockedQuantity;

    public CachedStock() {
    }

    public CachedStock(int stockedQuantity) {
        this.stockedQuantity = stockedQuantity;
    }

    public int getStockedQuantity() {
        return stockedQuantity;
    }

    public void setStockedQuantity(int stockedQuantity) {
        this.stockedQuantity = stockedQuantity;
    }

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        this.stockedQuantity = pofReader.readInt(0);

    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeInt(0, this.stockedQuantity);
    }

    @Override
    public String toString() {
        return "CachedStock{" +
                "stockedQuantity=" + stockedQuantity +
                '}';
    }
}
