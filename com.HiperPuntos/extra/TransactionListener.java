package extra;
import dao.CustomerDAO;
import dao.DAOException;
import dao.TransactionDAO;
import dao.implementation.CustomerDAOMySQLImplem;
import dao.implementation.TransactionDAOMySQLImplem;
import model.Customer;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class TransactionListener implements Runnable{

    private static void processFile(Path p) throws IOException, DAOException {
        //System.out.printf("\nProcesando: %s\n",
        //        p.getFileName().toString()
        //);
        // Aca iría el método nuevo
        List<String> all = Files.readAllLines(p);
        String[] splitTransaction;
        for (String s : all) {
            // Aca debería generar un objeto de tipo Transaction y llevarlo a la base de datos a almacenarlo
            // una vez hecho esto, buscar el Usuario X y sumarle puntos
            splitTransaction = s.split(";");
            if (splitTransaction.length == 3) {
                String dni = splitTransaction[0];
                Double points = Double.parseDouble(splitTransaction[1]);
                String commerce = splitTransaction[2];

                Transaction t = new Transaction(dni, points, commerce);
                TransactionDAO tDAO = new TransactionDAOMySQLImplem();
                tDAO.add(t);

                // Ahora debemos buscar el cliente x DNI y pasarle
                CustomerDAO cDAO = new CustomerDAOMySQLImplem();
                Customer customer = cDAO.findByDni(dni);
                if(customer != null) {
                    customer.addPoints(points);
                    // aca el Update de puntos del cliente
                    cDAO.update(customer);

                }
            } else {
                System.out.println("Registro incompleto: " + s);
            }
        }
    }

    private static void moveFile(Path file, Path dirDest) throws IOException{
        Path fileName = file.getFileName();
        Path fileDestiny = dirDest.resolve(fileName);
        //Path fileDestiny = dirDest;
        Files.move(file, fileDestiny);
    }

    @Override
    public void run() {
        // Primero tenemos que setear la ruta de búsqueda
        Path transactions = Paths.get("./transacciones");
        Path process = Paths.get("./procesados");

        while (true) {
            try (DirectoryStream<Path> stream
                         = Files.newDirectoryStream(
                    transactions,
                    "*.csv")) {
                for (Path transaction : stream) {
                    processFile(transaction);
                    moveFile(transaction, process);
                }
            } catch (IOException | DAOException ex) {
                ex.printStackTrace();
            }
            // Le damos un respiro
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
