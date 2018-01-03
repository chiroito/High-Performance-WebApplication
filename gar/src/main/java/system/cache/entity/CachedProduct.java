package system.cache.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class CachedProduct implements PortableObject {

    public String productName;

    public CachedProduct() {
    }

    public CachedProduct(String productName) {
        this.productName = productName;
    }

    public void readExternal(PofReader pofReader) throws IOException {
        this.productName = pofReader.readString(0);
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, this.productName);
    }
}
