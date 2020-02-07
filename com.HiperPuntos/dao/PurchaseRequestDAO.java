package dao;

import model.Product;

import java.util.List;

public interface PurchaseRequestDAO {

    void add(int idProduct) throws DAOException;
    List<Product> findAll() throws DAOException;

}
