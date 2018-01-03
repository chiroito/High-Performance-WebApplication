package domain.dao;

import domain.Product;
import domain.dao.exception.DataStoreException;

import java.io.Serializable;

public interface ProductDao extends Serializable {

    Product find(int planNo) throws DataStoreException;

    void save(Product product) throws DataStoreException;
}
