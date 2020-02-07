package model;


import java.util.List;

public class Product {

    private static List<Product> list;

    private int idProduct;
    private String description;
    private Double pointCost;
    private int stock;

    public Product() {
    }

    public Product(int idProduct, String description, Double pointCost, int stock) {
        this.idProduct = idProduct;
        this.description = description;
        this.pointCost = pointCost;
        this.stock = stock;
    }

    public int getIdProduct(){
        return  this.idProduct;
    }

    public String getDescription() {
        return description;
    }

    public Double getPointCost() {
        return pointCost;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                " idProduct=" + idProduct +
                " description='" + description + '\'' +
                ", pointCost=" + pointCost +
                ", stock=" + stock +
                " }";
    }
}
