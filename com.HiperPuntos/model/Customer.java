package model;


import java.util.List;

public class Customer {

    private String dni;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Double availablePoints;

    private List<Product> myProducts;

    public Customer(String dni, String fullName, String phoneNumber, String email, Double availablePoints) {
        this.dni = dni;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.availablePoints = availablePoints;
    }

    public String getDni() {
        return dni;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Double getAvailablePoints() {
        return availablePoints;
    }

    public void addPoints(double points){
        this.availablePoints += points;
    }

    public void discountPoints(double points){
        this.availablePoints -= points;
    }
}
