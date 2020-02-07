package dao.implementation;

import dao.DAOException;
import dao.ProductDAO;
import extra.JDBCUtil;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOMySQLImplem implements ProductDAO {


    @Override
    public void add(Product product) throws DAOException {
        String query = "INSERT INTO products (idProduct, description, pointCost, stock)"
                + "VALUES (?, ?, ?, ?)";

        int result = 0;

        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(query)) {

            ps.setInt(1, product.getIdProduct());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPointCost());
            ps.setInt(4, product.getStock());

            result = ps.executeUpdate();

            if (result == 0)
                throw new DAOException("0 registros añadidos");

        } catch (SQLException sqle) {
            throw new DAOException("No se pudo almacenar el producto", sqle);
        }


    }

    @Override
    public int modify(String field, String fieldValue, int id) throws DAOException {
        //String query = "UPDATE products SET ? = ?" + " WHERE idProduct = ?";
        String query = "UPDATE products SET "+ field + " = ?" + " WHERE idProduct = ?";

        int result = 0;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            //ps.setString(1,field);
            ps.setString(1,fieldValue);
            ps.setInt(2, id);
            result = ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new DAOException("No se pudo modificar el producto", sqle);
        }

        return result;
    }

    @Override
    public Product findById(int id) throws DAOException {
        String query = "SELECT * FROM products WHERE idProduct = ?";
        ResultSet rs = null;
        Product p = null;

        try(Connection c = JDBCUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(query)){
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                p = new Product(id,rs.getString("description"), rs.getDouble("pointCost"),
                        rs.getInt("stock"));
            }
        } catch (SQLException sqle){
            throw new DAOException("No se pudo buscar el producto", sqle);
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
        return p;
    }

    @Override
    public List<Product> findAllWithStock() throws DAOException {
        String query = "SELECT * FROM products WHERE stock > 0";
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
