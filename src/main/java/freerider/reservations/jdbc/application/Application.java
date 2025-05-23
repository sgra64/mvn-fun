package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import freerider.reservations.jdbc.model.Customer;
import freerider.reservations.jdbc.model.Vehicle;
import freerider.reservations.jdbc.repository.CrudRepositoryFactory;


/**
 * Main Application class.
 */
public class Application {

    private final static Application application = new Application();

    /**
     * Program execution starts here.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        System.out.println(String.format("Hello \"%s\" example!",
            Application.class.getName().replace(".application.Application", "")));
        // 
        application.run(args);
    }

    /**
     * Run program with {@code Application} instance.
     * @param args arguments passed from the command line
     */
    void run(String[] args) {
        /*
         * Provide database connection information.
         */
        String db_url = "jdbc:h2:mem:freerider";
        // String db_url = "jdbc:h2:file:./.database/freerider.h2";
        String db_user = "sa";
        String db_password = "";

        // try to open database connection
        try(
            final Connection dbcon = DriverManager.getConnection(db_url, db_user, db_password)
        ) {
            System.out.println("Database connection open.");
            // 
            var factory = CrudRepositoryFactory.getInstance(dbcon);
            var customerRepository = factory.getCustomerRepository();
            var vehicleRepository = factory.getVehicleRepository();
            // var reservationRepository = factory.getReservationRepository();

            var customers = customerRepository.findAll();
            printCustomerTable(customers);

            var vehicles = vehicleRepository.findAll();
            printVehicleTable(vehicles);

            customerRepository.deleteAllById(List.of(2, 6));

            customers = customerRepository.findAllById(List.of(2, 4, 6));
            printCustomerTable(customers);

            var r = String.format("customers: %d", customerRepository.count());
            System.out.println(r);

            int id=2; r = String.format("customer id=%d: %s", id, customerRepository.existsById(id));
            System.out.println(r);

            id=22; r = String.format("customer id=%d: %s", id, customerRepository.existsById(id));
            System.out.println(r);
        // 
        } catch (SQLException e) {
            System.out.println(String.format("Error opening database connection(%s, %s, %s): \"%s\"",
                db_url, db_user, db_password, e.getMessage()));
        }
    }


    /**
     * Print {@link Customer} objects in table format:
     * <code>
     * +----+---------------------+---------------------+---------------------+
     * | ID | NAME                | CONTACT             | STATUS              |
     * +----+---------------------+---------------------+---------------------+
     * | 1  | Meyer, Eric         | eme@gmail.com       | Active              |
     * | 2  | Allister, Tony      | +49 030 2304245     | InRegistration      |
     * | 3  | Ohlstadt, Sandra    | ohlstadt@gmx.de     | Active              |
     * | 4  | Gronemann, Erica    | maus@bht-berlin.de  | Active              |
     * | 5  | Samadi, Khaleed     | mocka@gmail.com     | Active              |
     * | 6  | Medwedev, Igor      | +49 042 30452626    | Active              |
     * +----+---------------------+---------------------+---------------------+
     * </code>
     * @param customers customers to print
     */
    private void printCustomerTable(Iterable<Customer> customers) {
        final String fmt = "| %-2s | %-20s| %-20s| %-20s|\n";
        StringBuilder sb = new StringBuilder();
        String hline = String.format("+----+%s+%s+%s+", "-".repeat(21), "-".repeat(21), "-".repeat(21));
        sb.append(String.format("%s\n", hline));
        sb.append(String.format(fmt, "ID", "NAME", "CONTACT", "STATUS"));
        sb.append(String.format("%s\n", hline));
        // 
        customers.forEach(customer -> {
            String line = String.format(fmt, customer.id(), customer.name(),
                                    customer.contact(), customer.status().name());
            sb.append(line);
        });
        sb.append(String.format("%s", hline));
        System.out.println(sb.toString());
    }

    /**
     * Print {@link VEHICLE} objects in table format:
     * +----+-----------+-----------+-----+-----------+-----------+-----------+
     * | ID | MAKE      | MODEL     | SEA | CATEG     | POWER     | STATUS    |
     * +----+-----------+-----------+-----+-----------+-----------+-----------+
     * | 1  | VW        | Golf      | 4   | Sedan     | Diesel    | Active    |
     * +----+-----------+-----------+-----+-----------+-----------+-----------+
     * @param customers customers to print
     */
    private void printVehicleTable(Iterable<Vehicle> vehicles) {
        final String fmt = "| %-2s | %-10s| %-10s| %-4s| %-10s| %-10s| %-10s|\n";
        final String s1 = "-".repeat(11);
        StringBuilder sb = new StringBuilder();
        String hline = String.format("+----+%s+%s+%s+%s+%s+%s+", s1, s1, "-----", s1, s1, s1);
        sb.append(String.format("%s\n", hline));
        sb.append(String.format(fmt, "ID", "MAKE", "MODEL", "SEA", "CATEGORY", "POWER", "STATUS"));
        sb.append(String.format("%s\n", hline));
        // 
        vehicles.forEach(vehicle -> {
            String line = String.format(fmt, vehicle.id(), vehicle.make(), vehicle.model(), "", "", "", "");
            // 
            sb.append(line);
        });
        sb.append(String.format("%s", hline));
        System.out.println(sb.toString());
    }
}