package dao;

import model.Product;

import java.util.List;

public interface ProductDAO {

    void add(Product product) throws  DAOException;
    int modify(String field, String fieldValue, int id) throws  DAOException;
    Product findById(int id) throws DAOException;
    List<Product> findAllWithStock() throws DAOException;

}
