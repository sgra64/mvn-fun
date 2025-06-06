package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.StreamSupport;

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
        // 
        // try to open database connection
        try(
            final Connection dbcon = DriverManager.getConnection(db_url, db_user, db_password)
        ) {
            System.out.println("Database connection open.");
            // 
            var factory = CrudRepositoryFactory.getInstance(dbcon);
            var customerRepository = factory.getCustomerRepository();
            var vehicleRepository = factory.getVehicleRepository();

            var customers = customerRepository.findAll();
            printCustomerTable(customers);

            var vehicles = vehicleRepository.findAll();
            printVehicleTable(vehicles);
        // 
        } catch (SQLException e) {
            System.out.println(String.format("Error opening database connection(%s, %s, %s): \"%s\"",
                db_url, db_user, db_password, e.getMessage()));
        }
    }


    /**
     * Print {@link Customer} objects in table format:
     * <code>
     * +------+---------------------+---------------------+---------------+
     * | ID   | NAME                | CONTACT             | STATUS        |
     * +------+---------------------+---------------------+---------------+
     * | 1000 | Meyer, Eric         | eme22@gmail.com     | Active        |
     * | 1001 | Sommer, Tina        | 030 22458 29425     | Active        |
     * | 1002 | Schulze, Tim        | +49 171 2358124     | Active        |
     * +------+---------------------+---------------------+---------------+
     * </code>
     * @param customers customers to print
     */
    private void printCustomerTable(Iterable<Customer> customers) {
        // 
        final TableFormatter tf = new TableFormatter("| %-5s", "| %-20s", "| %-20s", "| %-13s |")
            .line()
            .row("ID", "NAME", "CONTACT", "STATUS")    // table header
            .line();

        StreamSupport.stream(customers.spliterator(), false)
            .forEach(c -> {
                String id = String.format("%d", c.id());
                String name = c.name();
                String contact = c.contact();
                String status = c.status().toString();
                //
                tf.row(id, name, contact, status);  // write row into table
        });

        tf.line();
        System.out.println(tf.get().toString());    // print table
    }

    /**
     * Print {@link VEHICLE} objects in table format:
     * +------+---------------------+---------------------+-----+---------+-----------+------------+
     * | ID   | MAKE                | MODEL               | SEA | CATGORY | POWER     | STATUS     |
     * +------+---------------------+---------------------+-----+---------+-----------+------------+
     * | 8000 | VW                  | Golf                |   4 | Sedan   | Gasoline  | Active     |
     * | 8001 | VW                  | Golf                |   4 | Sedan   | Hybrid    | Active     |
     * | 8002 | VW                  | Multivan Life       |   8 | Van     | Gasoline  | Active     |
     * | 8003 | BMW                 | 320d                |   4 | Sedan   | Diesel    | Active     |
     * | 8004 | Mercedes            | EQS                 |   4 | Sedan   | Electric  | Active     |
     * | 8005 | Tesla               | Model 3             |   4 | Sedan   | Electric  | Active     |
     * | 8006 | Tesla               | Model S             |   4 | Sedan   | Electric  | Serviced   |
     * +------+---------------------+---------------------+-----+---------+-----------+------------+
     * @param vehicles vehicles to print
     */
    private void printVehicleTable(Iterable<Vehicle> vehicles) {
        // 
        final TableFormatter tf = new TableFormatter("| %-5s", "| %-20s", "| %-20s", "| %3s ", "| %-8s", "| %-9s", "| %-10s |")
            .line()
            .row("ID", "MAKE", "MODEL", "SEA", "CATEG", "POWER", "STATUS")    // table header
            .line();

        StreamSupport.stream(vehicles.spliterator(), false)
            .forEach(v -> {
                String id = String.format("%d", v.id());
                String make = v.make();
                String model = v.model();
                String seats = "";
                String category = "";
                String power = "";
                String status = "";
                //
                tf.row(id, make, model, seats, category, power, status);  // write row into table
        });

        tf.line();
        System.out.println(tf.get().toString());    // print table
    }

    /**
     * Print {@link RESERVATION} objects in table format:
     * +-------+------+------+------------------+------------------+-----------------+----------------+------------+
     * | ID    |CUS_ID|VEH_ID| BEGIN            | END              | PICKUP          | DROP-OFF       | STATUS     |
     * +-------+------+------+------------------+------------------+-----------------+----------------+------------+
     * | 10000 | 1000 | 8002 | 2025-07-20 10:00 | 2025-07-20 20:00 | Berlin Wedding  | Berlin Wedding | Booked     |
     * | 10001 | 1001 | 8002 | 2025-07-04 20:00 | 2025-07-04 23:00 | Berlin Wedding  | Hamburg        | Inquired   |
     * | 10002 | 1000 | 8006 | 2025-07-18 18:00 | 2025-07-18 18:10 | Berlin Wedding  | Hamburg        | Inquired   |
     * | 10003 | 1002 | 8001 | 2025-06-05 21:55 | 2025-06-05 23:55 | Berlin Wedding  | Hamburg        | Inquired   |
     * | 10004 | 1002 | 8003 | 2025-07-18 09:00 | 2025-07-18 18:00 | Potsdam         | Teltow         | Inquired   |
     * +-------+------+------+------------------+------------------+-----------------+----------------+------------+
     * @param reservations reservations to print
     */

    /* implement */

}