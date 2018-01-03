package system.cache.putget;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.TypeAssertion;
import domain.User;
import domain.dao.UserDao;
import system.cache.entity.CachedUser;

public class UserDaoCache implements UserDao {

    private NamedCache<Integer, CachedUser> userCache = CacheFactory.getTypedCache("User", TypeAssertion.withTypes(Integer.class, CachedUser.class));

    // TODO: 本来なら存在チェックを含めるが、このサンプルで伝える目的ではないため省略
    @Override
    public User find(int userId) {

        // キャッシュされたオブジェクトを取得
        CachedUser cachedUser = userCache.get(userId);

        // ドメインオブジェクトの生成
        User user = new User(userId, cachedUser.name);

        return user;
    }
}
