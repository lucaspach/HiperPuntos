package dao;

import model.Customer;

import java.util.List;

public interface CustomerDAO {

    void add(Customer customer) throws DAOException;
    void update(Customer customer) throws DAOException;
    void delete(int dni) throws DAOException;
    Customer findByDni(String dni) throws DAOException;
    List<Customer> findAll() throws DAOException;


}
