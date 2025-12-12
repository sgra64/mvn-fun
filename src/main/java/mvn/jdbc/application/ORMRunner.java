package mvn.jdbc.application;

import java.util.stream.Stream;

import mvn.jdbc.datamodel.Customer;
import mvn.jdbc.datamodel.Reservation;
import mvn.jdbc.datamodel.Vehicle;


public class ORMRunner implements Application.Runnable {

    /**
     * Method of {@link Application.Runnable} interface invoked to run the demo
     */
    @Override
    public void run(ApplicationContext context) {
        // 
        final Database db = Database.builder()
            // 
            // .db_url("jdbc:h2:mem:freerider")        // in-memory database (volatile)
            .db_url("jdbc:h2:file:./.database/orm-db")  // persistent database
            .db_user("sa")
            .db_password("")
            // 
            .schema("file:src/main/resources/db-schema.sql")
            .data("file:src/main/resources/db-data.sql")
            .build();

        ORM orm = ORM.getInstance(db);
        Stream<Customer> customersStream = Stream.of();

        customersStream = orm.<Customer>query(Customer.class, "SELECT * FROM CUSTOMER");

        // customersStream = orm.<Customer>query(Customer.class, "SELECT * FROM CUSTOMER WHERE ID=?", ps -> {
        //     ps.setLong(1, 102L);
        // });

        // customersStream = orm.<Customer>query(Customer.class, "SELECT * FROM CUSTOMER WHERE ID IN (?, ?, ?)", ps -> {
        //     ps.setLong(1, 102L);
        //     ps.setLong(2, 104L);
        //     ps.setLong(3, 106L);
        // });

        System.out.println("Customers:");
        TableFormatter tfc = context.customerTableFormatterBuilder().build();
            tfc.header();
            long count = customersStream
                .peek(tfc::row)
                .filter(c -> true)  // force execution of peek()
                .count();
            // 
            tfc.footer();
            tfc.print(System.out);
            // 
            System.out.println(String.format("(%d rows)\n", count));


        // Stream<Vehicle> vehiclesStream =
        //     orm.<Vehicle>query(Vehicle.class, "SELECT * FROM VEHICLE");
        // // 
        // System.out.println("Vehicles:");
        // TableFormatter tfv = context.vehicleTableFormatterBuilder().build();
        //     tfv.header();
        //     count = vehiclesStream
        //         .peek(tfv::row)
        //         .filter(c -> true)  // force execution of peek()
        //         .count();
        //     // 
        //     tfv.footer();
        //     tfv.print(System.out);
        //     // 
        //     System.out.println(String.format("(%d rows)\n", count));


        // System.out.println("Reservations:");
        // Stream<Reservation> reservationsStream =
        //     orm.<Reservation>query(Reservation.class, "SELECT * FROM RESERVATION");
        // // 
        // TableFormatter tfr = context.reservationTableFormatterBuilder().build();
        //     tfr.header();
        //     count = reservationsStream
        //         .peek(tfr::row)
        //         .filter(c -> true)  // force execution of peek()
        //         .count();
        //     // 
        //     tfr.footer();
        //     tfr.print(System.out);
        //     // 
        //     System.out.println(String.format("(%d rows)\n", count));
    }
}
