package domain.dao;

import domain.User;
import domain.dao.exception.DataStoreException;

public interface UserDao {
    User find(int uid) throws DataStoreException;
}
