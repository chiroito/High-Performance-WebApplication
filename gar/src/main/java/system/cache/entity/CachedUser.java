package system.cache.entity;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;

import java.io.IOException;

public class CachedUser implements PortableObject {

    public String name;

    public CachedUser() {
    }

    public CachedUser(String name) {
        this.name = name;
    }

    @Override
    public void readExternal(PofReader pofReader) throws IOException {
        this.name = pofReader.readString(0);
    }

    @Override
    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeString(0, this.name);
    }
}
