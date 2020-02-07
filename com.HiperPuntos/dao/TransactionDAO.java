package dao;

import extra.Transaction;

import java.util.List;

public interface TransactionDAO {

    void add(Transaction transaction) throws DAOException;
    List<Transaction> findByDni(String dni) throws DAOException;

}
