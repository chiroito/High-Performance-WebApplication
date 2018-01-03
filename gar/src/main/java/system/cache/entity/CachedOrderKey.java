package system.cache.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.cache.KeyAssociation;

import java.io.IOException;

public class CachedOrderKey implements PortableObject, KeyAssociation<Integer> {

    private String orderId;
    private int productNo;

    public CachedOrderKey() {
    }

    public CachedOrderKey(String orderId, int productNo) {
        this.orderId = orderId;
        this.productNo = productNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getProductNo() {
        return productNo;
    }

    public Integer getAssociatedKey() {
        return productNo;
    }

    public void readExternal(PofReader pofReader) throws IOException {
        this.productNo = pofReader.readInt(0);
        this.orderId = pofReader.readString(1);
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeInt(0, this.productNo);
        pofWriter.writeString(1, this.orderId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CachedOrderKey)) return false;

        CachedOrderKey that = (CachedOrderKey) o;

        if (productNo != that.productNo) return false;
        return orderId.equals(that.orderId);
    }

    @Override
    public int hashCode() {
        int result = orderId.hashCode();
        result = 31 * result + productNo;
        return result;
    }

    @Override
    public String toString() {
        return "CachedOrderKey{" +
                "orderId='" + orderId + '\'' +
                ", productNo=" + productNo +
                '}';
    }
}
