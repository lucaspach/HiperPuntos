package dao.implementation;

import dao.DAOException;
import dao.PurchaseRequestDAO;
import extra.JDBCUtil;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRequestDAOMySQLImplem implements PurchaseRequestDAO {

    @Override
    public void add(int idProduct) throws DAOException {
        String query = "INSERT INTO purchase_request (idProduct) VALUES (?)";
        int result = 0;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            ps.setInt(1, idProduct);

            result = ps.executeUpdate();

            if (result == 0)
                throw new DAOException("0 registros añadidos");
        } catch (SQLException sqle){
            throw new DAOException("No se pudo almacenar el cliente", sqle);
        }
    }

    @Override
    public List<Product> findAll() throws DAOException {
        String query = "SELECT products.* FROM purchase_request INNER JOIN products" +
                " ON purchase_request.idproduct = products.idproduct ORDER BY products.description ASC";
        List<Product> productList = new ArrayList<>();
        ResultSet rs = null;
        Product p = null;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){

            rs = ps.executeQuery();
            while (rs.next()){
                p = new Product(rs.getInt("idproduct"), rs.getString("description"), rs.getDouble("pointCost"),
                        rs.getInt("stock"));
                productList.add(p);
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

        return productList;
    }
}
