package system.rest;

import com.tangosol.net.NamedCache;
import system.cache.entity.CachedOrder;
import system.cache.entity.CachedOrderKey;
import system.cache.entity.CachedStock;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("show")
@RequestScoped
public class ShowCache extends HttpServlet {

    @Resource(mappedName = "Order")
    private NamedCache<CachedOrderKey, CachedOrder> orderCache;

    @Resource(mappedName = "Stock")
    private NamedCache<Integer, CachedStock> stockCache;

    @GET
    @Produces("text/plain")
    public String log() {
        StringBuilder output = new StringBuilder("**************************" + System.lineSeparator());
        output.append("Order Num : " + orderCache.size() + System.lineSeparator());
        output.append("**************************" + System.lineSeparator());
        stockCache.forEach((k, v) -> output.append(k.toString() + " : " + v.toString() + System.lineSeparator()));
        return output.toString();
    }
}
