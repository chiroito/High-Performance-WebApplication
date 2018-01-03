package domain.dao;

import domain.Order;
import domain.dao.exception.DataStoreException;

import java.io.Serializable;

public interface OrderDao extends Serializable {
    void save(Order order) throws DataStoreException;
}
