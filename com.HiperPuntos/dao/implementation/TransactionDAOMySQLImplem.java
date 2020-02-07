package dao.implementation;

import dao.DAOException;
import dao.TransactionDAO;
import extra.JDBCUtil;
import extra.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOMySQLImplem implements TransactionDAO {


    @Override
    public void add(Transaction transaction) throws DAOException {
        String query = "INSERT INTO transactions (dniCustomer, points, commerce)"
                + "VALUES (?, ?, ?)";

        int result = 0;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            ps.setString(1, transaction.getDni());
            ps.setDouble(2, transaction.getPoints());
            ps.setString(3, transaction.getCommerce());

            result = ps.executeUpdate();

            if (result == 0)
                throw new DAOException("0 registros añadidos");

        } catch (SQLException sqle){
            throw new DAOException("No se pudo almacenar la transacción", sqle);
        }

    }

    @Override
    public List<Transaction> findByDni(String dni) throws DAOException {
        String query = "SELECT * FROM transactions WHERE dniCustomer = ?";
        ResultSet rs = null;
        List<Transaction> listT = new ArrayList<>();
        Transaction t = null;


        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            ps.setString(1, dni);

            rs = ps.executeQuery();
            while (rs.next()){
                t = new Transaction(rs.getString("dniCustomer"), rs.getDouble("points"),
                        rs.getString("commerce"));
                listT.add(t);
            }
        } catch (SQLException sqle){
            throw new DAOException("Error al listar los productos", sqle);
        } finally {
            if (rs != null){
                try{
                    rs.close();
                } catch (SQLException e){
                    e.printStackTrace();
                    System.out.printf("Error al cerrar la conexión, %s", e.toString());
                }
            }
        }

        return listT;

    }
}
