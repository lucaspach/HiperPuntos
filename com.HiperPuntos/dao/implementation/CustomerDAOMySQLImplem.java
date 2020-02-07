package dao.implementation;

import dao.CustomerDAO;
import dao.DAOException;
import extra.JDBCUtil;
import model.Customer;

import java.sql.*;
import java.util.List;

public class CustomerDAOMySQLImplem implements CustomerDAO {

    @Override
    public void add(Customer customer) throws DAOException {
        String query = "INSERT INTO customers (dni, fullName, phoneNumber, email, availablePoints)"
                + "VALUES (?, ?, ?, ?, ?)";
        int result = 0;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            ps.setString(1, customer.getDni());
            ps.setString(2, customer.getFullName());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getEmail());
            ps.setDouble(5, customer.getAvailablePoints());

            result = ps.executeUpdate();

            if (result == 0)
                throw new DAOException("0 registros añadidos");
        } catch (SQLException sqle){
            throw new DAOException("No se pudo almacenar el cliente", sqle);
        }
    }

    @Override
    public void update(Customer customer) throws DAOException {
        String query = "UPDATE customers set availablePoints = ? WHERE dni = ?";
        int result = 0;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)) {
            ps.setDouble(1, customer.getAvailablePoints());
            ps.setString(2, customer.getDni());

            result = ps.executeUpdate();
            if (result == 0)
                throw new DAOException("0 registros actualizados");
        } catch (SQLException sqle){
            throw  new DAOException("No se pudo almacenar el cliente", sqle);
        }

    }

    @Override
    public void delete(int dni) {

    }

    @Override
    public Customer findByDni(String dni) throws DAOException {
        String query = "SELECT * FROM customers WHERE dni = ?";
        //boolean searchSucessfully = false;
        Customer customer = null;
        ResultSet rs = null;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){

            ps.setString(1, dni);
            rs = ps.executeQuery();
            if (rs.next()) {
                //System.out.println(rs.getString(2));
                // Creamos el objeto Customer
                String fullName = rs.getString("fullName");
                String phoneNumber = rs.getString("phoneNumber");
                String email = rs.getString("email");
                Double points = rs.getDouble("availablePoints");
                customer = new Customer(dni, fullName, phoneNumber, email, points);

                //searchSucessfully = true;
            }
        } catch (SQLException sqle){
            throw new DAOException("No se pudo buscar el cliente", sqle);
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
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }
}
