package system.cache.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class CachedOrder implements PortableObject {

    public int quantity;
    public int customerId;

    public CachedOrder() {
        super();
    }

    public CachedOrder(int quantity, int customerId) {
        this.quantity = quantity;
        this.customerId = customerId;
    }

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        this.quantity = pofReader.readInt(0);
        this.customerId = pofReader.readInt(1);
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeInt(0, this.quantity);
        pofWriter.writeInt(1, this.customerId);
    }

    @Override
    public String toString() {
        return "CachedOrder{" +
                "quantity=" + quantity +
                ", customerId=" + customerId +
                '}';
    }
}
