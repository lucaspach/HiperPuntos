package extra;

public class Transaction {

    public Transaction (String dni, double points, String commerce){
        this.dni = dni;
        this.points = points;
        this.commerce = commerce;
    }

    private String dni;
    private double points;
    private String commerce;

    public String getDni(){
        return this.dni;
    }
    public double getPoints(){
        return  this.points;
    }
    public String getCommerce(){
        return this.commerce;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                " Puntos=" + points +
                ", Comercio='" + commerce + '\'' +
                " }";
    }
}
