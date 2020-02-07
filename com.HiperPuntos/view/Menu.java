package view;


import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import dao.*;
import dao.implementation.CustomerDAOMySQLImplem;
import dao.implementation.ProductDAOMySQLImplem;
import dao.implementation.PurchaseRequestDAOMySQLImplem;
import dao.implementation.TransactionDAOMySQLImplem;
import extra.Transaction;
import extra.Validation;
import model.Customer;
import model.Product;
import org.w3c.dom.ls.LSOutput;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private Scanner sc = new Scanner(System.in);;

    public void mainMenu() {
        int option = 0;
        do {
            loadWelcomeMenu();
            System.out.println("Ingrese una opción:");
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException nfe){
                //nfe.printStackTrace();
                System.out.println("Formmato incorrecto, debe ingresar un número");
                mainMenu();
            }

            switch (option){
                case 1:
                    newCustomer();
                    break;
                case 2:
                    newProduct();
                    break;
                case 3:
                    modifyProduct();
                    break;
                case 4:
                    exchange();
                    break;
                case 5:
                    reportCustomer();
                    break;
                case 6:
                    reportProductOutStock();
                    break;
                default:
                    System.out.println("Opción incorrecta");
                    break;
            }
        } while (!(option == 0));
        System.out.println("Saliendo....");
    }


    private void loadWelcomeMenu(){
        System.out.println("*******************************************");
        System.out.println("\t*****Bienvenido a Hiper Puntos*****");
        System.out.println("*******************************************");


        System.out.println("1. Dar de alta nuevo cliente");
        System.out.println("2. Dar de alta nuevos productos");
        System.out.println("3. Modificar un producto existente");
        System.out.println("4. Realizar canje de un producto");
        System.out.println("5. Generar reporte de un cliente");
        System.out.println("6. Generar reporte de los productos a reponer");
        System.out.println("0. Salir del sistema");
    }

    private void newCustomer() {
        try {
            boolean telefonoOK = false;
            boolean emailOk = false;
            boolean saldoOk = false;

            System.out.println("");
            System.out.println("Ingrese dni del nuevo cliente");
            String dni = sc.nextLine();
            // buscamos que no exista
            CustomerDAO cDAOF = new CustomerDAOMySQLImplem();
            if (cDAOF.findByDni(dni) != null) {
                System.out.println("Cliente ya cargado en Sistema");
                System.out.println("");
                System.out.println("Volviendo al menú");
                System.out.println("......\n");
            }
            else{
                System.out.println("Ingrese apellido y nombre");
                String apellido = sc.nextLine();
                String telefono;
                String email;
                Double saldo;
                // Validacion telefono
                do {
                    System.out.println("Ingrese teléfono de contacto: (<codigo area> con el 0 delante) - <número>");
                    telefono = sc.nextLine();
                    if (Validation.customerPhoneNumber(telefono)) telefonoOK = true;
                    else System.out.println("Formato telefono incorrecto\n");
                } while (!telefonoOK);

                // Validacion email
                do {
                    System.out.println("Ingrese email con el formato: <nombre de cuenta>@<dominio>.com.ar");
                    email = sc.nextLine();
                    if (Validation.customerEmail(email)) emailOk = true;
                    else System.out.println("Formato email incorrecto\n");
                } while (!emailOk);

                // Validamos saldo y creamos el Cliente
                do {
                    System.out.println("Ingrese saldo inicial del nuevo cliente");
                    String s = sc.nextLine();
                    if (Validation.convertToPositiveDouble(s)){
                        saldo = Double.parseDouble(s);
                        saldoOk = true;
                        // datos correctos, creamos cliente
                        Customer c = new Customer(dni, apellido, telefono, email, saldo);
                        CustomerDAO cDAO = new CustomerDAOMySQLImplem();
                        cDAO.add(c);

                        System.out.printf("El cliente %s, DNI: %s fue creado exitosamente con un saldo inicial de $%.2f\n\n",
                                c.getFullName(), c.getDni(), c.getAvailablePoints());
                        Thread.sleep(300);
                    }
                    else System.out.println("Debe ingresar un número y positivo");
                } while (!saldoOk);
            }
        }catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("No se pudo completar la carga de datos del cliente " + ex.getMessage());
        }
    }

    private void newProduct(){
        try {
            sc = new Scanner(System.in);

            System.out.println("");
            System.out.println("Ingrese el id del producto");
            int id = Integer.parseInt(sc.nextLine());
            // buscamos que no exista
            ProductDAO pDAOF = new ProductDAOMySQLImplem();
            if (pDAOF.findById(id) != null) {
                System.out.println("Producto ya cargado en Sistema");
                System.out.println("");
                System.out.println("Volviendo al menú");
                System.out.println("......\n");
            }
            else{
                boolean datosOK = false;
                double costo;
                int stock;
                System.out.println("Ingrese descripcion del producto");
                String descripcion = sc.nextLine();
                try {
                    do {
                        System.out.println("Ingrese costo de puntos del producto");
                        String sCosto = sc.nextLine();

                        System.out.println("Ingrese stock inicial");
                        String sStock = sc.nextLine();

                        // Validamos que sean números y positivos
                        if (Validation.convertToPositiveDouble(sCosto) && Validation.convertToPositiveInteger(sStock)){
                            costo = Double.parseDouble(sCosto);
                            stock = Integer.parseInt(sStock);
                            datosOK = true;
                            // datos correctos, creamos producto
                            Product p = new Product(id, descripcion, costo, stock);
                            ProductDAO pDAO = new ProductDAOMySQLImplem();
                            pDAO.add(p);
                            System.out.printf("El producto código %d, Descripcion: %s fue creado exitosamente:" +
                                            "Costo de: $%.2f\n" +
                                            "Stock inicial: %d\n",
                                    p.getIdProduct(), p.getDescription(), p.getPointCost(), p.getStock());
                            Thread.sleep(300);

                        }
                        else System.out.println("Debe ingresar un número y positivo");
                    } while (!datosOK);

                } catch (NumberFormatException nE){
                    System.out.println("Debe ingresar un número y tiene que ser positivo");
                }
            }
        }catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("No se pudo completar la carga del producto");
        }

    }

    private void modifyProduct(){
        try{
            System.out.println("Ingrese el código del producto a modificar");
            int id = Integer.parseInt(sc.nextLine());
            //System.out.println("Ingrese los campos a modificar");
            ProductDAO pDAOF = new ProductDAOMySQLImplem();
            if (pDAOF.findById(id) != null) {
                Product p;
                p = pDAOF.findById(id);
                modifyProductFields(p);
            } else System.out.println("Producto no encontrado");
        } catch(Exception e){
            System.out.println("Fallo la busqueda" + e.toString());
        }
    }

    private void modifyProductFields(Product p) throws DAOException {
        int option = 0;
        String field = "";
        //System.out.println("El producto es:\n " + p.toString());
        System.out.println("Seleccione el campo que va a editar");
        do {
            System.out.println("1. Descripción: " +p.getDescription());
            System.out.println("2. Stock: " +p.getStock());
            System.out.println("3. Puntos " +p.getPointCost());
            //System.out.println("4. Todos"); no sense
            System.out.println("Seleccione una opcion");
            option = Integer.parseInt(sc.nextLine());

            switch (option) {
                case 1:
                    field = "description";
                    break;
                case 2:
                    field = "stock";
                    break;
                case 3:
                    field = "pointcost";
                    break;
                default:
                    System.out.println("Opción incorrecta");
                    break;
                }
            if(option == 1 || option == 2 || option ==3) {
                try {
                    System.out.println("Ingrese el nuevo valor del campo: " + field);
                    String fieldValue = sc.nextLine();
                    //Antes de cargarlo nos aseguramos que No sea negativa en caso de que sea Stock o Puntos
                    if (!checkFieldValue(fieldValue, field))
                         throw new RuntimeException();
                    //Aca mando a la base de datos el nuevo PN
                    ProductDAO pDAO = new ProductDAOMySQLImplem();
                    if (pDAO.modify(field, fieldValue, p.getIdProduct()) > 0) {
                        System.out.println("Se modifico correctamente");
                    } else
                        System.out.println("No se pudo modificar el producto");
                } catch (RuntimeException rE){
                    System.out.println("Debe ingresar un número y tiene que ser positivo");
                }
            }
            System.out.println("Desea modificar otro campo?\n 1. SI\t2. NO");
            option = Integer.parseInt(sc.nextLine());

        } while (option==1);

    }

    private boolean checkFieldValue(String value, String field){
        boolean result = false;
        try {
            switch (field) {
                case "description":
                    result = true;
                    break;
                //Debe ser un entero o un decimal, asíq lo comprobamos
                case "stock":
                    int stock = Integer.parseInt(value);
                    //Ahora comprobamos que es positivo
                    if (stock > 0)
                        result = true;
                    break;
                case "pointCost":
                    double pointCost = Double.parseDouble(value);
                    if (pointCost > 0)
                        result = true;
                    break;
                default:
                    System.out.println("Algo male sal");
                    break;
            }
        } catch (NumberFormatException nE){
            result = false;
        }
        return result;
    }

    public void exchange(){
        try {
            loadExchangeMenu(chooseCustomer());
        } catch (Exception ex){
           ex.printStackTrace();
        }
    }

    private Customer chooseCustomer() throws DAOException {
        Customer c = null;
        try {
            System.out.println("Ingrese el DNI del cliente que va a Canjear puntos");
            String dni = sc.nextLine();

            CustomerDAO cDAO = new CustomerDAOMySQLImplem();
            c = cDAO.findByDni(dni);
        } catch (DAOException ex){
            System.out.println("Cliente no encontrado");
        }
        return c;
    }

    private void loadExchangeMenu(Customer c) throws DAOException {
        // Primero obtenemos los productos disponibles
        ProductDAO pDAO = new ProductDAOMySQLImplem();
        try {
            // Agregar en caso que no haya listado disponibles
            List<Product> productList = pDAO.findAllWithStock();
            if (productList.isEmpty())
                System.out.println("No hay productos disponibles de momento, intente mas tarde");
            else {
                System.out.println("Seleccione un producto según el número asignado");
                int id = 0;
                for (Product s : productList) {
                    System.out.printf("%d. -- %s\n", id, s);
                    id++;
                    //System.out.println(s);
                }
                int option = Integer.parseInt(sc.nextLine());
                // Obtenemos el producto
                Product p = pDAO.findById(productList.get(option).getIdProduct());
                if (p != null) {
                    Double pointsCost = p.getPointCost();
                    // Validamos que el usuario tenga saldo
                    if (c.getAvailablePoints() < pointsCost)
                        System.out.println("Saldo insuficiente");
                    else {
                        // Lógica para la transacción
                        c.discountPoints(pointsCost);
                        // Actualizamos el saldo del usuario
                        CustomerDAO cDAO = new CustomerDAOMySQLImplem();
                        cDAO.update(c);
                        // Agregamos la transacción al histórico con saldo negativo
                        Double negativePoints = pointsCost * -1;
                        Transaction t = new Transaction(c.getDni(), negativePoints, p.getDescription());
                        TransactionDAO tDAO = new TransactionDAOMySQLImplem();
                        tDAO.add(t);
                        // Descontamos 1 en el stock del producto
                        int newStock = p.getStock() - 1;
                        pDAO.modify("stock", String.valueOf(newStock), p.getIdProduct());
                        // Chequeamos que el Stock no haya quedado en 0
                        if (newStock == 0) {
                            PurchaseRequestDAO prDAO = new PurchaseRequestDAOMySQLImplem();
                            prDAO.add(p.getIdProduct());
                            System.out.printf("El producto %s se quedo sin stock, añadido a tabla para reponer", p.getDescription());
                        }
                        System.out.printf("El cliente %s, DNI: %s canjeo exitosamente " +
                                        "el siguiente producto: %s\nSu nuevo puntaje es: %.2f puntos\nPuede pasar a retirarlo" +
                                        " a partir de las próximas 48hs hábiles, en el horario de 08hs a 16hs\n\n",
                                c.getFullName(), c.getDni(), p.getDescription(), c.getAvailablePoints());
                    }
                }
            }
        } catch (DAOException ex){
            throw new DAOException("Ocurrio un error al intentar canjear los puntos" + ex);
        } catch (Exception e){
            System.out.println("Excepción no contemplada: " + e.getMessage());
        }
    }

    public void reportCustomer(){
        try{
            getReportCustomer();
        } catch (Exception ex){
            System.out.println("No se encontró el cliente\n");
        }
    }

    private void getReportCustomer() throws DAOException {
        // Pedimos DNI del cliente y luego mostramos las transacciones
        System.out.println("Ingrese el DNI del cliente para generar el reporte");
        String dni = sc.nextLine();
        try {
            CustomerDAO cDAO = new CustomerDAOMySQLImplem();
            Customer c = cDAO.findByDni(dni);
            TransactionDAO tDAO = new TransactionDAOMySQLImplem();
            List<Transaction> transactionList = tDAO.findByDni(c.getDni());
            if (transactionList.isEmpty()){
                System.out.printf("El Cliente %s, DNI %s no cuenta con historico de operaciones\n"
                , c.getFullName(), c.getDni());
            } else {
                System.out.printf("Transacciones de: %s al día de hoy\n", c.getFullName());
                for (Transaction s : transactionList) {
                    System.out.println(s);
                }
            }
        } catch (DAOException ex){
            System.out.println("No se pudo listar los productos\n" + ex.getMessage());
        }
    }


    public void reportProductOutStock() {
        try{
            getReportProductOutStock();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getReportProductOutStock() throws DAOException {
        try {
            PurchaseRequestDAO prDAO = new PurchaseRequestDAOMySQLImplem();
            List<Product> productList = prDAO.findAll();
            if (productList.isEmpty())
                System.out.println("No se encontraron productos sin Stock");
            else {
                System.out.println("Se mostrara el listado de productos sin Stock");
                for (Product p : productList){
                    System.out.println(p);
                }
            }
        } catch (DAOException ex){
            System.out.println("No se pudo obtener los productos sin stock " + ex.getMessage());
        }
    }


}
